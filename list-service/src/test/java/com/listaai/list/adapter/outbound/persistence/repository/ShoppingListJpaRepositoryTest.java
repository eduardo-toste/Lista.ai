package com.listaai.list.adapter.outbound.persistence.repository;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListEntity;
import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListItemEntity;
import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListParticipantEntity;
import com.listaai.list.domain.enums.ItemUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ShoppingListJpaRepositoryTest {

    @Autowired
    private ShoppingListJpaRepository shoppingListJpaRepository;

    @Autowired
    private ShoppingListItemJpaRepository shoppingListItemJpaRepository;

    @Autowired
    private ShoppingListParticipantJpaRepository shoppingListParticipantJpaRepository;

    @Test
    void shouldSaveShoppingListWithItemsAndParticipants() {
        ShoppingListEntity shoppingList = newShoppingListEntity("Churrasco", "Carvao", 2, "Eduardo", "11999999999");

        ShoppingListEntity saved = shoppingListJpaRepository.saveAndFlush(shoppingList);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getItems()).hasSize(1);
        assertThat(saved.getParticipants()).hasSize(1);
        assertThat(shoppingListItemJpaRepository.count()).isEqualTo(1);
        assertThat(shoppingListParticipantJpaRepository.count()).isEqualTo(1);

        ShoppingListItemEntity savedItem = saved.getItems().iterator().next();
        ShoppingListParticipantEntity savedParticipant = saved.getParticipants().iterator().next();
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getShoppingList().getId()).isEqualTo(saved.getId());
        assertThat(savedParticipant.getId()).isNotNull();
    }

    @Test
    void shouldFindShoppingListByIdWithItemsAndParticipants() {
        ShoppingListEntity saved = shoppingListJpaRepository.saveAndFlush(
                newShoppingListEntity("Mercado", "Arroz", 3, "Maria", "11988887777")
        );

        var found = shoppingListJpaRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mercado");
        assertThat(found.get().getItems()).hasSize(1);
        assertThat(found.get().getParticipants()).hasSize(1);
        assertThat(found.get().getItems().iterator().next().getName()).isEqualTo("Arroz");
        assertThat(found.get().getParticipants().iterator().next().getName()).isEqualTo("Maria");
    }

    @Test
    void shouldReturnPagedShoppingLists() {
        shoppingListJpaRepository.saveAndFlush(newShoppingListEntity("Lista A", "Carvao", 2, "Eduardo", "11999999999"));
        shoppingListJpaRepository.saveAndFlush(newShoppingListEntity("Lista B", "Arroz", 1, "Maria", "11988887777"));

        Pageable pageable = PageRequest.of(0, 1);
        Page<ShoppingListEntity> page = shoppingListJpaRepository.findAll(pageable);

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(1);
        assertThat(page.getContent().getFirst().getItems()).isNotEmpty();
        assertThat(page.getContent().getFirst().getParticipants()).isNotEmpty();
    }

    @Test
    void shouldDeleteShoppingListAndCascadeItemsButKeepParticipants() {
        ShoppingListEntity saved = shoppingListJpaRepository.saveAndFlush(
                newShoppingListEntity("Churrasco", "Carvao", 2, "Eduardo", "11999999999")
        );

        Long listId = saved.getId();
        Long participantId = saved.getParticipants().iterator().next().getId();

        shoppingListJpaRepository.deleteById(listId);
        shoppingListJpaRepository.flush();

        assertThat(shoppingListJpaRepository.findById(listId)).isEmpty();
        assertThat(shoppingListItemJpaRepository.count()).isZero();
        assertThat(shoppingListParticipantJpaRepository.findById(participantId)).isPresent();
    }

    private ShoppingListEntity newShoppingListEntity(
            String listName,
            String itemName,
            int itemQuantity,
            String participantName,
            String participantPhoneNumber
    ) {
        ShoppingListEntity shoppingList = ShoppingListEntity.builder()
                .name(listName)
                .items(new HashSet<>())
                .participants(new HashSet<>())
                .build();

        ShoppingListItemEntity item = ShoppingListItemEntity.builder()
                .name(itemName)
                .quantity(itemQuantity)
                .unit(ItemUnit.UN)
                .purchased(false)
                .shoppingList(shoppingList)
                .build();

        ShoppingListParticipantEntity participant = ShoppingListParticipantEntity.builder()
                .name(participantName)
                .phoneNumber(participantPhoneNumber)
                .shoppingLists(new ArrayList<>())
                .build();

        shoppingList.getItems().add(item);
        shoppingList.getParticipants().add(participant);
        participant.setShoppingLists(List.of(shoppingList));

        return shoppingList;
    }
}
