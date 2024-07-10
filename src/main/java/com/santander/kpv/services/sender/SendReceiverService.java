package com.santander.kpv.services.sender;

import com.ibm.jakarta.jms.JMSTextMessage;
import com.santander.kpv.utils.ExtendedMessageCreatorUtils;
import jakarta.jms.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
public class SendReceiverService {
    private static final String STRING_SFH1 = """

            <?xml version="1.0"?>
            <requestMsg>
            <dse_operationName>SFH1</dse_operationName>
            <dse_formattedData>
            <kColl id="entryData">
            <field id="Usuario" value="MQKPVK" />
            <field id="NRDOCUM" value="99999990932"/>
            <field id="PENUMPE" value=""/>
            <field id="CODERRO" value=""/>
            <field id="MSGERRO" value=""/>
            </kColl>
            </dse_formattedData>
            <dse_processParameters>dse_operationName</dse_processParameters>
            </requestMsg>""";
    private static final String STRING_SFH8 = """
            <?xml version="1.0"?>
            <requestMsg>
            <dse_operationName>SHF8</dse_operationName>
            <dse_formattedData>
            <kColl id="entryData">
            <field id="Usuario" value="MQKPVK" />
            <field id="NRCPFCN" value="777.777.777-77"/>
            </kColl>

            </dse_formattedData>

            <dse_processParameters>dse_operationName</dse_processParameters>

            </requestMsg>""";
    private final JmsTemplate myTemplate;
    @Value("${ibm.mq.queueRequest}")
    protected String sendQueue;
    @Value("${ibm.mq.queueResponse}")
    protected String replyQueue;
    @Value("${ibm.mq.jmsExpiration}")
    protected long jmsExpiration;

    @Autowired
    protected Queue getReplyQueue;
    SendReceiverService(JmsTemplate myTemplate) {
        this.myTemplate = myTemplate;
    }
    public String getMensagem(String cpf) {
        return STRING_SFH8.replaceFirst("777.777.777-77", cpf);
    }
/*
    public String sendSyncReply(String msg) {
        log.info("Sending Message with sendSyncRepl [{}]", msg);
        try {
            return getMessage(msg);
        } catch (Exception e) {
            log.info("sendSyncReply exception [{}]", e.getMessage());
            return e.getMessage();
        }
    }
*/
    /*
    private String getMessage(String msg) {
        log.info("mensagem enviada [{}]", msg);
        Object replay = myTemplate.sendAndReceive(
                sendQueue,
                new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        Message jmsmsg = session.createTextMessage(msg);
                        jmsmsg.setJMSCorrelationID(UUID.randomUUID().toString());
                        jmsmsg.setJMSExpiration(jmsExpiration);
                        jmsmsg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
                        jmsmsg.setJMSReplyTo(session.createQueue(replyQueue));
                        return jmsmsg;
                    }
                });
        try {
            if (null == replay) {
                log.info("Reply is null");
                return "Reply is null";
            }
            if (replay instanceof JMSTextMessage jmsTextMessage) {
                log.info("getMessage(String msg) [{}]", jmsTextMessage.getText());
                return jmsTextMessage.getText();
            }
            log.info("mensagem recebida tipo texto[{}]", replay);
            return "Erro de Retorno";
        } catch (Exception e) {
            log.info("problema recebe retorno [{}]", e.getMessage());
            return e.getMessage();
        }

    }
*/
/*
    public String getMensagemLista(String cpf) {
        return STRING_SFH1.replaceFirst("99999990932", cpf);
    }
    public String sendSyncReplyLista(String msg) {
        log.info("Sending Message with sendSyncReplyLista {} -- ", msg);
        try {
            return getMessage(msg);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
*/

    // novidades

    public String send(String messageRequest) {
        log.info("SendReceiverService : passou 1");
        if (messageRequest == null) {
            throw new NullPointerException("Request object cannot be null.");
        } else {
            log.info("SendReceiverService : passou 2");
            Object messageReply;
            try {
                messageReply = this.sendAndReceiveTextMessage(messageRequest);
                log.info("send(String messageRequest) = {}", messageReply);
            } catch (Exception var4) {
                log.error("send(String messageRequest) {} ", var4.getMessage());
                return var4.getMessage();
            }
            log.info("Parsing AEA response... ");
            if (messageReply instanceof JMSTextMessage jmsTextMessage) {
                try {
                    log.info("mensagem recebida [{}]", jmsTextMessage.getText());
                    return jmsTextMessage.getText();
                } catch (JMSException e) {
                    return e.getMessage();
                }
            }
            return "erro";
        }
    }

    public String sendAndReceiveTextMessage(String msg) throws JMSException {
        log.info("sendAndReceiveTextMessage : chegou 2.1");
        Message requestMessage = this.sendTextMessage(msg);
        log.info("sendAndReceiveTextMessage : chegou 2.2");
        String jmsCorrelationID = requestMessage.getJMSCorrelationID();
        log.info("sendAndReceiveTextMessage-getJMSCorrelationID : chegou 2.3 [{}] ", jmsCorrelationID);
        return this.receiveTextMessage(jmsCorrelationID);
    }

    public Message sendTextMessage(final String msg) throws JMSException {
        ExtendedMessageCreatorUtils<TextMessage> creator = new ExtendedMessageCreatorUtils<TextMessage>() {
            @Override
            public void setParams(TextMessage message) {
                try {
                    String s = UUID.randomUUID().toString();
                    message.setJMSCorrelationID(s);
                    log.info("message.setJMSCorrelationID(UUID.randomUUID().toString()); [{}]", s);
                    message.setText(msg);
                } catch (JMSException e) {
                    log.error(e.getMessage());
                }
            }
        };
        return this.sendMessage(creator);
    }


    public String receiveTextMessage(String jmsCorrelationID) throws JMSException {
        log.info("sendAndReceiveTextMessage : chegou 2.4");
        String selector = "JMSCorrelationID = '" + jmsCorrelationID + "'";
        log.info("Selector: {}", selector);
        TextMessage resMessage = (TextMessage) this.receiveMessage(selector);
        log.info("sendAndReceiveTextMessage : chegou 2.5");
        return resMessage != null ? resMessage.getText() : null;
    }


    public Message sendMessage(ExtendedMessageCreatorUtils<?> creator) throws JMSException {
        if (sendQueue == null) {
            throw new NullPointerException("Request queue can not be null!");
        } else {
            myTemplate.send(sendQueue, creator);
            log.debug("Message Sent!");
            if (log.isTraceEnabled()) {
                log.trace("Message = {}", creator.getMessage());
            }
            return creator.getMessage();
        }
    }


    public Message receiveMessage(String selector) throws JMSException {
        if (selector == null) {
            throw new NullPointerException("Selector can not be null!");
        } else if (replyQueue == null) {
            throw new NullPointerException("Response queue can not be null!");
        } else {
            log.info("receiveMessage(String selector) - 1 [{}] ", getReplyQueue.getQueueName());
            Message resMessage = myTemplate.receiveSelected(getReplyQueue, selector);
            log.info("receiveMessage(String selector) - 2 [{}] ", selector);
            if (log.isTraceEnabled()) {
                log.trace("Response = {}", resMessage);
            }
            return resMessage;
        }

    }

}