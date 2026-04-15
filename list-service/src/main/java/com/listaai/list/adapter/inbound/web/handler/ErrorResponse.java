package com.listaai.list.adapter.inbound.web.handler;

import java.time.LocalDateTime;

public record ErrorResponse(

        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path

) {
}
