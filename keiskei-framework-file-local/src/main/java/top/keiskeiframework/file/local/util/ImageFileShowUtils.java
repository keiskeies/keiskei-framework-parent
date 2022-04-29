package top.keiskeiframework.file.local.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.file.local.process.ImageProcess;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/12 10:16
 */
public class ImageFileShowUtils {

    public static final Pattern PROCESS_PARAMS_PATTERN = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
    public static final String TEMP_SUFFIX = ".temp";

    public static void show(String path, String fileName, String process, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.hasText(process)) {
            String[] processes = process.split("/");
            if (processes.length > 1) {


                Matcher matcher = PROCESS_PARAMS_PATTERN.matcher(request.getQueryString());
                String params = matcher.replaceAll("");
                File tempFile = new File(path, fileName + params + TEMP_SUFFIX);
                if (tempFile.exists() && tempFile.length() > 0) {
                    show(path, fileName + params + TEMP_SUFFIX, request, response);
                    return;
                } else {
                    ImageProcess imageProcess = new ImageProcess(processes);
                    try {
                        String contentType = null;
                        Thumbnails.Builder<File> thumbnails = Thumbnails.of(path + fileName);
                        // 获取文件原始宽高
                        BufferedImage bi = Thumbnails.of(path + fileName).scale(1D).asBufferedImage();

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
                            contentType = "image/" + imageProcess.getFormat();

                        }
                        if (null != imageProcess.getRotate()) {
                            // 图片旋转
                            thumbnails.rotate(imageProcess.getRotate());
                        }

                        if (!StringUtils.hasText(contentType)) {
                            contentType = "image/jpeg";
                        }
                        response.setContentType(contentType);
                        OutputStream os = response.getOutputStream();
                        thumbnails.toOutputStream(os);

                        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                            thumbnails.toOutputStream(fos);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

            }
        }
        show(path, fileName, request, response);
    }

    public static void show(String path, String fileName, HttpServletRequest request, HttpServletResponse response) {
        File file = new File(path, fileName);
        try {
            String contentType = request.getServletContext().getMimeType(fileName);
            if (!StringUtils.hasText(contentType)) {
                contentType = Files.probeContentType(Paths.get(path + fileName));
            }
            if (!StringUtils.hasText(contentType)) {
                contentType = "image/jpeg";
            }
            response.setContentType(contentType);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
