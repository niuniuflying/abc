package com.nac.abc.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtils {


    public static String uploadImage(MultipartFile file, String uploadDirectory) {
        // 获取文件名称
        String originalFilename = file.getOriginalFilename();
        System.out.println("原文件名称：" + originalFilename);

        // 检查文件扩展名是否为.jpg或.png（不区分大小写）
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        if ("jpg".equalsIgnoreCase(fileExtension) || "png".equalsIgnoreCase(fileExtension) || "webp".equalsIgnoreCase(fileExtension)) {
            try {
                // 设置新名称
//                String newFilename = UUID.randomUUID() + "." + fileExtension;
                String newFilename = UUIDUtil.generateRandomUUID() + "." + fileExtension;

                System.out.println("新文件名称:" + newFilename);

                // 保存文件路径
                String filePath = uploadDirectory + newFilename;
                System.out.println("当前保存路径:" + filePath);

                // 使用Java NIO传输数据，使用Files.copy更简洁
                Files.copy(file.getInputStream(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);

                System.out.println("文件保存成功");
                return newFilename;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件保存失败");
                return null; // 或者抛出异常，具体取决于您的需求
            }
        } else {
            System.out.println("不支持的文件类型");
            return null; // 或者抛出异常，具体取决于您的需求
        }
    }


    public static boolean showImage(String url,String fileName, HttpServletResponse response) throws Exception {
        String path=url+fileName;
        File f=new File(path);
        if (f.exists()) {
            FileCopyUtils.copy(Files.newInputStream(Paths.get(path)),response.getOutputStream());
            return true;
        }else {
            return false;
        }
    }

    public static boolean deleteImage(String url,String fileUrl) throws IOException {
        System.out.println("删除文件名称:"+fileUrl);
        String s=url+fileUrl;
        File f=new File(s);
        if (f.exists()){
            Path path= Paths.get(url+fileUrl);
            boolean b = Files.deleteIfExists(path);
            System.out.println("是否删除成功:"+b);
            return b;
        }else {
            return false;
        }
    }
}
