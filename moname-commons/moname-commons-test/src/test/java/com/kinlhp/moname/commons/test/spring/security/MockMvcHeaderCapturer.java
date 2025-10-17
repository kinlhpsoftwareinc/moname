package com.kinlhp.moname.commons.test.spring.security;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

final class MockMvcHeaderCapturer {

	private MockMvcHeaderCapturer() {
	}

	@Nullable
	static String headerOf(@Nonnull final MockMvc mockMvc, @Nonnull final String headerName) {
		@Nonnull final var header = new AtomicReference<String>();
		@Nonnull final var captured = new AtomicBoolean(false);
		try {
			mockMvc.perform(get("/").with(captureHeader(headerName, header, captured))).andDo(print());
		} catch (@Nonnull final Exception exception) {
			if (!captured.get()) {
				throw new IllegalStateException("The header %s could not be captured".formatted(headerName), exception);
			}
			// Request processing after the request is built is not under test
		}
		return header.get();
	}

	@Nonnull
	private static RequestPostProcessor captureHeader(@Nonnull final String headerName,
			@Nonnull final AtomicReference<String> headerReference, @Nonnull final AtomicBoolean captureMarker) {
		return request -> {
			headerReference.set(request.getHeader(headerName));
			captureMarker.set(true);
			return request;
		};
	}
}
