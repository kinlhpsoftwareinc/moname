//package com.kinlhp.moname.commons.test.testcontainers;
//
//import org.slf4j.Logger;
//import org.testcontainers.containers.JdbcDatabaseContainer;
//import org.testcontainers.jdbc.ContainerDatabaseDriver;
//
//import jakarta.annotation.Nonnull;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Set;
//import java.util.regex.MatchResult;
//import java.util.regex.Pattern;
//
//import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;
//
//// TODO: Tests to be implemented
//@SuppressWarnings("java:S119")
//public interface UrlExtractableJdbcDatabaseContainer<SELF extends JdbcDatabaseContainer<SELF>>
//		extends UriExtractableContainer {
//
//	@Nonnull
//	Logger getLogger();
//
//	@Nonnull
//	String getSubprotocol();
//
//	@Nonnull
//	default SELF withDatabaseName() {
//		//noinspection unchecked
//		return getJdbcUrl()
//				.map(this::extractDatabaseName)
//				.filter(Optional::isPresent)
//				.map(Optional::get)
//				.map(((SELF) this)::withDatabaseName)
//				.orElseGet(() -> ((SELF) this).withDatabaseName("%s_test".formatted(MONAME)));
//	}
//
//	@Nonnull
//	private Optional<String> getJdbcUrl() {
//		@Nonnull final var subprotocol = "jdbc:tc:%s:".formatted(getSubprotocol());
//		getLogger().trace("""
//						Attempting to find the JDBC URL for subprotocol "{}:" in ContainerDatabaseDriver's JDBC URL \
//						container cache""",
//				subprotocol);
//		return reflectOverContainerDatabaseDriver()
//				.map(Map::keySet)
//				.stream()
//				.flatMap(Set::stream)
//				.filter(jdbcUrl -> jdbcUrl.startsWith(subprotocol))
//				.findFirst();
//	}
//
//	@Nonnull
//	@SuppressWarnings("java:S3011")
//	private <T extends JdbcDatabaseContainer<T>> Optional<Map<String, T>> reflectOverContainerDatabaseDriver() {
//		return Optional.of(ContainerDatabaseDriver.class)
//				.map(driverClass -> {
//					try {
//						@Nonnull final var field = driverClass.getDeclaredField("jdbcUrlContainerCache");
//						field.setAccessible(true);
//						//noinspection unchecked
//						return (Map<String, T>) field.get(null);
//					} catch (@Nonnull final ReflectiveOperationException exception) {
//						getLogger().warn("Failed to access private field jdbcUrlContainerCache by reflection in {}",
//								driverClass.getName());
//						return null;
//					}
//				});
//	}
//
//	@Nonnull
//	private Optional<String> extractDatabaseName(@Nonnull final String jdbcUrl) {
//		getLogger().trace("Extracting database name from JDBC URL {}", jdbcUrl);
//		@Nonnull @SuppressWarnings("RegExpRedundantEscape") final Pattern databaseNamePattern =
//				Pattern.compile("(?<=\\/)[^\\/\\?]+(?=\\?|$)");
//		return databaseNamePattern.matcher(jdbcUrl)
//				.results()
//				.map(MatchResult::group)
//				.findFirst();
//	}
//}
