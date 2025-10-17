package com.kinlhp.moname.commons.annotation.spring.condition;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kinlhp.moname.commons.annotation.spring.condition.DoNotTreatAsNestedTypeIT.TestConfiguration;

@SpringBootTest(classes = TestConfiguration.class)
final class DoNotTreatAsNestedTypeIT extends AbstractConditionalOnMissingPropertyIT {

	private final Object bean;

	DoNotTreatAsNestedTypeIT(@Autowired @Qualifier("doNotTreatAsNestedType") final Object bean) {
		this.bean = bean;
	}

	@Nullable
	@Override
	Object getBean() {
		return bean;
	}

	@Override
	void doAssert(@Nullable final Object bean) {
		Assertions.assertNotNull(bean);
	}

	@Override
	void doAssert(@Nonnull final CapturedOutput output) {
		org.assertj.core.api.Assertions.assertThat(output.getOut()).contains("""
				Condition OnMissingPropertyCondition on \
				com.kinlhp.moname.commons.annotation.spring.condition.DoNotTreatAsNestedTypeIT$TestConfiguration#bean \
				matched due to @ConditionalOnMissingProperty (container) matched""");
	}

	@Configuration
	static class TestConfiguration {

		@Bean(name = "doNotTreatAsNestedType")
		@ConditionalOnMissingProperty(name = "container")
		@SuppressWarnings("unused")
		Object bean() {
			return """
					Condition OnMissingPropertyCondition on \
					com.kinlhp.moname.commons.annotation.spring.condition.DoNotTreatAsNestedTypeIT$TestConfiguration#bean \
					matched due to @ConditionalOnMissingProperty (container) matched""";
		}
	}
}
