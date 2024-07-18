package com.santander.kpv.config;


import com.ibm.msg.client.jms.*;

import com.santander.kpv.exceptions.MyRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Session;

@Configuration
@EnableJms
@Slf4j
public class MQTextMessageConfiguration {

    @Value("${ibm.mq.host}")
    private String mqHostName;

    @Value("${ibm.mq.port}")
    private int mqPort;

    @Value("${ibm.mq.queueManager}")
    private String mqQueueManager;

    @Value("${ibm.mq.channel}")
    private String mqChannel;

    @Value("${ibm.mq.user}")
    private String user;
    @Value("${ibm.mq.password}")
    private String password;
    @Value("${ibm.mq.queueRequest}")
    private String queueRequest;

    @Value("${ibm.mq.queueResponse}")
    private String queueResponse;

    public String getQueueRequest() {
        return queueRequest;
    }

    public String getQueueResponse() {
        return queueResponse;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getMqHostName() {
        return mqHostName;
    }

    public int getMqPort() {
        return mqPort;
    }

    public String getMqQueueManager() {
        return mqQueueManager;
    }

    public String getMqChannel() {
        return mqChannel;
    }

    @Bean("jmsConnectionFactory")
    public JmsConnectionFactory jmsConnectionFactory() {
        try {
            JmsFactoryFactory jmsFactoryFactory = JmsFactoryFactory.getInstance(CustomMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = jmsFactoryFactory.createConnectionFactory();
            cf.setStringProperty(CustomMQConstants.WMQ_HOST_NAME, getMqHostName());
            cf.setIntProperty(CustomMQConstants.WMQ_PORT, getMqPort());
            cf.setStringProperty(CustomMQConstants.WMQ_CHANNEL, getMqChannel());
            cf.setIntProperty(CustomMQConstants.WMQ_CONNECTION_MODE, 1);
            cf.setStringProperty(CustomMQConstants.WMQ_QUEUE_MANAGER, getMqQueueManager());
            cf.setStringProperty(CustomMQConstants.WMQ_APPLICATIONNAME, "KPV.BLINDAGEM");
            cf.setBooleanProperty(CustomMQConstants.USER_AUTHENTICATION_MQCSP, true);
            cf.setStringProperty(CustomMQConstants.USERID, getUser());
            cf.setStringProperty(CustomMQConstants.PASSWORD, getPassword());
            return cf;
        } catch (JMSException e) {
            throw new MyRuntimeException(e);
        }
    }

    @Bean("jmsContext")
    public JMSContext jmsContext(JmsConnectionFactory jmsConnectionFactory){
            return jmsConnectionFactory.createContext();
    }

    @Bean("queueRequest")
    public Destination queueRequest(JMSContext jmsContext){
        return jmsContext.createQueue(getQueueRequest());
    }
    @Bean("queueResponse")
    public Destination queueResponse(JMSContext jmsContext){
        return jmsContext.createQueue(getQueueResponse());
    }

    @Bean("session")
    public Session session(JmsConnectionFactory jmsConnectionFactory) throws JMSException {
        return jmsConnectionFactory.createConnection().createSession();
    }
}