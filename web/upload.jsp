<%--
  User: 马平凡 Date: 2017/11/20 Time: 15:48
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<html>
<head>
    <title>文件上传</title>
</head>
<body>
<form method="post" enctype="multipart/form-data" action="uploadServlet">
    <input type="file" name="file">
    <input type="submit" value="提交">
</form>
</body>
</html>
