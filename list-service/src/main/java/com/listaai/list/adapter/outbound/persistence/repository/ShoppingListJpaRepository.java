package com.listaai.list.adapter.outbound.persistence.repository;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListJpaRepository extends JpaRepository<ShoppingListEntity, Long> {
}
