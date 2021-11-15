<%-- 
    Document   : index
    Created on : Mar 27, 2021, 12:24:35 PM
    Author     : User
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div><a href="Test">Test servlet</a></div>
        <hr>
        
        <form action="Chooser">
            <input type="text" name="info" value="" size="30" />
            <input type="submit" value="Send" name="send" />
            <hr>
            <input type="submit" value="Get msg list" name="list" />
            <input type="submit" value="Get sum" name="sum" />
            <hr>
            <input type="submit" value="Del msg" name="clearMsg" />
            <input type="submit" value="Del num" name="clearNum" />
        </form>
       
        <% String msg = (String)request.getAttribute("msg"); %>
        <h1><%=(msg==null)?"":msg%></h1>
        
        <% ArrayList <String> list = (ArrayList)request.getAttribute("list");
        if(list != null){%>
        <h2> message list  </h2><ul>
            <% for(String s : list) {%>
            <%= "<li>" + s + "</li>"%>
            <%}}%>
            </ul>
            
        
    </body>
</html>
