package com.kinlhp.moname.commons.jpa.type.descriptor.jdbc.internal;

import java.io.Serial;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import org.hibernate.dialect.Dialect;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.spi.BasicJdbcLiteralFormatter;

/**
 * @see org.hibernate.type.descriptor.jdbc.internal.JdbcLiteralFormatterUUIDData JdbcLiteralFormatterUUIDData
 */
public class JdbcLiteralFormatterUlidData<T> extends BasicJdbcLiteralFormatter<T> {

	@Serial
	private static final long serialVersionUID = 8806048544706434221L;

	public JdbcLiteralFormatterUlidData(@Nonnull final JavaType<T> javaType) {
		super(javaType);
	}

	@Override
	public void appendJdbcLiteral(@Nonnull final SqlAppender appender, @Nonnull final Object value,
			@Nonnull final Dialect dialect, @Nonnull final WrapperOptions wrapperOptions) {
		@Nonnull final Ulid literalValue = unwrap(value, Ulid.class, wrapperOptions);
		appender.appendSql("cast('");
		appender.appendSql(literalValue.toString());
		appender.appendSql("' as ulid)");
	}
}
