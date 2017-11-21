# JavaWeb文件上传

Java文件上传学习

---
最近学习遇到了文件上传到服务器，以前遇到过，但是没有深入学习过，借此机会学习总结。
在`JavaWeb`中要上传文件到服务器，`Servlet3.0`之前需要`Apache`的第三方工具包：`commons-fileupload、commons-io`两个工具包。但是从`Servlet3.0`以来，上传不需要借助`Apache`的工具包就可以实现上传。
对于传统的上传，主要涉及如下几个流程

- 创建`DiskFileItemFactory`工厂
- 创建请求解析器`ServletFileUpload`
- 每个上传数据被封装为`FileItem`。

具体的一些细节控制，诸如上传文件大小，上传到哪里，参见代码即可。网络上有的代码仍然是通过流的读写来传递文件，但其实`FileItem`的对象封装了`write`方法，可以直接把请求的数据写入到指定的文件中。通过流的来回转换，其实是有点繁琐的，代码看起来也是很丑陋。毕竟新手还会忘记关闭流。
对于`Servlet3.0`之后的版本，会用到一个新的类`Part`.通过这个类，极大简化了文件上传。
```Java
protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        Part part = request.getPart("file"); //直接获得文件
        System.out.println("文件类型: " + part.getContentType());
        part.write(part.getSubmittedFileName()); //直接写入文件
        PrintWriter out = response.getWriter();
        out.println("上传成功");
        out.flush();
        out.close();
    }
```
总结到此结束，其实这些代码的逻辑都很简单，跟写`JDBC`的连接差不多，关键是常用。不常用可能下一次用到还需要复习。




