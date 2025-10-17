package com.kinlhp.moname.commons.annotation.spring.condition;

import org.jspecify.annotations.Nullable;
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

	AlsoTreatNestedTypeIT(final @Autowired(required = false) @Qualifier("alsoTreatNestedType") Object bean) {
		this.bean = bean;
	}

	@Override
	@Nullable Object getBean() {
		return bean;
	}

	@Override
	void doAssert(final @Nullable Object bean) {
		Assertions.assertNull(bean);
	}

	@Override
	void doAssert(final CapturedOutput output) {
		org.assertj.core.api.Assertions.assertThat(output.getOut()).contains("""
				Condition OnMissingPropertyCondition on \
				com.kinlhp.moname.commons.annotation.spring.condition.AlsoTreatNestedTypeIT$TestConfiguration#bean did \
				not match due to @ConditionalOnMissingProperty (container) found property 'container (that was treated \
				as nested value structure since annotation's element alsoTreatNestedType was set to true)'""");
	}

	@Configuration(proxyBeanMethods = false)
	static class TestConfiguration {

		@Bean(name = "alsoTreatNestedType")
		@ConditionalOnMissingProperty(alsoTreatNestedType = true, name = "container")
		@SuppressWarnings("unused")
		Object bean() {
			return """
					Condition OnMissingPropertyCondition on \
					com.kinlhp.moname.commons.annotation.spring.condition.AlsoTreatNestedTypeIT$TestConfiguration#bean \
					did not match due to @ConditionalOnMissingProperty (container) found property 'container (that was \
					treated as nested value structure since annotation's element alsoTreatNestedType was set to true)'\
					""";
		}
	}
}
