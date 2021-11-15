/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msg;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 *
 * @author User
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/MessageQueue")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MessageConsumer implements MessageListener {

    @EJB
    private DbMasterLocal dbMaster;

    public MessageConsumer() {
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("Message MDB " + message);

        if (message instanceof TextMessage) {
            String msg = null;
            try {
                msg = ((TextMessage) message).getText();
                System.out.println("MDB MESSAGE OK " + msg);
                dbMaster.writeMessage(msg);
            } catch (JMSException ex) {
                System.out.println("Error MDB MESSAGE " + ex.getMessage());
            }
        }else{
            System.out.println("Not text message in MessageConsumer");
        }

    }

}
