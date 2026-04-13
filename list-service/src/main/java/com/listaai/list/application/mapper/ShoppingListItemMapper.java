package com.listaai.list.application.mapper;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.domain.model.ShoppingListItem;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListItemMapper {

    public ShoppingListItem toDomain(ShoppingListItemCommand command) {
        return new ShoppingListItem(
               null,
               command.name(),
               command.quantity(),
               command.unit()
        );
    }

    public ShoppingListItemOutput toOutput(ShoppingListItem domain) {
        return new ShoppingListItemOutput(
                domain.getId(),
                domain.getName(),
                domain.getQuantity(),
                domain.getUnit(),
                domain.isPurchased()
        );
    }

}
