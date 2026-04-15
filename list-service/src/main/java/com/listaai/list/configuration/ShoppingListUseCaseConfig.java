package com.listaai.list.configuration;

import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.CreateShoppingListUseCase;
import com.listaai.list.application.port.inbound.GetShoppingListUseCase;
import com.listaai.list.application.port.inbound.UpdateListNameUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.usecase.CreateShoppingListService;
import com.listaai.list.application.usecase.GetShoppingListService;
import com.listaai.list.application.usecase.UpdateListNameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingListUseCaseConfig {

    @Bean
    public CreateShoppingListUseCase createShoppingListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        return new CreateShoppingListService(shoppingListRepositoryPort, shoppingListMapper);
    }

    @Bean
    public GetShoppingListUseCase getShoppingListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        return new GetShoppingListService(shoppingListRepositoryPort, shoppingListMapper);
    }

    @Bean
    public UpdateListNameUseCase updateListNameUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        return new UpdateListNameService(shoppingListRepositoryPort, shoppingListMapper);
    }

}
