package com.listaai.list.adapter.outbound.persistence.repository;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListParticipantJpaRepository extends JpaRepository<ShoppingListParticipantEntity, Long> {
}
