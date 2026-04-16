package com.listaai.list.application.port.outbound;

import com.listaai.list.domain.model.ShoppingList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ShoppingListRepositoryPort {

    ShoppingList save(ShoppingList shoppingList);

    Optional<ShoppingList> findById(Long id);
    Page<ShoppingList> findAll(Pageable pageable);

    void deleteById(Long id);

}
