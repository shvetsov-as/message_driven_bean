/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;

/**
 *
 * @author User
 */
@Singleton
public class DbMaster implements DbMasterLocal {

    Connection conn = null;

    @Override
    public void writeInteger(int number) {
        Connection conn = getConnection();
        String sql = "INSERT INTO numbers VALUES (" + number + ")";
        try{
            conn.createStatement().executeUpdate(sql);
            System.out.println("Number " + number + " add to db");
        } catch (SQLException ex) {
            System.out.println("Error " + number + " not add to db" + ex.getMessage());
        }
        
    }

    @Override
    public void writeMessage(String message) {
        Connection conn = getConnection();
        String sql = "INSERT INTO messages VALUES ('" + message + "')";
        try{
            conn.createStatement().executeUpdate(sql);
            System.out.println("String " + message + " add to db");
        } catch (SQLException ex) {
            System.out.println("Error " + message + " not add to db" + ex.getMessage());
        }
    }

    @Override
    public ArrayList<String> getMessageList() {

        ArrayList<String> messages = new ArrayList<>();
        Connection conn = getConnection();

        try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM MESSAGES")) {
            while (rs.next()) {
                messages.add(rs.getString(1));// 1 - perviy stolbec/ mojno napisat po imeny "message", no po nomery bbIstree
            }
            System.out.println("Yspeshnoe izvlechenie from message");

        } catch (SQLException ex) {
            System.out.println("Error from... MESSAGES " + ex.getMessage());
        }

        return messages;
    }

    @Override
    public int getTotal() {
        Connection conn = getConnection();
        int sum = -1;
         try (ResultSet rs = conn.createStatement().executeQuery("SELECT SUM(NUMBER) FROM NUMBERS")) {
            if(rs.next()){
                sum = rs.getInt(1);
            }
            System.out.println("Yspeshnoe izvlechenie from message");

        } catch (SQLException ex) {
            System.out.println("Error... from NUMBERS " + ex.getMessage());
        }
        return sum;
    }

    @Override
    public void deleteMessages() {
        Connection conn = getConnection();
         String sql = "DELETE FROM messages";
        try{
            conn.createStatement().executeUpdate(sql);
            System.out.println("Messages are deleted from db");
        } catch (SQLException ex) {
            System.out.println("Error while deleting messages from db " + ex.getMessage());
        }
    }

    @Override
    public void deleteNumbers() {
        Connection conn = getConnection();
         String sql = "DELETE FROM numbers";
        try{
            conn.createStatement().executeUpdate(sql);
            System.out.println("Numbers are deleted from db");
        } catch (SQLException ex) {
            System.out.println("Error while deleting Numbers from db " + ex.getMessage());
        }
    }

    private Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection("jdbc:derby://localhost:1527/test", "test", "test");
                System.out.println("Connected to " + conn.getSchema());
            }
        } catch (SQLException ex) {
            System.out.println("Connection to DB failed " + ex.getMessage());
        }
        return conn;
    }

}
