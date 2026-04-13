package com.listaai.list.adapter.outbound.persistence.repository;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListItemJpaRepository extends JpaRepository<ShoppingListItemEntity, Long> {
}
