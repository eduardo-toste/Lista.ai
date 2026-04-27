package com.listaai.list.configuration.usecase;

import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.mapper.ShoppingListParticipantMapper;
import com.listaai.list.application.port.inbound.participants.AddParticipantToListUseCase;
import com.listaai.list.application.port.inbound.participants.RemoveParticipantFromListUseCase;
import com.listaai.list.application.port.inbound.participants.UpdateParticipantFromListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.usecase.participants.AddParticipantToListService;
import com.listaai.list.application.usecase.participants.RemoveParticipantFromListService;
import com.listaai.list.application.usecase.participants.UpdateParticipantFromListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingListParticipantUseCaseConfig {

    @Bean
    public AddParticipantToListUseCase addParticipantToListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper, ShoppingListParticipantMapper shoppingListParticipantMapper) {
        return new AddParticipantToListService(shoppingListRepositoryPort, shoppingListMapper, shoppingListParticipantMapper);
    }

    @Bean
    public UpdateParticipantFromListUseCase updateParticipantFromListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        return new UpdateParticipantFromListService(shoppingListRepositoryPort, shoppingListMapper);
    }

    @Bean
    public RemoveParticipantFromListUseCase removeParticipantFromListUseCase(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        return new RemoveParticipantFromListService(shoppingListRepositoryPort, shoppingListMapper);
    }

}
