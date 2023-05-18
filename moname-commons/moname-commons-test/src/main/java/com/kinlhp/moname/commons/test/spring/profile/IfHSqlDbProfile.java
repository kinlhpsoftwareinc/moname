package com.kinlhp.moname.commons.test.spring.profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.annotation.Nonnull;

import org.springframework.test.context.junit.jupiter.EnabledIf;

@EnabledIf(expression = IfHSqlDbProfile.EXPRESSION)
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface IfHSqlDbProfile {

	@Nonnull
	String EXPRESSION = "#{environment.acceptsProfiles(T(org.springframework.core.env.Profiles).of('hsqldb'))}";
}
