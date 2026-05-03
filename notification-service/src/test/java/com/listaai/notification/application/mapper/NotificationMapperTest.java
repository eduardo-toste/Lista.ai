package com.listaai.notification.application.mapper;

import com.listaai.notification.application.dto.input.HandleShoppingListSharedCommand;
import com.listaai.notification.application.dto.output.NotificationRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationMapperTest {

    private final NotificationMapper notificationMapper = new NotificationMapper();

    @Test
    void shouldMapCommandAndParticipantToNotificationRequest() {
        HandleShoppingListSharedCommand command = new HandleShoppingListSharedCommand(
                "shopping-list.shared",
                "2026-05-03T12:00:00Z",
                1L,
                "Churrasco",
                List.of(
                        new HandleShoppingListSharedCommand.SharedItemInput(10L, "Carvao", 2, "UN", false),
                        new HandleShoppingListSharedCommand.SharedItemInput(11L, "Picanha", 1, "KG", false)
                ),
                List.of(
                        new HandleShoppingListSharedCommand.SharedParticipantInput(20L, "Eduardo", "11999999999")
                )
        );

        NotificationRequest result = notificationMapper.toRequest(
                "WHATSAPP",
                "shopping-list-shared",
                command.participants().getFirst(),
                command
        );

        assertThat(result.channel()).isEqualTo("WHATSAPP");
        assertThat(result.recipient()).isEqualTo("11999999999");
        assertThat(result.templateName()).isEqualTo("shopping-list-shared");
        assertThat(result.variables()).containsEntry("1", "Eduardo");
        assertThat(result.variables()).containsEntry("2", "Churrasco");
        assertThat(result.variables().get("3")).isEqualTo("- Carvao (2 UN)\n- Picanha (1 KG)");
    }
}
