package top.keiskeiframework.file.local.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import top.keiskeiframework.file.local.process.VideoProcess;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
 * @since 2022/4/12 10:37
 */
@Slf4j
public class VideoFileShowUtils {

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
                    ImageFileShowUtils.show(path, fileName + params + TEMP_SUFFIX, request, response);
                    return;
                } else {
                    VideoProcess videoProcess = new VideoProcess(processes);
                    if (null != videoProcess.getSnapshot()) {
                        try {
                            BufferedImage bufferedImage = videoProcess.getSnapshot().snapshot(new File(path + fileName));
                            String f = StringUtils.hasText(videoProcess.getSnapshot().getF()) ? videoProcess.getSnapshot().getF() : "jpg";
                            response.setContentType("image/" + f);
                            OutputStream os = response.getOutputStream();
                            ImageIO.write(bufferedImage, f, os);
                            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                                ImageIO.write(bufferedImage, f, fos);
                            }
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        }
        show(path, fileName, request, response);
    }

    public static void show(String path, String fileName, HttpServletRequest request, HttpServletResponse response) {
        fileName = path + fileName;
        File file = new File(fileName);

        String range = request.getHeader("Range");
        log.info("current request rang:" + range);
        //开始下载位置
        long startByte = 0;
        //结束下载位置
        long endByte = file.length() - 1;
        log.info("文件开始位置：{}，文件结束位置：{}，文件总长度：{}", startByte, endByte, file.length());

        //有range的话
        if (range != null && range.contains("bytes=") && range.contains("-")) {
            range = range.substring(range.lastIndexOf("=") + 1).trim();
            String[] ranges = range.split("-");
            try {
                //判断range的类型
                if (ranges.length == 1) {
                    //类型一：bytes=-2343
                    if (range.startsWith("-")) {
                        endByte = Long.parseLong(ranges[0]);
                    }
                    //类型二：bytes=2343-
                    else if (range.endsWith("-")) {
                        startByte = Long.parseLong(ranges[0]);
                    }
                }
                //类型三：bytes=22-2343
                else if (ranges.length == 2) {
                    startByte = Long.parseLong(ranges[0]);
                    endByte = Long.parseLong(ranges[1]);
                }

            } catch (NumberFormatException e) {
                startByte = 0;
                endByte = file.length() - 1;
                log.error("Range Occur Error,Message:{}", e.getLocalizedMessage());
            }
        }

        //要下载的长度
        long contentLength = endByte - startByte + 1;

        // 解决下载文件时文件名乱码问题
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        fileName = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);

        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(fileName);
            if (!StringUtils.hasText(contentType)) {
                contentType = Files.probeContentType(Paths.get(path + fileName));
            }
            response.setContentType(contentType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Disposition", "inline;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(contentLength));
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());


        //已传送数据大小
        long transmitted = 0;
        try (
                BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        ) {


            byte[] buff = new byte[4096];
            int len = 0;
            randomAccessFile.seek(startByte);
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
                transmitted += len;
            }
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                outputStream.write(buff, 0, len);
                transmitted += len;
            }

            outputStream.flush();
            response.flushBuffer();
            randomAccessFile.close();
            log.info("下载完毕：" + startByte + "-" + endByte + "：" + transmitted);
        } catch (IOException e) {
            log.warn("用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
            log.error("用户下载IO异常，Message：{}", e.getLocalizedMessage());
        }
    }
}
