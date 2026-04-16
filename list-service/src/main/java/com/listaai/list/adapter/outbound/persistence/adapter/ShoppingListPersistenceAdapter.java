package com.listaai.list.adapter.outbound.persistence.adapter;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListEntity;
import com.listaai.list.adapter.outbound.persistence.mapper.ShoppingListPersistenceMapper;
import com.listaai.list.adapter.outbound.persistence.repository.ShoppingListJpaRepository;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.model.ShoppingList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ShoppingListPersistenceAdapter implements ShoppingListRepositoryPort {

    private final ShoppingListPersistenceMapper shoppingListMapper;
    private final ShoppingListJpaRepository shoppingListJpaRepository;

    @Override
    public ShoppingList save(ShoppingList shoppingList) {
        ShoppingListEntity entity = shoppingListMapper.toEntity(shoppingList);
        ShoppingListEntity savedShoppingList = shoppingListJpaRepository.save(entity);
        return shoppingListMapper.toDomain(savedShoppingList);
    }

    @Override
    public Optional<ShoppingList> findById(Long id) {
        return shoppingListJpaRepository.findById(id)
                .map(shoppingListMapper::toDomain);
    }

    @Override
    public Page<ShoppingList> findAll(Pageable pageable) {
        Page<ShoppingListEntity> entityPage = shoppingListJpaRepository.findAll(pageable);

        return shoppingListMapper.toPageDomain(entityPage);
    }

    @Override
    public void deleteById(Long id) {
        shoppingListJpaRepository.deleteById(id);
    }

}
