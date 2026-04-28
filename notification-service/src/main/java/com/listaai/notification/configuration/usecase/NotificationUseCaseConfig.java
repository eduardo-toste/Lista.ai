package com.listaai.notification.configuration.usecase;

import com.listaai.notification.application.port.inbound.HandleShoppingListSharedUseCase;
import com.listaai.notification.application.usecase.HandleShoppingListSharedService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationUseCaseConfig {

    @Bean
    public HandleShoppingListSharedUseCase handleShoppingListSharedUseCase() {
        return new HandleShoppingListSharedService();
    }
}
