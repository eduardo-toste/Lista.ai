package com.listaai.notification.application.usecase;

import com.listaai.notification.application.dto.input.HandleShoppingListSharedCommand;
import com.listaai.notification.application.dto.output.NotificationRequest;
import com.listaai.notification.application.mapper.NotificationMapper;
import com.listaai.notification.application.port.outbound.NotificationSenderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandleShoppingListSharedServiceTest {

    @Mock
    private NotificationSenderPort notificationSenderPort;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private HandleShoppingListSharedService handleShoppingListSharedService;

    @Test
    void shouldSendOneNotificationPerParticipant() {
        HandleShoppingListSharedCommand command = new HandleShoppingListSharedCommand(
                "shopping-list.shared",
                "2026-05-03T12:00:00Z",
                1L,
                "Churrasco",
                List.of(new HandleShoppingListSharedCommand.SharedItemInput(10L, "Carvao", 2, "UN", false)),
                List.of(
                        new HandleShoppingListSharedCommand.SharedParticipantInput(20L, "Eduardo", "11999999999"),
                        new HandleShoppingListSharedCommand.SharedParticipantInput(21L, "Maria", "11988887777")
                )
        );
        NotificationRequest request1 = new NotificationRequest("WHATSAPP", "11999999999", "shopping-list-shared", java.util.Map.of());
        NotificationRequest request2 = new NotificationRequest("WHATSAPP", "11988887777", "shopping-list-shared", java.util.Map.of());

        when(notificationMapper.toRequest("WHATSAPP", "shopping-list-shared", command.participants().get(0), command))
                .thenReturn(request1);
        when(notificationMapper.toRequest("WHATSAPP", "shopping-list-shared", command.participants().get(1), command))
                .thenReturn(request2);

        handleShoppingListSharedService.handle(command);

        verify(notificationMapper).toRequest("WHATSAPP", "shopping-list-shared", command.participants().get(0), command);
        verify(notificationMapper).toRequest("WHATSAPP", "shopping-list-shared", command.participants().get(1), command);

        ArgumentCaptor<NotificationRequest> captor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationSenderPort, times(2)).send(captor.capture());
        assertThat(captor.getAllValues()).containsExactly(request1, request2);
    }
}
