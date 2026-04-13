package com.listaai.list.adapter.inbound.web.response;

import com.listaai.list.domain.enums.ItemUnit;

public record ListItemResponse(

        Long id,
        String name,
        int quantity,
        ItemUnit unit,
        boolean purchased

) {
}
