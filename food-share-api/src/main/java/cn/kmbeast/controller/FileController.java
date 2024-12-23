package cn.kmbeast.controller;

import cn.kmbeast.utils.IdFactoryUtil;
import cn.kmbeast.utils.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件前端控制器
 *
 * @since 2024-03-22
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${my-server.api-context-path}")
    private String API;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 文件上传
     *
     * @param multipartFile 文件流
     * @return 响应
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws UnknownHostException {
        String uuid = IdFactoryUtil.getFileId();
        String fileName = uuid + multipartFile.getOriginalFilename();
        Map<String, Object> rep = new HashMap<>();
        // 生成链接 , 本地的话使用localhost就可以
        // String ipAddr = RootUtils.getIpAddr();
        String ipAddr = "localhost";
        // 生成前缀链接
        String perUrl = "http://" + ipAddr + ":" + serverPort + "/";
        try {
            if (uploadFile(multipartFile, fileName)) {
                rep.put("code", 200);
                rep.put("data", perUrl + API + "/file/getFile?fileName=" + fileName);
                return rep;
            }
        } catch (IOException e) {
            rep.put("code", 400);
            rep.put("msg", "文件上传异常");
            return rep;
        }
        rep.put("code", 400);
        rep.put("msg", "文件上传异常");
        return rep;
    }

    /**
     * 上传文件
     *
     * @param multipartFile 文件流
     * @param fileName      文件名
     * @return boolean
     * @throws IOException 异常
     */
    public boolean uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        return fileName(multipartFile, fileName);
    }

    public static boolean fileName(MultipartFile multipartFile, String fileName) throws IOException {
        File fileDir = new File(PathUtils.getClassLoadRootPath() + "/pic");
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                return false;
            }
        }
        File file = new File(fileDir.getAbsolutePath() + "/" + fileName);
        if (file.exists()) {
            if (!file.delete()) {
                return false;
            }
        }
        if (file.createNewFile()) {
            multipartFile.transferTo(file);
            return true;
        }
        return false;
    }

    /**
     * 查看图片资源
     *
     * @param imageName 文件名
     * @param response  响应
     * @throws IOException 异常
     */
    @GetMapping("/getFile")
    public void getImage(@RequestParam("fileName") String imageName,
                         HttpServletResponse response) throws IOException {
        File fileDir = new File(PathUtils.getClassLoadRootPath() + "/pic");
        File image = new File(fileDir.getAbsolutePath() + "/" + imageName);
        if (image.exists()) {
            FileInputStream fileInputStream = new FileInputStream(image);
            byte[] bytes = new byte[fileInputStream.available()];
            if (fileInputStream.read(bytes) > 0) {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(bytes);
                outputStream.close();
            }
            fileInputStream.close();
        }
    }

}

