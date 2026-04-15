package com.listaai.list.adapter.outbound.persistence.repository;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingListJpaRepository extends JpaRepository<ShoppingListEntity, Long> {

    @EntityGraph(attributePaths = {"items", "participants"})
    Optional<ShoppingListEntity> findById(Long id);

    @EntityGraph(attributePaths = {"items", "participants"})
    Page<ShoppingListEntity> findAll(Pageable pageable);

}
