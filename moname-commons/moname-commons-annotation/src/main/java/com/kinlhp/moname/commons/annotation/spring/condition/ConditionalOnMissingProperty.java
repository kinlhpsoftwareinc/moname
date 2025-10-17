package com.kinlhp.moname.commons.annotation.spring.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.annotation.Nonnull;

import org.springframework.context.annotation.Conditional;

/**
 * If the property is contained in the {@link org.springframework.core.env.Environment Environment} at all, the
 * {@link #matchIfPresent matchIfPresent} attribute is consulted. By default, present attributes do not match.
 *
 * @see org.springframework.boot.autoconfigure.condition.ConditionalOnProperty @ConditionalOnProperty
 */
@Conditional(OnMissingPropertyCondition.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ConditionalOnMissingProperty {

	/**
	 * Alias for {@link #name() name}.
	 *
	 * @return the names
	 */
	@Nonnull
	String[] value() default {};

	/**
	 * A prefix that should be applied to each property. The prefix automatically ends with a dot if not specified. A
	 * valid prefix is defined by one or more words separated with dots (e.g. {@code "acme.system.feature"}).
	 *
	 * @return the prefix
	 */
	@Nonnull
	String prefix() default "";

	/**
	 * The name of the properties to test. If a prefix has been defined, it is applied to compute the full key of each
	 * property. For instance if the prefix is {@code app.config} and one value is {@code my-value}, the full key would
	 * be {@code app.config.my-value}
	 * <p>
	 * Use the dashed notation to specify each property, that is all lower case with a "-" to separate words
	 * (e.g. {@code my-long-property}).
	 * <p>
	 * If multiple names are specified, all the properties have to pass the test for the condition to match.
	 *
	 * @return the names
	 */
	@Nonnull
	String[] name() default {};

	/**
	 * By default, a property is considered specified if their computed full key was specified ({@link #prefix prefix}
	 * and {@link #name name}). In other words, it isn't works for nested value structure.
	 * <p>
	 * That said, this annotation's element specifies whether the property should be treated as a nested value
	 * structure. Defaults to {@code false}.
	 * <p>
	 * The table below shows when a condition matches according to the property value structure:
	 * <table border="1">
	 * <caption>Treat as nested type</caption>
	 * <tr>
	 * <th>Property definition</th>
	 * <th>Property Name</th>
	 * <th>{@code alsoTreatNestedType="true"}</th>
	 * <th>{@code alsoTreatNestedType="false"}</th>
	 * </tr>
	 * <tr>
	 * <td rowspan="2" style="white-space:nowrap">
	 * <pre>{@code
	 * container:
	 *   nested-content: value
	 * }</pre>
	 * </td>
	 * <td>{@code name="container"}</td>
	 * <td color="green">yes</td>
	 * <td color="red">no</td>
	 * </tr>
	 * <tr>
	 * <td>{@code name="container.nested-content"}</td>
	 * <td color="green">yes</td>
	 * <td color="green">yes</td>
	 * </tr>
	 * </table>
	 * <p>
	 *
	 * @return if the property should be treated as a nested value structure
	 */
	boolean alsoTreatNestedType() default false;

	/**
	 * Specify if the condition should match if the property is not set. Defaults to {@code false}.
	 *
	 * @return if the condition should match if the property is present
	 */
	boolean matchIfPresent() default false;
}
