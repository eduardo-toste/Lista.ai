package com.listaai.notification.configuration.usecase;

import com.listaai.notification.application.mapper.NotificationMapper;
import com.listaai.notification.application.port.inbound.HandleShoppingListSharedUseCase;
import com.listaai.notification.application.port.outbound.NotificationSenderPort;
import com.listaai.notification.application.usecase.HandleShoppingListSharedService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationUseCaseConfig {

    @Bean
    public HandleShoppingListSharedUseCase handleShoppingListSharedUseCase(NotificationSenderPort notificationSenderPort, NotificationMapper notificationMapper) {
        return new HandleShoppingListSharedService(notificationSenderPort, notificationMapper);
    }
}
