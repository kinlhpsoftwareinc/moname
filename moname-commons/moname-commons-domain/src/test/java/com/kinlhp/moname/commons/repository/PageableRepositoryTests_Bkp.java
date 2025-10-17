//package com.kinlhp.moname.commons.repository;
//
//import java.io.Serial;
//import java.io.Serializable;
//import java.util.List;
//import java.util.stream.IntStream;
//
//import jakarta.annotation.Nonnull;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.domain.PageImpl;
//
//import com.kinlhp.moname.commons.domain.entity.Identifiable;
//import com.kinlhp.moname.commons.domain.entity.Immutable;
//
//final class PageableRepositoryTests {
//
//	@Test
//	void one() {
//		@Nonnull final var content = IntStream.range(0, 10).mapToObj(Immutable::new).toList();
//		@Nonnull final var repository = new PageableRepositoryImpl<>(content);
//
//		Assertions.assertAll(
//			() -> Assertions.assertNotNull(repository.one(0)),
//			() -> Assertions.assertTrue(repository.one(0).isPresent())
//		);
//	}
//
//	private static final class PageableRepositoryImpl<T extends Identifiable<ID>, ID extends Serializable> extends PageImpl<T> implements PageableRepository<T, ID> {
//
//		@Serial
//		private static final long serialVersionUID = 2190782318397043652L;
//
//		public PageableRepositoryImpl(@Nonnull final List<T> content) {
//			super(content);
//		}
//	}
//}
