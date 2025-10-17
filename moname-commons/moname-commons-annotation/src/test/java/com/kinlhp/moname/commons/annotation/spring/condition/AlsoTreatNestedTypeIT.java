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

import com.kinlhp.moname.commons.annotation.spring.condition.AlsoTreatNestedTypeIT.TestConfiguration;

@SpringBootTest(classes = TestConfiguration.class)
final class AlsoTreatNestedTypeIT extends AbstractConditionalOnMissingPropertyIT {

	private final Object bean;

	AlsoTreatNestedTypeIT(@Autowired(required = false) @Qualifier("alsoTreatNestedType") final Object bean) {
		this.bean = bean;
	}

	@Nullable
	@Override
	Object getBean() {
		return bean;
	}

	@Override
	void doAssert(@Nullable final Object bean) {
		Assertions.assertNull(bean);
	}

	@Override
	void doAssert(@Nonnull final CapturedOutput output) {
		org.assertj.core.api.Assertions.assertThat(output.getOut()).contains("""
				Condition OnMissingPropertyCondition on \
				com.kinlhp.moname.commons.annotation.spring.condition.AlsoTreatNestedTypeIT$TestConfiguration#bean did \
				not match due to @ConditionalOnMissingProperty (container) found property 'container (that was treated \
				as nested value structure since annotation's element alsoTreatNestedType was set to true)'""");
	}

	@Configuration
	static class TestConfiguration {

		@Bean(name = "alsoTreatNestedType")
		@ConditionalOnMissingProperty(alsoTreatNestedType = true, name = "container")
		@SuppressWarnings("unused")
		Object bean() {
			return """
					Condition OnMissingPropertyCondition on \
					com.kinlhp.moname.commons.annotation.spring.condition.AlsoTreatNestedTypeIT$TestConfiguration#bean \
					did not match due to @ConditionalOnMissingProperty (container) found property 'container (that was \
					treated as nested value structure since annotation's element alsoTreatNestedType was set to true)'
					""";
		}
	}
}
