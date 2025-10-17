package com.kinlhp.moname.commons.annotation.spring.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;

import jakarta.annotation.Nonnull;

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

	@Nonnull
	@Override
	public ConditionOutcome getMatchOutcome(@Nonnull final ConditionContext context,
			@Nonnull final AnnotatedTypeMetadata metadata) {
		@Nonnull final var noMatch = new ArrayList<ConditionMessage>();
		@Nonnull final var match = new ArrayList<ConditionMessage>();
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

	@Nonnull
	private ConditionOutcome determineOutcome(@Nonnull final AnnotationAttributes attributes,
			@Nonnull final PropertyResolver resolver) {
		@Nonnull final var spec = new Spec(attributes);
		@Nonnull final var presentProperties = new ArrayList<String>();
		spec.collectProperties(resolver, presentProperties);
		return !presentProperties.isEmpty()
				? ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnMissingProperty.class, spec)
				.found("property", "properties").items(Style.QUOTE, presentProperties))
				: ConditionOutcome.match(ConditionMessage.forCondition(ConditionalOnMissingProperty.class, spec)
				.because("matched"));
	}

	private static class Spec {

		@Nonnull
		private final String prefix;

		@Nonnull
		private final String[] names;

		private final boolean alsoTreatNestedType;

		private final boolean matchIfPresent;

		Spec(@Nonnull final AnnotationAttributes attributes) {
			prefix = getPrefix(attributes);
			names = getNames(attributes);
			alsoTreatNestedType = attributes.getBoolean("alsoTreatNestedType");
			matchIfPresent = attributes.getBoolean("matchIfPresent");
		}

		@Nonnull
		private String getPrefix(@Nonnull final AnnotationAttributes attributes) {
			@Nonnull @SuppressWarnings("java:S1117") final var prefix = attributes.getString("prefix").trim();
			return (StringUtils.hasText(prefix) && !prefix.endsWith(".")) ? prefix + "." : prefix;
		}

		@Nonnull
		private String[] getNames(@Nonnull final Map<String, Object> attributes) {
			@Nonnull final var value = (String[]) attributes.get("value");
			@Nonnull final var name = (String[]) attributes.get("name");
			Assert.state(value.length > 0 || name.length > 0,
					"The name or value attribute of @ConditionalOnMissingProperty must be specified");
			Assert.state(value.length == 0 || name.length == 0,
					"The name and value attributes of @ConditionalOnMissingProperty are exclusive");
			return (value.length > 0) ? value : name;
		}

		private void collectProperties(@Nonnull final PropertyResolver resolver, @Nonnull final List<String> present) {
			for (@Nonnull final String name : names) {
				@Nonnull final String key = prefix + name;
				final var contains = (resolver instanceof @Nonnull final ConfigurableEnvironment environment)
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
		private Entry<Boolean, String> resolveProperty(@Nonnull final ConfigurableEnvironment environment,
				@Nonnull final String key) {
			@Nonnull final var contains = resolveSingleValuedProperty(environment, key);
			return contains.getKey()
					? contains
					: resolveNestedValuedProperty(environment.getPropertySources(), key);
		}

		private Entry<Boolean, String> resolveSingleValuedProperty(@Nonnull final PropertyResolver resolver,
				@Nonnull final String key) {
			return Map.entry(resolver.containsProperty(key), "");
		}

		private Entry<Boolean, String> resolveNestedValuedProperty(@Nonnull final MutablePropertySources propertySources,
				@Nonnull final String key) {
			@Nonnull final var additionalInfo = """
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

		@Nonnull
		@Override
		public String toString() {
			@Nonnull final var result = new StringBuilder();
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
