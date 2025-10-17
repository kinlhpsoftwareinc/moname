package com.kinlhp.moname.commons.annotation.spring.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionMessage.Style;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotationPredicates;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link Condition Condition} that checks if properties are defined in environment.
 *
 * @see ConditionalOnMissingProperty @ConditionalOnMissingProperty
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 40)
class OnMissingPropertyCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
		final var noMatch = new ArrayList<ConditionMessage>();
		final var match = new ArrayList<ConditionMessage>();
		metadata.getAnnotations()
				.stream(ConditionalOnMissingProperty.class.getName())
				.filter(MergedAnnotationPredicates.unique(MergedAnnotation::getMetaTypes))
				.map(MergedAnnotation::asAnnotationAttributes)
				.map(attributes -> determineOutcome(attributes, context.getEnvironment()))
				.forEach(outcome -> (outcome.isMatch() ? match : noMatch).add(outcome.getConditionMessage()));
		return !noMatch.isEmpty()
				? ConditionOutcome.noMatch(ConditionMessage.of(noMatch))
				: ConditionOutcome.match(ConditionMessage.of(match));
	}

	private ConditionOutcome determineOutcome(final AnnotationAttributes attributes, final PropertyResolver resolver) {
		final var spec = new Spec(attributes);
		final var presentProperties = new ArrayList<String>();
		spec.collectProperties(resolver, presentProperties);
		return !presentProperties.isEmpty()
				? ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnMissingProperty.class, spec)
				.found("property", "properties").items(Style.QUOTE, presentProperties))
				: ConditionOutcome.match(ConditionMessage.forCondition(ConditionalOnMissingProperty.class, spec)
				.because("matched"));
	}

	private static class Spec {

		private final String prefix;

		private final String[] names;

		private final boolean alsoTreatNestedType;

		private final boolean matchIfPresent;

		Spec(final AnnotationAttributes attributes) {
			prefix = getPrefix(attributes);
			names = getNames(attributes);
			alsoTreatNestedType = attributes.getBoolean("alsoTreatNestedType");
			matchIfPresent = attributes.getBoolean("matchIfPresent");
		}

		private String getPrefix(final AnnotationAttributes attributes) {
			final @SuppressWarnings("java:S1117") var prefix = attributes.getString("prefix").trim();
			return (StringUtils.hasText(prefix) && !prefix.endsWith(".")) ? prefix + "." : prefix;
		}

		private String[] getNames(final Map<String, Object> attributes) {
			final var value = (String[]) attributes.get("value");
			final var name = (String[]) attributes.get("name");
			Assert.state(value.length > 0 || name.length > 0,
					"The name or value attribute of @ConditionalOnMissingProperty must be specified");
			Assert.state(value.length == 0 || name.length == 0,
					"The name and value attributes of @ConditionalOnMissingProperty are exclusive");
			return (value.length > 0) ? value : name;
		}

		private void collectProperties(final PropertyResolver resolver, final List<String> present) {
			for (final String name : names) {
				final String key = prefix + name;
				final var contains = (resolver instanceof final ConfigurableEnvironment environment)
						? resolveProperty(environment, key)
						: resolveSingleValuedProperty(resolver, key);
				if (contains.getKey() && !matchIfPresent) {
					present.add("%s%s".formatted(name, contains.getValue()));
				}
			}
		}

		/**
		 * By default, a property is considered specified if their computed full key was specified
		 * ({@link ConditionalOnMissingProperty#prefix prefix} and {@link ConditionalOnMissingProperty#name name}). In
		 * other words, it isn't works for nested value structure.
		 * <p>
		 * So if the property wasn't resolved in the default way, it will be attempted to resolve it applying the
		 * {@link ConditionalOnMissingProperty#alsoTreatNestedType alsoTreatNestedType} attribute.
		 */
		private Entry<Boolean, String> resolveProperty(final ConfigurableEnvironment environment, final String key) {
			final var contains = resolveSingleValuedProperty(environment, key);
			return contains.getKey()
					? contains
					: resolveNestedValuedProperty(environment.getPropertySources(), key);
		}

		private Entry<Boolean, String> resolveSingleValuedProperty(final PropertyResolver resolver, final String key) {
			return Map.entry(resolver.containsProperty(key), "");
		}

		private Entry<Boolean, String> resolveNestedValuedProperty(final MutablePropertySources propertySources,
				final String key) {
			final var additionalInfo = """
					 (that was treated as nested value structure since annotation's element alsoTreatNestedType was set to \
					true)""";
			return alsoTreatNestedType
					? propertySources.stream()
					.filter(MapPropertySource.class::isInstance)
					.map(MapPropertySource.class::cast)
					.map(MapPropertySource::getPropertyNames)
					.flatMap(Arrays::stream)
					.filter(propertyName -> propertyName.startsWith(key))
					.findAny()
					.map(propertyName -> Map.entry(true, additionalInfo))
					.orElse(Map.entry(false, ""))
					: Map.entry(false, "");
		}

		@Override
		public String toString() {
			final var result = new StringBuilder();
			result.append("(");
			result.append(prefix);
			if (names.length == 1) {
				result.append(names[0]);
			} else {
				result.append("[");
				result.append(StringUtils.arrayToCommaDelimitedString(names));
				result.append("]");
			}
			result.append(")");
			return result.toString();
		}
	}
}
