package com.santander.kpv.services.sender;

import com.ibm.jakarta.jms.JMSTextMessage;
import jakarta.jms.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class SendReceivServiceTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private SendReceivService sendReceivService;

    @Mock
    private Session session;

    @Mock
    private JMSTextMessage textMessage;

    @Mock
    private Queue queue;

    @Value("${app.queue.name1}")
    private String sendQueue;

    @Value("${app.queue.name2}")
    private String replyQueue;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testSendSyncReplyWithJMSException() throws JMSException {
        when(jmsTemplate.sendAndReceive(anyString(), any(MessageCreator.class))).thenReturn(textMessage);
        when(textMessage.getText()).thenThrow(new JMSException("Test Exception"));

        String result = sendReceivService.sendSyncReply("Test Message");

        assertEquals("Test Message", result);
    }


}
