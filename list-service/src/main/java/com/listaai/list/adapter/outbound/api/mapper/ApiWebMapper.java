package com.listaai.list.adapter.outbound.api.mapper;

import com.listaai.list.adapter.outbound.api.response.RecipeApiResponse;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingListItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class ApiWebMapper {

    public List<ShoppingListItem> toDomain(RecipeApiResponse response) {
        if (response == null || response.items() == null) {
            return List.of();
        }

        return response.items().stream()
                .map(item -> new ShoppingListItem(
                        null,
                        item.name(),
                        item.quantity(),
                        toItemUnit(item.unit()),
                        item.purchased()
                ))
                .toList();
    }

    private ItemUnit toItemUnit(String unit) {
        return ItemUnit.valueOf(unit.trim().toUpperCase(Locale.ROOT));
    }

}
