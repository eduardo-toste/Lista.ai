package com.listaai.list.adapter.outbound.persistence.adapter;

import com.listaai.list.adapter.outbound.persistence.mapper.ShoppingListItemPersistenceMapper;
import com.listaai.list.adapter.outbound.persistence.mapper.ShoppingListParticipantPersistenceMapper;
import com.listaai.list.adapter.outbound.persistence.mapper.ShoppingListPersistenceMapper;
import com.listaai.list.adapter.outbound.persistence.repository.ShoppingListJpaRepository;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        ShoppingListPersistenceAdapter.class,
        ShoppingListPersistenceMapper.class,
        ShoppingListItemPersistenceMapper.class,
        ShoppingListParticipantPersistenceMapper.class
})
class ShoppingListPersistenceAdapterTest {

    @Autowired
    private ShoppingListPersistenceAdapter shoppingListPersistenceAdapter;

    @Autowired
    private ShoppingListJpaRepository shoppingListJpaRepository;

    @Test
    void shouldSaveShoppingListDomainObject() {
        ShoppingList shoppingList = newShoppingList(null, "Churrasco", "Carvao", 2, false, "Eduardo", "11999999999");

        ShoppingList saved = shoppingListPersistenceAdapter.save(shoppingList);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Churrasco");
        assertThat(saved.getItems()).hasSize(1);
        assertThat(saved.getParticipants()).hasSize(1);
        assertThat(saved.getItems().getFirst().getId()).isNotNull();
        assertThat(saved.getParticipants().getFirst().getId()).isNotNull();
    }

    @Test
    void shouldFindShoppingListById() {
        ShoppingList saved = shoppingListPersistenceAdapter.save(
                newShoppingList(null, "Mercado", "Arroz", 3, false, "Maria", "11988887777")
        );

        var found = shoppingListPersistenceAdapter.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getName()).isEqualTo("Mercado");
        assertThat(found.get().getItems()).hasSize(1);
        assertThat(found.get().getParticipants()).hasSize(1);
        assertThat(found.get().getItems().getFirst().getName()).isEqualTo("Arroz");
    }

    @Test
    void shouldReturnPagedShoppingListsAsDomainObjects() {
        shoppingListPersistenceAdapter.save(
                newShoppingList(null, "Lista A", "Carvao", 2, false, "Eduardo", "11999999999")
        );
        shoppingListPersistenceAdapter.save(
                newShoppingList(null, "Lista B", "Arroz", 1, true, "Maria", "11988887777")
        );

        Page<ShoppingList> page = shoppingListPersistenceAdapter.findAll(PageRequest.of(0, 1));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent().getFirst().getItems()).hasSize(1);
        assertThat(page.getContent().getFirst().getParticipants()).hasSize(1);
    }

    @Test
    void shouldDeleteShoppingListById() {
        ShoppingList saved = shoppingListPersistenceAdapter.save(
                newShoppingList(null, "Churrasco", "Carvao", 2, false, "Eduardo", "11999999999")
        );

        shoppingListPersistenceAdapter.deleteById(saved.getId());

        assertThat(shoppingListPersistenceAdapter.findById(saved.getId())).isEmpty();
        assertThat(shoppingListJpaRepository.findById(saved.getId())).isEmpty();
    }

    private ShoppingList newShoppingList(
            Long id,
            String listName,
            String itemName,
            int itemQuantity,
            boolean purchased,
            String participantName,
            String participantPhoneNumber
    ) {
        return new ShoppingList(
                id,
                listName,
                List.of(new ShoppingListItem(null, itemName, itemQuantity, ItemUnit.UN, purchased)),
                List.of(new ShoppingListParticipant(null, participantName, participantPhoneNumber))
        );
    }
}
