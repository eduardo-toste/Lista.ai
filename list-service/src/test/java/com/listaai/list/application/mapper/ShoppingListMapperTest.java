package com.listaai.list.application.mapper;

import com.listaai.list.application.dto.input.ShoppingListCommand;
import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingListMapperTest {

    private ShoppingListMapper shoppingListMapper;

    @BeforeEach
    void setUp() {
        shoppingListMapper = new ShoppingListMapper(
                new ShoppingListItemMapper(),
                new ShoppingListParticipantMapper()
        );
    }

    @Test
    void shouldMapCreateShoppingListCommandToDomain() {
        ShoppingListCommand command = new ShoppingListCommand(
                "Lista do mercado",
                List.of(new ShoppingListItemCommand("Arroz", 2, ItemUnit.KG)),
                List.of(new ShoppingListParticipantCommand("Eduardo", "11999990001"))
        );

        ShoppingList result = shoppingListMapper.toDomain(command);

        assertNull(result.getId());
        assertEquals("Lista do mercado", result.getName());
        assertEquals(1, result.getItems().size());
        assertNull(result.getItems().getFirst().getId());
        assertEquals("Arroz", result.getItems().getFirst().getName());
        assertEquals(2, result.getItems().getFirst().getQuantity());
        assertEquals(ItemUnit.KG, result.getItems().getFirst().getUnit());
        assertFalse(result.getItems().getFirst().isPurchased());
        assertEquals(1, result.getParticipants().size());
        assertNull(result.getParticipants().getFirst().getId());
        assertEquals("Eduardo", result.getParticipants().getFirst().getName());
        assertEquals("11999990001", result.getParticipants().getFirst().getPhoneNumber());
    }

    @Test
    void shouldMapShoppingListDomainToOutput() {
        ShoppingList domain = new ShoppingList(
                1L,
                "Lista do mercado",
                List.of(new ShoppingListItem(10L, "Arroz", 2, ItemUnit.KG, true)),
                List.of(new ShoppingListParticipant(20L, "Eduardo", "11999990001"))
        );

        ShoppingListOutput result = shoppingListMapper.toOutput(domain);

        assertEquals(1L, result.id());
        assertEquals("Lista do mercado", result.name());
        assertEquals(1, result.items().size());
        assertEquals(10L, result.items().getFirst().id());
        assertEquals("Arroz", result.items().getFirst().name());
        assertEquals(2, result.items().getFirst().quantity());
        assertEquals(ItemUnit.KG, result.items().getFirst().unit());
        assertTrue(result.items().getFirst().purchased());
        assertEquals(1, result.participants().size());
        assertEquals(20L, result.participants().getFirst().id());
        assertEquals("Eduardo", result.participants().getFirst().name());
        assertEquals("11999990001", result.participants().getFirst().phoneNumber());
    }

    @Test
    void shouldMapShoppingListPageToOutputPage() {
        ShoppingList firstList = new ShoppingList(
                1L,
                "Lista 1",
                List.of(new ShoppingListItem(10L, "Arroz", 2, ItemUnit.KG)),
                List.of(new ShoppingListParticipant(20L, "Eduardo", "11999990001"))
        );
        ShoppingList secondList = new ShoppingList(
                2L,
                "Lista 2",
                List.of(new ShoppingListItem(11L, "Feijao", 1, ItemUnit.KG, true)),
                List.of(new ShoppingListParticipant(21L, "Maria", "11999990002"))
        );
        Page<ShoppingList> page = new PageImpl<>(
                List.of(firstList, secondList),
                PageRequest.of(0, 2),
                2
        );

        Page<ShoppingListOutput> result = shoppingListMapper.toPageOutput(page);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Lista 1", result.getContent().get(0).name());
        assertEquals("Arroz", result.getContent().get(0).items().getFirst().name());
        assertEquals("Lista 2", result.getContent().get(1).name());
        assertTrue(result.getContent().get(1).items().getFirst().purchased());
        assertEquals("Maria", result.getContent().get(1).participants().getFirst().name());
    }

}
