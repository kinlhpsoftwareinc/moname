---
name: java-locks-singletons
description: Java intrinsic-lock hygiene and thread-safe lazy initialization. Replaces synchronized(this), synchronized methods and synchronized(X.class) with private final lock objects; implements singletons with the holder idiom, enum, or volatile double-checked locking; reviews code for exposed monitors, broken DCL, boxed/String locks, wait/notify misuse, class-init deadlock and reentrancy bugs. Use when writing, reviewing or refactoring Java code that uses synchronized, wait/notify, ReentrantLock, lazy initialization or static singletons, or when asked why locking on this or a Class object is an anti-pattern.
metadata:
  version: "1.0.0"
  last-verified: "2026-11-06"
  verified-against: "JLS SE 17/21/24, JVMS SE 21, JEP 390, JEP 491, OpenJDK 21 (javac/java)"
---

# Java private locks and thread-safe singletons

Every rule below carries a source tag such as `[S3]`; the list is at the end. Tags marked `[T]` were confirmed by compiling and running code on OpenJDK 21, not by a specification. Do not state anything in this area that the sources do not support; when unsure, say so and point to the source.

## 1. Hard rules (apply in every answer and every edit)

R1. **Never lock on a publicly reachable object.** A `synchronized` instance method locks `this`; a `static synchronized` method locks the `Class` object of the declaring class `[S1]`. Any code holding a reference to the instance (or able to reach the `Class` object, e.g. via `getClass()`) can acquire the same monitor and hold it, causing contention, deadlock or denial of service `[S8]`. Use a `private final` lock object instead `[S8]`.

R2. **The lock field must be `private` and `final`.** A non-final lock can be reassigned, so two threads may lock different objects and both enter the critical section `[S8]`. `final` also gives safe publication of the reference `[S2]`. Never return the lock, pass it to foreign code, or expose it as `protected` `[S8]`.

R3. **Never lock on objects that may be shared or reused:** `Boolean`, boxed primitives (`Integer`, `Long`, …), string literals, interned strings `[S9]`. Wrapper classes are value-based; since JDK 16 javac warns by default (`[synchronization]` lint) when you synchronize on them, and HotSpot can diagnose it at run time with `-XX:DiagnoseSyncOnValueBasedClasses=1|2` `[S10][T]`.

R4. **Call `wait` only inside a loop that re-checks the condition.** The JLS allows spurious wake-ups `[S3]`. Call `wait`/`notify`/`notifyAll` on the private lock, never on `this`.

R5. **Double-checked locking (DCL) on a mutable object requires a `volatile` field.** Without it, another thread may see a non-null reference to a partially constructed object; the `volatile` fix is valid only from JDK 5 (JSR-133) onward `[S11][S12]`. Read the field into a local variable once `[S13]`.

R6. **For a lazily initialized `static` value, prefer the holder idiom (or an `enum`) to hand-written DCL.** Class initialization is performed under a per-class initialization lock by the JVM, runs once, and a nested class is not initialized until first use `[S4][S11][S12]`.

R7. **Lazy initialization per instance cannot use the holder idiom** (a `static` field exists once per class, not per instance). Use instance-field DCL with `volatile`, a synchronized accessor, or a library memoizer `[S12][S14]`.

R8. **A "one per JVM" requirement needs a `static` field.** An instance field gives one value per object. And `static` is really one per *class loader*: a class at run time is identified by its binary name plus its defining loader `[S5]`.

R9. **Do not call unknown code while holding a lock** (listeners, overridable methods, callbacks, lambdas passed in). It can acquire other locks in arbitrary order; the JLS neither prevents nor detects deadlock `[S1]`.

R10. **Changing an existing class's locking policy can break clients.** If a class *documents* client-side locking on its own monitor (as `Collections.synchronizedList` does: callers must `synchronized (list)` while iterating `[S15]`), switching to a private lock is a breaking change. Flag it; do not do it silently. CERT also lists documented client-side locking as an exception to R1 `[S8]`.

## 2. Workflow in Claude Code

When asked to write, review or fix concurrent Java code:

1. **Find the Java version first** (e.g. `maven.compiler.release` / `<release>` in `pom.xml`, `toolchain`/`sourceCompatibility` in Gradle). Version-dependent advice: R3 lint (JDK 16+), virtual-thread pinning (§6.8, JDK 24+).
2. **Scan** with ripgrep (adjust paths):
   ```bash
   rg -n --type java 'synchronized\s*\(\s*this\s*\)'
   rg -n --type java '^\s*((public|protected|private|static|final|abstract)\s+)*synchronized\s+[\w<>\[\],.? ]+\s+\w+\s*\('   # synchronized methods
   rg -n --type java 'synchronized\s*\(\s*[A-Za-z_][\w.]*\.class\s*\)'
   rg -n --type java 'synchronized\s*\(\s*"'               # string literal lock
   rg -n --type java '\.wait\s*\('                         # verify each is inside a while loop
   rg -n --type java 'if\s*\([^)]*==\s*null\)' -A3        # candidate DCL; check for volatile
   ```
3. **Classify each hit** against R1–R10 and §6. For each finding report: file:line, rule ID, the concrete risk, and the minimal fix.
4. **Before replacing `synchronized(this)` or synchronized methods**, check Javadoc/README for documented client-side locking (R10). If found, ask before changing.
5. **Edit minimally**, keep behaviour, then build and run tests. If Error Prone is in the build, rely on its checks (§7).
6. **Never claim** anything listed in §8.

## 3. Private lock patterns

### 3.1 Instance lock
```java
public final class Counter {
    private final Object lock = new Object();
    private int value;                       // guarded by lock

    public void increment() {
        synchronized (lock) { value++; }
    }

    public int value() {
        synchronized (lock) { return value; }
    }
}
```

### 3.2 Named lock class (readable thread dumps)
A dedicated class makes the lock identifiable in `jcmd <pid> Thread.print` output (`- locked <0x…> (a com.example.Cache$CacheLock)` instead of `(a java.lang.Object)`) `[T]`. Make the class `static`: a non-static (inner) class instance carries a reference to its enclosing instance `[S6]`. Do not name it `Lock`: a member type named `Lock` shadows an imported `java.util.concurrent.locks.Lock` throughout the class body `[S7][T]`.
```java
public final class Cache {
    private static final class CacheLock {}
    private final Object lock = new CacheLock();
}
```
Declaring the field as `Object` or as `CacheLock` behaves identically; the monitor belongs to the object, not to the reference type `[S1]`.

### 3.3 Static lock (replaces `static synchronized`)
```java
private static final Object LOCK = new Object();

public static void register(String s) {
    synchronized (LOCK) { /* ... */ }
}
```

### 3.4 Lock splitting (independent state only)
Use separate private locks only when the guarded states share no invariant. If an operation needs both, acquire them in one fixed global order everywhere `[S1]`.

### 3.5 wait/notify on the private lock
```java
private final Object lock = new Object();
private boolean ready;                       // guarded by lock

public void awaitReady() throws InterruptedException {
    synchronized (lock) {
        while (!ready) lock.wait();          // loop: spurious wake-ups [S3]
    }
}

public void signalReady() {
    synchronized (lock) {
        ready = true;
        lock.notifyAll();
    }
}
```

### 3.6 `ReentrantLock` when you need `tryLock`, timeouts, interruptible acquisition or several conditions
```java
private final ReentrantLock lock = new ReentrantLock();
private final Condition notEmpty = lock.newCondition();

public void take() throws InterruptedException {
    lock.lock();
    try {
        while (queue.isEmpty()) notEmpty.await();
        // ...
    } finally {
        lock.unlock();                       // always in finally [S16]
    }
}
```

### 3.7 Annotate what each lock guards
Prefer `com.google.errorprone.annotations.concurrent.GuardedBy` (Error Prone's recommended annotation; it also recognizes `javax.annotation.concurrent.GuardedBy` and Checker Framework's) `[S17]`. `net.jcip.annotations.GuardedBy` exists but is not in Error Prone's recognized list `[S17][S18]`.
```java
@GuardedBy("lock") private int value;
```

## 4. Singleton and lazy-initialization decision table

| Situation | Use | Notes |
|---|---|---|
| One instance per class loader, no constructor arguments | Holder idiom (4.1) | Thread-safe, lazy, no `volatile` to forget `[S4][S12]` |
| Same, and must resist reflection and serialization duplicates | `enum` (4.2) | Cannot extend a class `[T]` |
| Needs runtime configuration | Explicit init method (4.3) | Fail fast if used before init or initialized twice |
| Lazy value **per instance**, expensive and often unused | `volatile` DCL (4.4), synchronized accessor, or `Suppliers.memoize` | Holder idiom cannot do per-instance `[S12]` |
| One value per key | `ConcurrentHashMap.computeIfAbsent` | Atomic; function runs once per call when key absent `[S19]` |
| Value must be resettable (tests, reload) | Not holder/enum: class init runs once | Use an explicit reference you can replace |
| Testability matters and you control wiring | Create once at the entry point and inject | Avoids global state altogether |

### 4.1 Holder idiom
```java
public abstract class Factory {
    private static final class ContainerHolder {
        static final Container INSTANCE = new Container();
    }

    protected static final Container container() {
        return ContainerHolder.INSTANCE;
    }
}
```
`ContainerHolder` is initialized only when `INSTANCE` is first used `[S4]`. After initialization the JVM may elide the init lock and patch out the init check `[S4]`. There is one `INSTANCE` for the whole hierarchy. Declare the accessor `static final`: a `final` method cannot be hidden by a subclass (compile-time error), whereas a non-final static method can be hidden silently `[S6c][T]`.

If existing code uses `this.container` everywhere, keep the syntax and the uniqueness (note: a subclass field named `container` would hide this one without error `[S6]`):
```java
protected final Container container = ContainerHolder.INSTANCE;   // eager w.r.t. Factory
```

### 4.2 Enum singleton
```java
public enum Registry {
    INSTANCE;
    private final Map<String, String> map = new ConcurrentHashMap<>();
    public String get(String k) { return map.get(k); }
}
```
Guarantees: no instances beyond the constants; `clone` is final in `Enum`; reflective instantiation is prohibited; deserialization never creates duplicates `[S6b]`. Bloch recommends a single-element enum as the best way to implement a singleton `[S20]`.

### 4.3 Configured singleton (explicit initialization)
```java
public abstract class Factory {
    private static final Object LOCK = new Object();
    private static volatile Container container;

    public static void configure(Config cfg) {
        synchronized (LOCK) {
            if (container != null) throw new IllegalStateException("already configured");
            container = new Container(cfg);
        }
    }

    protected static final Container container() {
        Container c = container;
        if (c == null) throw new IllegalStateException("not configured");
        return c;
    }
}
```

### 4.4 Double-checked locking for an instance field
```java
public final class Document {
    private final String text;
    private final Object indexLock = new Object();
    private volatile Index index;                 // volatile is mandatory [S11]

    public Document(String text) { this.text = text; }

    public Index index() {
        Index i = index;                          // single volatile read on the fast path [S13]
        if (i == null) {
            synchronized (indexLock) {
                i = index;
                if (i == null) {
                    i = Index.build(text);
                    index = i;
                }
            }
        }
        return i;
    }
}
```
Use DCL only when the value is expensive, often unused, and accessed concurrently; otherwise initialize in the constructor into a `final` field. Even the first printing of *Effective Java* 3rd ed. shipped a DCL example that could return `null` (fixed in the second printing) `[S21]`; copy this shape exactly.

### 4.5 Lock-free alternative (may compute twice)
```java
private final AtomicReference<Index> index = new AtomicReference<>();

public Index index() {
    Index i = index.get();
    if (i == null) {
        Index fresh = Index.build(text);
        i = index.compareAndSet(null, fresh) ? fresh : index.get();
    }
    return i;
}
```
Several threads may build concurrently and all but one discard their result. Unsuitable if construction opens resources that must be closed.

### 4.6 Library memoizer
Guava `Suppliers.memoize(delegate)`: the returned supplier is thread-safe and calls `delegate.get()` at most once `[S14]`.

## 5. Variants that are correct only under narrow conditions

- **DCL without `volatile`** is safe only if the published object is immutable with all fields `final` `[S11][S22]`. Even then, read the field once into a local: two unsynchronized reads can be reordered and the method can return `null` `[S13]`. Prefer `volatile` anyway; Error Prone still encourages it `[S22]`.
- **Racy single-check on primitives** (no lock, no `volatile`, tolerate repeated computation) works for 32-bit primitives such as a cached hash code, never for `long`/`double`, whose non-volatile reads and writes are not guaranteed atomic `[S11][S2b]`.

## 6. Pitfalls no lock fixes

6.1 **Reentrant construction.** Monitors are reentrant `[S1]`, and a recursive class-initialization request by the same thread "completes normally" `[S4]`. So if a singleton's constructor (directly or indirectly) calls its own accessor:
- holder idiom: the inner call returns **`null`** silently `[S4][T]`;
- DCL: the inner call sees `null`, constructs again, recurses → `StackOverflowError` `[T]`.
Keep singleton constructors trivial; defer work that can call back.

6.2 **Class-initialization deadlock.** Circular dependencies between static initializers (e.g. a superclass initializer referencing a subclass) can deadlock when two threads start initializing them at once. Break the cycle `[S23][S4]`.

6.3 **Class-loader scope.** Statics and `X.class` monitors are per (name, defining loader) `[S5]`: app servers, OSGi and plugin systems can hold several copies.

6.4 **Serialization.** `Object` does not implement `Serializable`; a `final Object lock` field in a serializable class makes serialization fail with `NotSerializableException: java.lang.Object` `[S24][T]`. Use a `transient` lock recreated in `readObject`; this forces dropping `final`, since assigning a `final` field there does not compile `[T]`. Or redesign. A serializable non-enum singleton needs all instance fields `transient` and a `readResolve` method to stay single `[S20]`.

6.5 **`clone()`.** `Object.clone` is a shallow copy `[S24]`: original and clone share the lock object `[T]`. Give the clone its own lock.

6.6 **Inheritance.** A subclass of a class using the private-lock idiom must use its own private lock too; do not widen the lock to `protected` `[S8]`. A subclass field with the same name hides the superclass field without error `[S6]`.

6.7 **Shared mutable singleton state.** Making the singleton's *creation* safe says nothing about its methods. If its methods use `synchronized`/`this`, you have created a global monitor reachable via the accessor (R1). Prefer immutable state with `final` fields `[S2]`, or private locks inside.

6.8 **Virtual threads.** Before JDK 24, a virtual thread blocking inside `synchronized` (or in `Object.wait()`) pinned its carrier. JEP 491, delivered in JDK 24 (GA 18 March 2025), lets virtual threads acquire, hold and release monitors independently of carriers `[S25][S26]`. Pinning remains when native code (native method or FFM API) calls back into Java and blocks `[S25]`. Code already migrated to `ReentrantLock` need not be reverted `[S25]`.

## 7. Tooling that enforces these rules

- **javac** `-Xlint:synchronization` (on by default since JDK 16): warns on synchronizing on value-based classes `[S10][T]`.
- **Error Prone**: `GuardedBy` (ERROR; intraprocedural, does not track aliasing) `[S17]`, `DoubleCheckedLocking` (WARNING) `[S22]`, `ClassInitializationDeadlock` `[S23]`.
- **CodeQL**: `java/unsafe-double-checked-locking` `[S13]`.

## 8. Do not claim (common errors)

- ✗ "`setAccessible(true)` fails on an enum constructor." It succeeds; `Constructor.newInstance` then throws `IllegalArgumentException: Cannot reflectively create enum objects` `[S6b][T]`.
- ✗ "`@GuardedBy` lives in `jakarta.annotation`." Use the packages in §3.7 `[S17]`.
- ✗ "DCL without `volatile` is fine on modern JVMs." Only for immutable objects with `final` fields, read once into a local (§5) `[S11][S13][S22]`.
- ✗ "A holder/enum singleton can be reset for tests." Class initialization runs once per loaded class `[S4]`.
- ✗ "`private Container container` in a base class gives one instance per JVM." It gives one per object (R8).
- ✗ "The JIT always removes the holder-idiom cost." The JLS *permits* eliding the lock and patching out the check after initialization `[S4]`; it does not mandate it.
- ✗ Any claim about which JDK classes lock on `this` internally unless you have read their documentation or source.

## Sources

- [S1] JLS SE 24 §17.1 Synchronization — monitors, reentrancy, what synchronized methods lock, deadlock not prevented. https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html#jls-17.1
- [S2] JLS SE 24 §17.5 `final` Field Semantics. https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html#jls-17.5
- [S2b] JLS SE 24 §17.7 Non-Atomic Treatment of `double` and `long`. https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html#jls-17.7
- [S3] JLS SE 24 §17.2.1 Wait — spurious wake-ups, use `wait` in loops. https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html#jls-17.2.1
- [S4] JLS SE 24 §12.4.1–12.4.2 When Initialization Occurs / Detailed Initialization Procedure. https://docs.oracle.com/javase/specs/jls/se24/html/jls-12.html#jls-12.4.2
- [S5] JVMS SE 21 §5.3 Creation and Loading — class identity = binary name + defining loader. https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-5.html#jvms-5.3
- [S6] JLS SE 21 §8.1.3 (inner classes / enclosing instances), §8.3 (field hiding). https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html
- [S6b] JLS SE 21 §8.9 Enum Classes. https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.9
- [S6c] JLS SE 21 §8.4.3.3 `final` Methods — cannot be overridden or hidden. https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.3.3
- [S7] JLS SE 17 §6.4.1 Shadowing. https://docs.oracle.com/javase/specs/jls/se17/html/jls-6.html#jls-6.4.1
- [S8] SEI CERT Oracle Coding Standard for Java, LCK00-J. https://wiki.sei.cmu.edu/confluence/display/java/LCK00-J.+Use+private+final+lock+objects+to+synchronize+classes+that+may+interact+with+untrusted+code
- [S9] SEI CERT, LCK01-J. https://wiki.sei.cmu.edu/confluence/display/java/LCK01-J.+Do+not+synchronize+on+objects+that+may+be+reused
- [S10] JEP 390: Warnings for Value-Based Classes (JDK 16). https://openjdk.org/jeps/390
- [S11] Bacon, Bloch, Lea, Pugh et al., "The 'Double-Checked Locking is Broken' Declaration". https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
- [S12] Manson & Goetz, JSR-133 (Java Memory Model) FAQ, section on double-checked locking. http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#dcl
- [S13] CodeQL, "Double-checked locking is not thread-safe" (java/unsafe-double-checked-locking). https://codeql.github.com/codeql-query-help/java/java-unsafe-double-checked-locking/
- [S14] Guava `Suppliers.memoize` Javadoc. https://guava.dev/releases/23.0/api/docs/com/google/common/base/Suppliers.html
- [S15] `java.util.Collections.synchronizedList` Javadoc. https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#synchronizedList-java.util.List-
- [S16] `java.util.concurrent.locks.ReentrantLock` Javadoc (Java SE 21). https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/locks/ReentrantLock.html
- [S17] Error Prone, GuardedBy. https://errorprone.info/bugpattern/GuardedBy
- [S18] JCIP annotations, `net.jcip.annotations.GuardedBy`. https://jcip.net/annotations/doc/net/jcip/annotations/GuardedBy.html
- [S19] `ConcurrentHashMap.computeIfAbsent` Javadoc (wording per JDK-8231592). https://bugs.openjdk.org/browse/JDK-8231592
- [S20] J. Bloch, *Effective Java* 2nd ed., Item 3 (excerpt, Dr. Dobb's). https://drdobbs.com/jvm/creating-and-destroying-java-objects-par/208403883?pgno=3
- [S21] jbloch/effective-java-3e-source-code, issue #8 (Item 83 DCL erratum). https://github.com/jbloch/effective-java-3e-source-code/issues/8
- [S22] Error Prone, DoubleCheckedLocking. https://errorprone.info/bugpattern/DoubleCheckedLocking
- [S23] Error Prone, ClassInitializationDeadlock. https://github.com/google/error-prone/blob/master/docs/bugpattern/ClassInitializationDeadlock.md
- [S24] `java.lang.Object` Javadoc (Java SE 21): class declaration; `clone()` shallow copy. https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html
- [S25] JEP 491: Synchronize Virtual Threads without Pinning. https://openjdk.org/jeps/491
- [S26] OpenJDK, JDK 24 project page (GA date, feature list). https://openjdk.org/projects/jdk/24
- [T] Behaviour confirmed by compiling and running on OpenJDK 21.0.12 (javac/java/jcmd): enum reflection, holder/DCL reentrancy, member-type shadowing of an import, value-based sync warning, `NotSerializableException` for an `Object` lock, shared lock after `clone()`, thread-dump lock labels, `enum … extends` rejected, `static final` method cannot be hidden, `final` field not assignable in `readObject`, all code snippets in this file compile with `-Xlint:all` and no warnings.
