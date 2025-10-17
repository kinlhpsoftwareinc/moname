package com.kinlhp.moname.commons.test.spring.context;

import org.springframework.test.context.junit.jupiter.EnabledIf;

import jakarta.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.kinlhp.moname.commons.test.spring.context.IfSecurityProfile.EXPRESSION;

@EnabledIf(expression = EXPRESSION, loadContext = true)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IfSecurityProfile {

	/**
	 * @see org.springframework.core.env.Profiles#of(String...) of
	 */
	@Nonnull
	String EXPRESSION = "#{environment.acceptsProfiles(T(org.springframework.core.env.Profiles).of('security'))}";
}
