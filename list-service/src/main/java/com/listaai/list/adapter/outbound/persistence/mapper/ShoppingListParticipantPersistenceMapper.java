package com.listaai.list.adapter.outbound.persistence.mapper;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListParticipantEntity;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListParticipantPersistenceMapper {

    public ShoppingListParticipantEntity toEntity(ShoppingListParticipant domain) {
        return ShoppingListParticipantEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .phoneNumber(domain.getPhoneNumber())
                .build();
    }

    public ShoppingListParticipant toDomain(ShoppingListParticipantEntity entity) {
        return new ShoppingListParticipant(
                entity.getId(),
                entity.getName(),
                entity.getPhoneNumber()
        );
    }

}
