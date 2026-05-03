package com.listaai.list.adapter.inbound.web.handler;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseFactoryTest {

    private final ErrorResponseFactory errorResponseFactory = new ErrorResponseFactory();

    @Test
    void shouldBuildErrorResponseWithRequestInformation() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/lists/1/share");

        ErrorResponse response = errorResponseFactory.build(HttpStatus.CONFLICT, "invalid state", request);

        assertThat(response.status()).isEqualTo(409);
        assertThat(response.error()).isEqualTo("Conflict");
        assertThat(response.message()).isEqualTo("invalid state");
        assertThat(response.path()).isEqualTo("/lists/1/share");
        assertThat(response.timestamp()).isNotNull();
    }
}
