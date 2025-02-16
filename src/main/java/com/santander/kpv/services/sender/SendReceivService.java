/*
 * (c) Copyright IBM Corporation 2021, 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.santander.kpv.services.sender;

import com.ibm.jakarta.jms.JMSTextMessage;
import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class SendReceivService {
    protected final Log logger = LogFactory.getLog(getClass());
    private final JmsTemplate myTemplateAnt;
    @Value("${app.queue.name1}")
    public String sendQueue;
    @Value("${app.queue.name2}")
    public String replyQueue;
    @Value("${app.JMSExpiration}")
    private long jmsExpiration;

    SendReceivService(JmsTemplate myTemplateAnt) {
        this.myTemplateAnt = myTemplateAnt;
    }

    public String sendSyncReply(String msg) {
        logger.info("Sending Message with Sync Reply");
        Object reply = myTemplateAnt.sendAndReceive(sendQueue, new MessageCreator() {
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
            logger.info("Reply received");

            if (null == reply) {
                logger.info("Reply is null");
            } else if (reply instanceof JMSTextMessage) {
                logger.info("Reply is a text message, attempting to extract data");
                JMSTextMessage txtReply = (JMSTextMessage) reply;
                logger.info("Resultado final : sendo recebido pelo sender " + txtReply.getText());
                return txtReply.getText();
            } else {
                logger.info("reply is not null, but of unexpected type");
                logger.info("reply is of type : " + reply.getClass());
            }
        } catch (JMSException e) {
            logger.warn("JMSException processing reply");
            logger.warn(e.getMessage());
        }
        return msg;
    }

}



