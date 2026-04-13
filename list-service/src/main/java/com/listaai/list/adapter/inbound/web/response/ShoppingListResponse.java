package com.listaai.list.adapter.inbound.web.response;

import java.util.List;

public record ShoppingListResponse(

        Long id,
        String name,
        List<ShoppingListItemResponse> items,
        List<ShoppingListParticipantResponse> participants

) {
}
