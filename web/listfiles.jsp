<%@ page import="java.util.Map" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<%--
  User: 马平凡 Date: 2017/11/21 Time: 16:44
--%>
<%
    Map<String, String> fileNameMap = (Map<String, String>) request.getAttribute("fileNameMap");
    if (fileNameMap != null) {
        for (String s : fileNameMap.keySet()) {
%>
            文件名：<%=fileNameMap.get(s)%>&nbsp;下载链接<a href="<%=s%>">下载</a><br>
<%
        }
    }
%>