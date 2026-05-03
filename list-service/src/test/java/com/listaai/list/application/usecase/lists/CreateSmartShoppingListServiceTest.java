package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.input.SmartShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.outbound.RecipeExtractionPort;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateSmartShoppingListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private RecipeExtractionPort recipeExtractionPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private CreateSmartShoppingListService createSmartShoppingListService;

    private SmartShoppingListCommand command;
    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;

    @BeforeEach
    void setUp() {
        command = new SmartShoppingListCommand(
                "Jantar",
                List.of(new ShoppingListParticipantCommand("Eduardo", "11999999999")),
                "Ingredientes para molho"
        );
        shoppingList = new ShoppingList(
                null,
                "Jantar",
                new ArrayList<>(),
                List.of(new ShoppingListParticipant(null, "Eduardo", "11999999999"))
        );
        savedShoppingList = new ShoppingList(
                1L,
                "Jantar",
                List.of(new ShoppingListItem(1L, "Tomate", 3, ItemUnit.UN, false)),
                List.of(new ShoppingListParticipant(1L, "Eduardo", "11999999999"))
        );
        shoppingListOutput = new ShoppingListOutput(1L, "Jantar", List.of(), List.of());
    }

    @Test
    void shouldCreateSmartShoppingListWithDeduplicatedRecipeItems() {
        List<ShoppingListItem> extractedItems = List.of(
                new ShoppingListItem(null, "Tomate", 2, ItemUnit.UN, false),
                new ShoppingListItem(null, " tomate ", 1, ItemUnit.UN, false),
                new ShoppingListItem(null, "Cebola", 4, ItemUnit.KG, false)
        );

        when(recipeExtractionPort.extractItemsFromRecipe(command.recipeMessage())).thenReturn(extractedItems);
        when(shoppingListMapper.toDomain(command)).thenReturn(shoppingList);
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = createSmartShoppingListService.createSmartShoppingList(command);

        assertSame(shoppingListOutput, result);
        assertEquals(2, shoppingList.getItems().size());
        assertEquals("Tomate", shoppingList.getItems().get(0).getName());
        assertEquals(3, shoppingList.getItems().get(0).getQuantity());
        assertEquals(ItemUnit.UN, shoppingList.getItems().get(0).getUnit());
        assertEquals("Cebola", shoppingList.getItems().get(1).getName());
        assertEquals(4, shoppingList.getItems().get(1).getQuantity());
        assertEquals(ItemUnit.KG, shoppingList.getItems().get(1).getUnit());

        var inOrder = inOrder(recipeExtractionPort, shoppingListMapper, shoppingListRepositoryPort);
        inOrder.verify(recipeExtractionPort).extractItemsFromRecipe(command.recipeMessage());
        inOrder.verify(shoppingListMapper).toDomain(command);
        inOrder.verify(shoppingListRepositoryPort).save(shoppingList);
        inOrder.verify(shoppingListMapper).toOutput(savedShoppingList);
    }

    @Test
    void shouldCreateSmartShoppingListWhenRecipeExtractionReturnsNoItems() {
        when(recipeExtractionPort.extractItemsFromRecipe(command.recipeMessage())).thenReturn(null);
        when(shoppingListMapper.toDomain(command)).thenReturn(shoppingList);
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = createSmartShoppingListService.createSmartShoppingList(command);

        assertSame(shoppingListOutput, result);
        assertEquals(0, shoppingList.getItems().size());
        verify(shoppingListRepositoryPort).save(shoppingList);
    }
}
