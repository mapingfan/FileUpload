<%@page pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<%
  request.setCharacterEncoding("utf-8");
  String message = (String) request.getAttribute("message");
  if (message != null && message.trim().length() != 0) {
      out.println(message);
  }
  //out.println("hh");
%>