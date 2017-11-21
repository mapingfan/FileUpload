package com.whu;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Servlet3",urlPatterns = {"/listfileservlet"})
public class ListFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uploadFilePath = getServletContext().getRealPath("/WEB-INF/upload");
        //使用Map存储要下载的文件名
        Map<String, String> fileNameMap = new HashMap<>();
        listFile(new File(uploadFilePath), fileNameMap);
        request.setAttribute("fileNameMap", fileNameMap);
        request.getRequestDispatcher("/listfiles.jsp").forward(request, response);
    }

    private void listFile(File uploadFilePath, Map<String, String> fileNameMap) {
        //如果存在并且是目录，那么递归列出子文件
        if (uploadFilePath.exists() && uploadFilePath.isDirectory()) {
            File[] files = uploadFilePath.listFiles();
            for (File file : files) {
                listFile(file, fileNameMap);
            }
        } else {
            //如果是文件，那么加入到Map中
            String UUIDName = uploadFilePath.getName();
            String realName = UUIDName.substring(UUIDName.indexOf("_") + 1);
            fileNameMap.put(UUIDName, realName);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
