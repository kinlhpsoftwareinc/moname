package com.kinlhp.moname.commons.test.spring.security;

import org.jspecify.annotations.Nullable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

final class MockMvcHeaderCapturer {

	private MockMvcHeaderCapturer() {
	}

	static @Nullable String headerOf(final MockMvc mockMvc, final String headerName) {
		final var header = new AtomicReference<@Nullable String>();
		final var captured = new AtomicBoolean(false);
		try {
			mockMvc.perform(get("/").with(captureHeader(headerName, header, captured))).andDo(print());
		} catch (final Exception exception) {
			if (!captured.get()) {
				throw new IllegalStateException("The header %s could not be captured".formatted(headerName), exception);
			}
			// Request processing after the request is built is not under test
		}
		return header.get();
	}

	private static RequestPostProcessor captureHeader(final String headerName,
			final AtomicReference<String> headerReference, final AtomicBoolean captureMarker) {
		return request -> {
			headerReference.set(request.getHeader(headerName));
			captureMarker.set(true);
			return request;
		};
	}
}
