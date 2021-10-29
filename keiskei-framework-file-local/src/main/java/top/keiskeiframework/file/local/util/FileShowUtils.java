package top.keiskeiframework.file.local.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.file.local.process.FileProcess;
import top.keiskeiframework.file.local.process.ImageProcess;
import top.keiskeiframework.file.local.process.VideoProcess;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 16:10
 */
@Slf4j
public class FileShowUtils {

    private final static String IMAGE_TYPE = "image";
    private final static String VIDEO_TYPE = "video";

    public static void show(String fileName, String process, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String contentType = Files.probeContentType(Paths.get(fileName));
        response.setContentType(contentType);
        FileProcess fileProcess = getProcess(process);

        if (null == fileProcess) {
            showDefault(fileName, request, response);
        } else {
            if (contentType.contains(IMAGE_TYPE) && null != fileProcess.getImageProcess()) {
                image2Image(fileName, fileProcess.getImageProcess(), request, response);
            } else if (contentType.contains(VIDEO_TYPE) && null != fileProcess.getVideoProcess()) {
                video2Image(fileName, fileProcess.getVideoProcess(), request, response);
            } else {
                showDefault(fileName, request, response);
            }
        }
    }

    public static void image2Image(String fileName, ImageProcess imageProcess, HttpServletRequest request, HttpServletResponse response) throws IOException {

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

    public static void video2Image(String fileName, VideoProcess videoProcess, HttpServletRequest request, HttpServletResponse response) throws IOException {
        showDefault(fileName, request, response);
    }


    public static void showDefault(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(fileName);
        String contentType = request.getServletContext().getMimeType(fileName); // Files.probeContentType(Paths.get(fileName));
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
                log.error("Range Occur Error,Message:{}",e.getLocalizedMessage());
            }
        }


        //要下载的长度
        long contentLength = endByte - startByte + 1;

        // 解决下载文件时文件名乱码问题
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        fileName = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);

        //各种响应头设置     
        //支持断点续传，获取部分字节内容：
        response.setHeader("Accept-Ranges", "bytes");
        //http状态码要为206：表示获取部分内容
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType(contentType);
        response.setHeader("Content-Type", contentType);
        //inline表示浏览器直接使用，attachment表示下载，fileName表示下载的文件名
        response.setHeader("Content-Disposition", "inline;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(contentLength));
        // Content-Range，格式为：[要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());

        BufferedOutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;
        //已传送数据大小
        long transmitted = 0;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[4096];
            int len = 0;
            randomAccessFile.seek(startByte);
            //warning：判断是否到了最后不足4096（buff的length）个byte这个逻辑（(transmitted + len) <= contentLength）要放前面
            //不然会会先读取randomAccessFile，造成后面读取位置出错;
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                outputStream.write(buff,  0, len);
                transmitted += len;
            }
            //处理不足buff.length部分
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                outputStream.write(buff, 0, len);
                transmitted += len;
            }

            outputStream.flush();
            response.flushBuffer();
            randomAccessFile.close();
            log.info("下载完毕：" + startByte + "-" + endByte + "：" + transmitted);
        } catch (ClientAbortException e) {
            log.warn("用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
            //捕获此异常表示拥护停止下载
        } catch (IOException e) {
            log.error("用户下载IO异常，Message：{}", e.getLocalizedMessage());
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
