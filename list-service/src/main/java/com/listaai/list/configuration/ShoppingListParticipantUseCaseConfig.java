package com.listaai.list.configuration;

import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.mapper.ShoppingListParticipantMapper;
import com.listaai.list.application.port.inbound.participants.AddParticipantToListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.usecase.participants.AddParticipantToListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingListParticipantUseCaseConfig {

    @Bean
    public AddParticipantToListUseCase addParticipantToListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper, ShoppingListParticipantMapper shoppingListParticipantMapper) {
        return new AddParticipantToListService(shoppingListRepositoryPort, shoppingListMapper, shoppingListParticipantMapper);
    }

}
