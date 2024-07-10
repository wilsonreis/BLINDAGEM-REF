package com.santander.kpv.services.sender;

import javax.jms.DeliveryMode;

import com.santander.kpv.exceptions.MyException;
import com.santander.kpv.exceptions.MyRuntimeException;
import com.santander.kpv.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.UUID;
@Service
@Slf4j
public class SendReceiverTextMessageService {
    private final JMSContext jmsContext;
    private final Destination queueRequest;
    private final Destination queueResponse;

    @Value("${ibm.mq.jmsExpiration}")
    private long jmsExpiration;

    public void setJmsExpiration(long jmsExpiration) {
        this.jmsExpiration = jmsExpiration;
    }

    public SendReceiverTextMessageService(JMSContext jmsContext, Destination queueRequest, Destination queueResponse) {
        this.jmsContext = jmsContext;
        this.queueRequest = queueRequest;
        this.queueResponse = queueResponse;
    }

    public String enviaRecebeMensagensSFH(String cpf, String sfh) {
        String sfhRetornado = StringUtils.getMensagem(cpf, sfh);
        log.info("sfhRetornado [{}]", sfhRetornado);
        if (sfhRetornado.length() < 5) {
            return "Erro de parâmetro SFH";
        }
        return enviaRecebeMensagens(sfhRetornado);
    }

    public String enviaRecebeMensagens(String mensagem) {
        TextMessage message = jmsContext.createTextMessage(mensagem);
        String receivedMessage = "Erro ao receber mensagem";
        try {
            configureMessage(message);
            sendMessage(message);
            receivedMessage = receiveMessage(message);
        } catch (Exception e) {
            log.error("Erro inesperado", e);
            throw new MyRuntimeException(e.getMessage());
        }
        return receivedMessage;
    }

    private void configureMessage(TextMessage message) throws JMSException {
        message.setJMSExpiration(jmsExpiration);
        message.setJMSCorrelationID(UUID.randomUUID().toString());
        log.info("setJMSCorrelationID(UUID.randomUUID().toString()) {}", message.getJMSCorrelationID());
        message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
        message.setJMSReplyTo(queueResponse);
    }

    private void sendMessage(TextMessage message) throws JMSException {
        JMSProducer producer = jmsContext.createProducer();
        producer.send(queueRequest, message);
        log.info("Mensagem enviada para a fila de requisição");
    }

    private String receiveMessage(TextMessage message) throws JMSException {
        String receivedMessage;
        try {
            String messageSelector = "JMSCorrelationID = '" + message.getJMSCorrelationID() + "'";
            JMSConsumer consumer = jmsContext.createConsumer(queueResponse, messageSelector);
            receivedMessage = consumer.receiveBody(String.class, 15000);
            if (receivedMessage == null) {
                log.info("Tempo de espera expirou sem receber mensagem");
                receivedMessage = "Tempo de espera expirado";
                throw new MyRuntimeException("Tempo de espera expirado");
            } else {
                log.info("Mensagem recebida: {}", receivedMessage);
            }
        } catch (JMSException e) {
            log.error("Erro ao receber mensagem", e);
            throw new MyRuntimeException(e.getMessage());
        }
        return receivedMessage;
    }
}
