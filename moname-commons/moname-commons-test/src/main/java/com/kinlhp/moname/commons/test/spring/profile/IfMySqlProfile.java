package com.kinlhp.moname.commons.test.spring.profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.annotation.Nonnull;

import org.springframework.test.context.junit.jupiter.EnabledIf;

import static com.kinlhp.moname.commons.test.spring.profile.IfMySqlProfile.EXPRESSION;

@EnabledIf(expression = EXPRESSION, loadContext = true)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IfMySqlProfile {

	/**
	 * @see org.springframework.core.env.Profiles#of(String...) of
	 */
	@Nonnull
	String EXPRESSION = "#{environment.acceptsProfiles(T(org.springframework.core.env.Profiles).of('mysql'))}";
}
