/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import msg.DbMasterLocal;

/**
 *
 * @author User
 */
@WebServlet(name = "Chooser", urlPatterns = {"/Chooser"})
public class Chooser extends HttpServlet {

    @EJB
    private DbMasterLocal dbMaster;

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private QueueConnectionFactory factory;

    @Resource(lookup = "jms/MessageQueue")
    private Queue msgQ;

    @Resource(lookup = "jms/IntegerQueue")
    private Queue intQ;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        if (request.getParameter("list") != null) {
            ArrayList<String> messages = dbMaster.getMessageList();
            request.setAttribute("list", messages);
            request.getRequestDispatcher("index.jsp").forward(request, response);

        } else if (request.getParameter("send") != null) {
            String info = request.getParameter("info");
            if (info == null || info.trim().isEmpty()) {
                String msg = "Empty message ";
                request.setAttribute("msg", msg);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
            int number = 0;
            try {
                number = Integer.parseInt(info);
                sendObjectMessage(number);
                String msg = "Send num " + number;
                request.setAttribute("msg", msg);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (NumberFormatException ex) {
                sendTextMessage(info);
                String msg = "Send text " + info;
                request.setAttribute("msg", msg);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } else if (request.getParameter("send") != null) {
            int sum = dbMaster.getTotal();
            String msg = "Sum in table numbers " + sum;
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }else if (request.getParameter("clearMsg") != null) {
            dbMaster.deleteMessages();
            String msg = "All mesages are deleted";
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }else if (request.getParameter("clearNum") != null) {
            dbMaster.deleteNumbers();
            String msg = "All numbers are deleted";
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }else if (request.getParameter("sum") != null) {
            
            String msg = "SUM " + dbMaster.getTotal();
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }

        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Chooser</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Chooser at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void sendObjectMessage(int number) {
        System.out.println("Sending number " + number + " factory = " + factory);
        try {
            QueueConnection conn = factory.createQueueConnection();
            QueueSession ses = conn.createQueueSession(true, 0);
            QueueSender sender = ses.createSender(intQ);
            ObjectMessage om = ses.createObjectMessage(number);
            sender.send(om);

        } catch (JMSException ex) {
            System.out.println("Error sender.send(om) number " + ex.getMessage());
        }
    }

    private void sendTextMessage(String info) {
        System.out.println("Sending text " + info + " factory = " + factory);
        try {
            QueueConnection conn = factory.createQueueConnection();
            QueueSession ses = conn.createQueueSession(true, 0);
            QueueSender sender = ses.createSender(msgQ);
            TextMessage tm = ses.createTextMessage(info);
            sender.send(tm);

        } catch (JMSException ex) {
            System.out.println("Error sender.send(tm) text " + ex.getMessage());
        }
    }

}
