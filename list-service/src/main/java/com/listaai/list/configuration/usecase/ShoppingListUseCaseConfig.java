package com.listaai.list.configuration.usecase;

import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.lists.CreateShoppingListUseCase;
import com.listaai.list.application.port.inbound.lists.DeleteShoppingListUseCase;
import com.listaai.list.application.port.inbound.lists.GetShoppingListUseCase;
import com.listaai.list.application.port.inbound.lists.UpdateListNameUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.usecase.lists.CreateShoppingListService;
import com.listaai.list.application.usecase.lists.DeleteShoppingListService;
import com.listaai.list.application.usecase.lists.GetShoppingListService;
import com.listaai.list.application.usecase.lists.UpdateListNameService;
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

    @Bean
    public DeleteShoppingListUseCase deleteShoppingListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort) {
        return new DeleteShoppingListService(shoppingListRepositoryPort);
    }

}
