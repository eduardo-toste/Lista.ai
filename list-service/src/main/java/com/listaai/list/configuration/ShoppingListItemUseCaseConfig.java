package com.listaai.list.configuration;

import com.listaai.list.application.mapper.ShoppingListItemMapper;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.items.AddItemToListUseCase;
import com.listaai.list.application.port.inbound.items.PurchaseItemFromListUseCase;
import com.listaai.list.application.port.inbound.items.RemoveItemFromListUseCase;
import com.listaai.list.application.port.inbound.items.UpdateItemFromListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.usecase.items.AddItemToListService;
import com.listaai.list.application.usecase.items.PurchaseItemFromListService;
import com.listaai.list.application.usecase.items.RemoveItemFromListService;
import com.listaai.list.application.usecase.items.UpdateItemFromListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingListItemUseCaseConfig {

    @Bean
    public AddItemToListUseCase addItemToListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper, ShoppingListItemMapper shoppingListItemMapper) {
        return new AddItemToListService(shoppingListRepositoryPort, shoppingListMapper, shoppingListItemMapper);
    }

    @Bean
    public RemoveItemFromListUseCase removeItemFromListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        return new RemoveItemFromListService(shoppingListRepositoryPort, shoppingListMapper);
    }

    @Bean
    public UpdateItemFromListUseCase updateItemFromListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        return new UpdateItemFromListService(shoppingListRepositoryPort, shoppingListMapper);
    }

    @Bean
    public PurchaseItemFromListUseCase purchaseItemFromListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        return new PurchaseItemFromListService(shoppingListRepositoryPort, shoppingListMapper);
    }

}
