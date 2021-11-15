/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msg;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *jms/__defaultConnectionFactory
 * @author User
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/IntegerQueue")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class IntegerConsumer implements MessageListener {

    @EJB
    private DbMasterLocal dbMaster;
    

    public IntegerConsumer() {
    }
    
    @Override
    public void onMessage(Message message) {
        if(message instanceof ObjectMessage){
            ObjectMessage om = (ObjectMessage) message;
            try {
                Integer num = (Integer) om.getObject();
                dbMaster.writeInteger(num);
                System.out.println("MDB NUMBER OK " + num);
            } catch (JMSException ex) {
                System.out.println("Error MDB NUMBER " + ex.getMessage());
            }
        }else{
            System.out.println("Not ObjectMessage in IntegerConsumer");
        }
        
    }
    
}
