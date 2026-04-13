package com.listaai.list.application.mapper;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListParticipantMapper {

    public ShoppingListParticipant toDomain(ShoppingListParticipantCommand command) {
        return new ShoppingListParticipant(
                null,
                command.name(),
                command.phoneNumber()
        );
    }

    public ShoppingListParticipantOutput toOutput(ShoppingListParticipant domain) {
        return new ShoppingListParticipantOutput(
                domain.getId(),
                domain.getName(),
                domain.getPhoneNumber()
        );
    }

}
