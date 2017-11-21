package com.whu;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * 这个版本针对上一个版本改进；
 * 控制上传文件存放的目录；
 * 防止文件覆盖，产生唯一文件名
 * 防止一个目录底下出现太多文件，用hash算法发散存储
 * 限制文件上传大小限制
 * 限制上传文件的类型
 */
@WebServlet(name = "HandleUploadServletV2",urlPatterns = {"/handleUploadServletV2"})
public class HandleUploadServletV2 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
        File storageDirectory = new File(savePath);
        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs();
        }
        String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
        File tempDirectory = new File(tempPath);
        if (!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }

        String message = "";
        try {
            //创建工厂
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            //设置工厂缓冲区,默认是10kb
            diskFileItemFactory.setSizeThreshold(1024*1024 * 10);
            diskFileItemFactory.setRepository(tempDirectory);
            //创建文件上传解析器
            ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
            fileUpload.setProgressListener(new ProgressListener() {
                @Override
                public void update(long l, long l1, int i) {
                    System.out.println("文件大小为：" + l1 + ",当前已处理" + l);
                }
            });
            //解决中文乱码问题
            fileUpload.setHeaderEncoding("utf-8");
            //判断提交来的请求是否是上传表单的数据
            if (!ServletFileUpload.isMultipartContent(request)) {
                return;
            }
            fileUpload.setFileSizeMax(1024 * 1024 * 10);
            fileUpload.setSizeMax(1024 * 1024 * 100);
            List<FileItem> list = fileUpload.parseRequest(request);
            if (list != null && list.size() != 0) {
                for (FileItem fileItem : list) {
                    if (fileItem.isFormField()) {
                        //如果不是上传文件的input，按照别的方式处理
                        String name = fileItem.getFieldName();
                        String value = fileItem.getString("UTF-8");
                        System.out.println(name + "=" + value);
                    } else {
                        //上传的数据是文件
                        String fileName = fileItem.getName();
                        System.out.println(fileItem);
                        //多个上传文件input时，有的控件可能没有进行上传，所以进行判断处理。
                        if (fileName != null && fileName.trim().length() != 0) {
                            //获取真正的文件名；
                            //a\b\c.txt
                            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                            String fileExtname = fileName.substring(fileName.lastIndexOf("." )+1, fileName.length());
                            //通过拓展名判断上传的文件类型是否合法
                            System.out.println("上传文件拓展名是: " + fileExtname);
                            //保存到服务器上
                            //这个地方需要产生唯一的文件名
                            String uniqueFilename = generateFilename(fileName);
                            String realSavePath = generatePath(uniqueFilename, savePath);
                            File tmp = new File(realSavePath + File.separator + uniqueFilename);
                            fileItem.write(tmp);
                            fileItem.delete();
                            message = "上传成功" + tmp;
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("message", message);
        request.getRequestDispatcher("/message.jsp").forward(request, response);
    }

    private String generatePath(String fileName, String savePath) {
        int hashCode = fileName.hashCode();
        int dir1 = hashCode & 0xf;
        int dir2 = (hashCode & 0xf0) >> 4;
        String dir = savePath + "\\" + dir1 + "\\" + dir2;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    private String generateFilename(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
