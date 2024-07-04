package com.santander.kpv.config;

import jakarta.jms.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class MQConfigurationTest {

    @Mock
    private ConnectionFactory connectionFactory;

    @InjectMocks
    private MQConfigurationOld mqConfiguration;

    @Test
    void testMyTemplate() {
        // Simula o retorno de uma conexão válida pelo ConnectionFactory
        JmsTemplate jmsTemplate = mqConfiguration.myTemplate(connectionFactory);
        assertNotNull(jmsTemplate);
    }
}
