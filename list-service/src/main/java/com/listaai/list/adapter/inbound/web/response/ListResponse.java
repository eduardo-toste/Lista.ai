package com.listaai.list.adapter.inbound.web.response;

import java.util.List;

public record ListResponse(

        Long id,
        String name,
        List<ListItemResponse> items,
        List<ListParticipantResponse> participants

) {
}
