package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.dto.input.SmartShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.lists.CreateSmartShoppingListUseCase;
import com.listaai.list.application.port.outbound.RecipeExtractionPort;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateSmartShoppingListService implements CreateSmartShoppingListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final RecipeExtractionPort recipeExtractionPort;
    private final ShoppingListMapper shoppingListMapper;

    public CreateSmartShoppingListService(RecipeExtractionPort recipeExtractionPort, ShoppingListMapper shoppingListMapper, ShoppingListRepositoryPort shoppingListRepositoryPort) {
        this.recipeExtractionPort = recipeExtractionPort;
        this.shoppingListMapper = shoppingListMapper;
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
    }

    @Override
    public ShoppingListOutput createSmartShoppingList(SmartShoppingListCommand command) {
        List<ShoppingListItem> recipeItems = recipeExtractionPort.extractItemsFromRecipe(command.recipeMessage());
        List<ShoppingListItem> deduplicatedItems = deduplicateItems(recipeItems);
        ShoppingList shoppingList = shoppingListMapper.toDomain(command);

        deduplicatedItems.forEach(shoppingList::addItem);
        ShoppingList savedShoppingList = shoppingListRepositoryPort.save(shoppingList);

        return shoppingListMapper.toOutput(savedShoppingList);
    }

    private List<ShoppingListItem> deduplicateItems(List<ShoppingListItem> items) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        Map<String, ShoppingListItem> deduplicatedItems = new LinkedHashMap<>();

        for (ShoppingListItem item : items) {
            String key = buildItemKey(item);

            deduplicatedItems.merge(
                    key,
                    copyItem(item),
                    (existingItem, currentItem) -> new ShoppingListItem(
                            null,
                            existingItem.getName(),
                            existingItem.getQuantity() + currentItem.getQuantity(),
                            existingItem.getUnit(),
                            existingItem.isPurchased() || currentItem.isPurchased()
                    )
            );
        }

        return List.copyOf(deduplicatedItems.values());
    }

    private String buildItemKey(ShoppingListItem item) {
        return normalizeName(item.getName()) + "|" + item.getUnit();
    }

    private String normalizeName(String name) {
        if (name == null) {
            return "";
        }

        return name.trim()
                .replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);
    }

    private ShoppingListItem copyItem(ShoppingListItem item) {
        return new ShoppingListItem(
                null,
                item.getName(),
                item.getQuantity(),
                item.getUnit(),
                item.isPurchased()
        );
    }
}
