package com.whu;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet(name = "HandleUploadServlet",urlPatterns = {"/handleUploadServletV1"})
public class HandleUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String savePath =  this.getServletContext().getRealPath("/WEB-INF/upload");
        response.setCharacterEncoding("utf-8");
        File storeDir = new File(savePath);
        //if the directory does not exist, create it;
        if (!storeDir.exists()) {
            storeDir.mkdirs();
        }
        String message = "";
        //使用Apache组件进行上传
        try {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            //创建文件解析器
            ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);
            //解决上传文件中文文件名乱码
            upload.setHeaderEncoding("UTF-8");
            if (!ServletFileUpload.isMultipartContent(request)) {
                //如果提交上来的数据不是多媒体数据，那么按照request.getParameter()获取数据.
                return;
            }
            //使用解析器解析请求。表单中的每个input空间都被解析成一个item，即items列表中的一项；
            List<FileItem> items = upload.parseRequest(request);
            if (items != null && items.size() != 0) {
                for (FileItem fileItem : items) {
                    //判断是上传文件的控件还是简单的输入表单
                    if (fileItem.isFormField()) {
                        String name = fileItem.getFieldName();//得到控件名
                        //解决普通控件输入汉字乱码问题
                        String value = fileItem.getString("UTF-8");
                    } else {
                        //此时的fileItem中封装的是上传文件；
                        String fileName = fileItem.getName();
                        System.out.println(fileItem);
                        if (fileName == null || fileName.trim().length() == 0) {
                            //上传文件有问题
                            return;
                        } else {
                            //对形如b\abc.txt的文件，截取出真实的文件名
                            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                            InputStream in = fileItem.getInputStream();
                            FileOutputStream fout = new FileOutputStream(savePath + File.separator + fileName);
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            while ((len = in.read(buffer)) > 0) {
                                fout.write(buffer, 0, len);
                            }
                            in.close();
                            fout.close();
                            fileItem.delete();
                            message = "上传成功";
                        }
                    }
                }
            }

        } catch (FileUploadException e) {
            message = "文件上传失败";
            e.printStackTrace();
        }
        request.setAttribute("message", message+savePath);
      request.getRequestDispatcher("/message.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
