package com.listaai.list.adapter.inbound.web.handler;

import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.domain.exception.list.EmptyShoppingListCannotBeSharedException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerWebMvcTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Validator validator = validator();
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController(validator))
                .setControllerAdvice(new GlobalExceptionHandler(new ErrorResponseFactory()))
                .setValidator((org.springframework.validation.Validator) validator)
                .build();
    }

    @Test
    void shouldReturnNotFoundForShoppingListNotFoundException() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Shopping list not found"))
                .andExpect(jsonPath("$.path").value("/test/not-found"));
    }

    @Test
    void shouldReturnConflictForBusinessConflictException() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Empty list can't be shared"))
                .andExpect(jsonPath("$.path").value("/test/conflict"));
    }

    @Test
    void shouldReturnBadRequestForConstraintViolationException() throws Exception {
        mockMvc.perform(get("/test/constraint")
                        .param("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("name: must not be blank"))
                .andExpect(jsonPath("$.path").value("/test/constraint"));
    }

    @Test
    void shouldReturnBadRequestForMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/test/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("name: must not be blank"))
                .andExpect(jsonPath("$.path").value("/test/body"));
    }

    @Test
    void shouldReturnInternalServerErrorForUnexpectedException() throws Exception {
        mockMvc.perform(get("/test/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/test/error"));
    }

    private Validator validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        return validator;
    }

    @RestController
    static class TestController {

        private final Validator validator;

        TestController(Validator validator) {
            this.validator = validator;
        }

        @GetMapping("/test/not-found")
        String notFound() {
            throw new ShoppingListNotFoundException();
        }

        @GetMapping("/test/conflict")
        String conflict() {
            throw new EmptyShoppingListCannotBeSharedException();
        }

        @GetMapping("/test/constraint")
        String constraint(@RequestParam String name) {
            var violations = validator.validate(new QueryPayload(name));
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return "ok";
        }

        @PostMapping("/test/body")
        String body(@Valid @RequestBody BodyPayload payload) {
            return payload.name();
        }

        @GetMapping("/test/error")
        String error() {
            throw new IllegalStateException("boom");
        }
    }

    private record QueryPayload(@NotBlank String name) {
    }

    private record BodyPayload(@NotBlank String name) {
    }
}
