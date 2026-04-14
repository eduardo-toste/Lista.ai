package com.listaai.list.adapter.outbound.persistence.mapper;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListItemEntity;
import com.listaai.list.domain.model.ShoppingListItem;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListItemPersistenceMapper {

    public ShoppingListItemEntity toEntity(ShoppingListItem domain) {
        return ShoppingListItemEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .quantity(domain.getQuantity())
                .unit(domain.getUnit())
                .purchased(domain.isPurchased())
                .build();
    }

    public ShoppingListItem toDomain(ShoppingListItemEntity entity) {
        return new ShoppingListItem(
                entity.getId(),
                entity.getName(),
                entity.getQuantity(),
                entity.getUnit(),
                entity.isPurchased()
        );
    }

}
