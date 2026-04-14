package com.listaai.list.adapter.outbound.persistence.adapter;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListEntity;
import com.listaai.list.adapter.outbound.persistence.mapper.ShoppingListMapper;
import com.listaai.list.adapter.outbound.persistence.repository.ShoppingListJpaRepository;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.model.ShoppingList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingListPersistenceAdapter implements ShoppingListRepositoryPort {

    private final ShoppingListMapper shoppingListMapper;
    private final ShoppingListJpaRepository shoppingListJpaRepository;

    @Override
    public ShoppingList save(ShoppingList shoppingList) {
        ShoppingListEntity entity = shoppingListMapper.toEntity(shoppingList);
        ShoppingListEntity savedShoppingList = shoppingListJpaRepository.save(entity);
        return shoppingListMapper.toDomain(savedShoppingList);
    }

}
