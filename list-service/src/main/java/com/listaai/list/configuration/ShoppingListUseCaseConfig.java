package com.listaai.list.configuration;

import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.CreateShoppingListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.usecase.CreateShoppingListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingListUseCaseConfig {

    @Bean
    public CreateShoppingListUseCase createShoppingListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        return new CreateShoppingListService(shoppingListRepositoryPort, shoppingListMapper);
    }

}
