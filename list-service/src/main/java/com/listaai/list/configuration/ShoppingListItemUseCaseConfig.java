package com.listaai.list.configuration;

import com.listaai.list.application.mapper.ShoppingListItemMapper;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.items.AddItemToListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.usecase.items.AddItemToListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingListItemUseCaseConfig {

    @Bean
    public AddItemToListUseCase addItemToListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper, ShoppingListItemMapper shoppingListItemMapper) {
        return new AddItemToListService(shoppingListRepositoryPort, shoppingListMapper, shoppingListItemMapper);
    }

}
