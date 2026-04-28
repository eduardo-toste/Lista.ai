package com.listaai.notification.application.mapper;

import com.listaai.notification.application.dto.input.HandleShoppingListSharedCommand;
import com.listaai.notification.application.dto.output.NotificationRequest;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    public NotificationRequest toRequest(
            String channel,
            String templateName,
            HandleShoppingListSharedCommand.SharedParticipantInput participant,
            HandleShoppingListSharedCommand command
    ) {
        return new NotificationRequest(
                channel,
                participant.phoneNumber(),
                templateName,
                Map.of(
                        "1", participant.name(),
                        "2", command.shoppingListName(),
                        "3", buildItemsSummary(command)
                )
        );
    }

    private String buildItemsSummary(HandleShoppingListSharedCommand command) {
        return command.items().stream()
                .map(item -> "- " + item.name() + " (" + item.quantity() + " " + item.unit() + ")")
                .collect(Collectors.joining("\n"));
    }

}
