package com.listaai.list.configuration.usecase;

import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.lists.*;
import com.listaai.list.application.port.outbound.ShoppingListEventPublisherPort;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.usecase.lists.*;
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

    @Bean
    public ShareShoppingListUseCase shareShoppingListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListEventPublisherPort shoppingListEventPublisherPort) {
        return new ShareShoppingListService(shoppingListRepositoryPort, shoppingListEventPublisherPort);
    }

}
