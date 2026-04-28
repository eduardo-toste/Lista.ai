package com.listaai.notification.application.usecase;

import com.listaai.notification.application.dto.input.HandleShoppingListSharedCommand;
import com.listaai.notification.application.dto.output.NotificationRequest;
import com.listaai.notification.application.mapper.NotificationMapper;
import com.listaai.notification.application.port.inbound.HandleShoppingListSharedUseCase;
import com.listaai.notification.application.port.outbound.NotificationSenderPort;

public class HandleShoppingListSharedService implements HandleShoppingListSharedUseCase {

    private final NotificationSenderPort notificationSenderPort;
    private final NotificationMapper notificationMapper;

    public HandleShoppingListSharedService(
            NotificationSenderPort notificationSenderPort,
            NotificationMapper notificationMapper
    ) {
        this.notificationSenderPort = notificationSenderPort;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public void handle(HandleShoppingListSharedCommand command) {
        command.participants().forEach(participant -> {
            NotificationRequest request = notificationMapper.toRequest(
                    "WHATSAPP",
                    "shopping-list-shared",
                    participant,
                    command
            );
            notificationSenderPort.send(request);
        });
    }
}
