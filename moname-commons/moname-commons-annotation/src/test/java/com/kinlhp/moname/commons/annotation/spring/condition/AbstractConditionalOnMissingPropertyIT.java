package com.kinlhp.moname.commons.annotation.spring.condition;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
@TestPropertySource(properties = "container.nested-content = value")
abstract class AbstractConditionalOnMissingPropertyIT {

	@Nullable
	abstract Object getBean();

	abstract void doAssert(@Nullable final Object bean);

	abstract void doAssert(@Nonnull final CapturedOutput output);

	@SuppressWarnings("java:S2699")
	@Test
	final void assertCondition(@Nonnull final CapturedOutput output) {
		doAssert(getBean());
		doAssert(output);
	}
}
