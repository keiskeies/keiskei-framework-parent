package top.keiskeiframework.file.local.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.file.local.process.FileProcess;
import top.keiskeiframework.file.local.process.ImageProcess;
import top.keiskeiframework.file.local.process.VideoProcess;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 16:10
 */
public class FileShowUtils {

    private final static String IMAGE_TYPE = "image";
    private final static String VIDEO_TYPE = "video";

    public static void show(String fileName, String process, HttpServletResponse response) throws IOException {

        String contentType = Files.probeContentType(Paths.get(fileName));
        response.setContentType(contentType);
        FileProcess fileProcess = getProcess(process);

        if (null == fileProcess) {
            showDefault(fileName, response);
        } else {
            if (contentType.contains(IMAGE_TYPE) && null != fileProcess.getImageProcess()) {
                image2Image(fileName, fileProcess.getImageProcess(), response);
            } else if (contentType.contains(VIDEO_TYPE) && null != fileProcess.getVideoProcess()) {
                video2Image(fileName, fileProcess.getVideoProcess(), response);
            } else {
                showDefault(fileName, response);
            }
        }
    }

    public static void image2Image(String fileName, ImageProcess imageProcess, HttpServletResponse response) throws IOException {

        Thumbnails.Builder<File> thumbnails = Thumbnails.of(fileName);
        // 获取文件原始宽高
        BufferedImage bi = Thumbnails.of(fileName).scale(1D).asBufferedImage();
        int srcImgWidth = bi.getWidth();
        int srcImgHeight = bi.getHeight();

        // 图片处理
        if (null != imageProcess.getResize()) {
            imageProcess.getResize().resize(thumbnails, srcImgWidth, srcImgHeight);
        } else {
            thumbnails.scale(1D);
        }
        if (null != imageProcess.getQuality()) {
            // 图片质量压缩
            imageProcess.getQuality().quality(thumbnails, srcImgWidth, srcImgHeight);
        }
        if (null != imageProcess.getCircle()) {
            // 图片裁剪压缩
            imageProcess.getCircle().circle(thumbnails, srcImgWidth, srcImgHeight);
        }
        if (null != imageProcess.getIndexcrop()) {
            // 图片分块裁剪
            imageProcess.getIndexcrop().indexcrop(thumbnails, srcImgWidth, srcImgHeight);
        }
        if (null != imageProcess.getWatermark()) {
            // 图片水印
            imageProcess.getWatermark().watermark(thumbnails, srcImgWidth, srcImgHeight);
        }
        if (null != imageProcess.getFormat()) {
            // 图片格式转换
            thumbnails.outputFormat(imageProcess.getFormat());
            response.setContentType("image/" + imageProcess.getFormat());
        }
        if (null != imageProcess.getRotate()) {
            // 图片旋转
            thumbnails.rotate(imageProcess.getRotate());
        }

        OutputStream os = response.getOutputStream();
        thumbnails.toOutputStream(os);
    }

    public static void video2Image(String fileName, VideoProcess videoProcess, HttpServletResponse response) throws IOException {
        showDefault(fileName, response);
    }

    public static void showDefault(String fileName, HttpServletResponse response) throws IOException {
        File file = new File(fileName);
        response.setContentType(Files.probeContentType(Paths.get(fileName)));
        try (
                InputStream is = new FileInputStream(file);
                ServletOutputStream os = response.getOutputStream()
        ) {
            int bufferLength;
            byte[] buffer = new byte[StreamUtils.BUFFER_SIZE];
            while ((bufferLength = is.read(buffer)) != -1) {
                os.write(buffer, 0, bufferLength);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.reset();
            response.setContentType("application/json;charset=utf-8");
        }
    }

    private static FileProcess getProcess(String process) {
        if (StringUtils.isEmpty(process)) {
            return null;
        }
        String[] processes = process.split("/");
        if (processes.length <= 1) {
            return null;
        }
        if (IMAGE_TYPE.equals(processes[0])) {
            FileProcess fileProcess = new FileProcess();
            fileProcess.setImageProcess(new ImageProcess(processes));
            return fileProcess;
        } else if (VIDEO_TYPE.equals(processes[0])) {
            FileProcess fileProcess = new FileProcess();
            fileProcess.setVideoProcess(new VideoProcess(processes));
            return fileProcess;
        } else {
            return null;
        }
    }


}
