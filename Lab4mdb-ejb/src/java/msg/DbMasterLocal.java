/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msg;

import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface DbMasterLocal {

    void writeInteger(int number);

    void writeMessage(String message);

    ArrayList<String> getMessageList();

    int getTotal();

    void deleteMessages();

    void deleteNumbers();
    
}
