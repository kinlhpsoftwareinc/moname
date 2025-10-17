package com.kinlhp.moname.commons.annotation.spring.condition;

import org.jspecify.annotations.Nullable;
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

	DoNotTreatAsNestedTypeIT(final @Autowired @Qualifier("doNotTreatAsNestedType") Object bean) {
		this.bean = bean;
	}

	@Override
	@Nullable Object getBean() {
		return bean;
	}

	@Override
	void doAssert(final @Nullable Object bean) {
		Assertions.assertNotNull(bean);
	}

	@Override
	void doAssert(final CapturedOutput output) {
		org.assertj.core.api.Assertions.assertThat(output.getOut()).contains("""
				Condition OnMissingPropertyCondition on \
				com.kinlhp.moname.commons.annotation.spring.condition.DoNotTreatAsNestedTypeIT$TestConfiguration#bean \
				matched due to @ConditionalOnMissingProperty (container) matched""");
	}

	@Configuration(proxyBeanMethods = false)
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
