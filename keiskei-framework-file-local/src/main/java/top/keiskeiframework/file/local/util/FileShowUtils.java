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
        // ????????????????????????
        BufferedImage bi = Thumbnails.of(fileName).scale(1D).asBufferedImage();
        int srcImgWidth = bi.getWidth();
        int srcImgHeight = bi.getHeight();

        // ????????????
        if (null != imageProcess.getResize()) {
            imageProcess.getResize().resize(thumbnails, srcImgWidth, srcImgHeight);
        } else {
            thumbnails.scale(1D);
        }
        if (null != imageProcess.getQuality()) {
            // ??????????????????
            imageProcess.getQuality().quality(thumbnails, srcImgWidth, srcImgHeight);
        }
        if (null != imageProcess.getCircle()) {
            // ??????????????????
            imageProcess.getCircle().circle(thumbnails, srcImgWidth, srcImgHeight);
        }
        if (null != imageProcess.getIndexcrop()) {
            // ??????????????????
            imageProcess.getIndexcrop().indexcrop(thumbnails, srcImgWidth, srcImgHeight);
        }
        if (null != imageProcess.getWatermark()) {
            // ????????????
            imageProcess.getWatermark().watermark(thumbnails, srcImgWidth, srcImgHeight);
        }
        if (null != imageProcess.getFormat()) {
            // ??????????????????
            thumbnails.outputFormat(imageProcess.getFormat());
            response.setContentType("image/" + imageProcess.getFormat());
        }
        if (null != imageProcess.getRotate()) {
            // ????????????
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
        //??????????????????
        long startByte = 0;
        //??????????????????
        long endByte = file.length() - 1;
        log.info("?????????????????????{}????????????????????????{}?????????????????????{}", startByte, endByte, file.length());

        //???range??????
        if (range != null && range.contains("bytes=") && range.contains("-")) {
            range = range.substring(range.lastIndexOf("=") + 1).trim();
            String[] ranges = range.split("-");
            try {
                //??????range?????????
                if (ranges.length == 1) {
                    //????????????bytes=-2343
                    if (range.startsWith("-")) {
                        endByte = Long.parseLong(ranges[0]);
                    }
                    //????????????bytes=2343-
                    else if (range.endsWith("-")) {
                        startByte = Long.parseLong(ranges[0]);
                    }
                }
                //????????????bytes=22-2343
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


        //??????????????????
        long contentLength = endByte - startByte + 1;

        // ??????????????????????????????????????????
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        fileName = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);

        //?????????????????????     
        //????????????????????????????????????????????????
        response.setHeader("Accept-Ranges", "bytes");
        //http???????????????206???????????????????????????
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType(contentType);
        response.setHeader("Content-Type", contentType);
        //inline??????????????????????????????attachment???????????????fileName????????????????????????
        response.setHeader("Content-Disposition", "inline;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(contentLength));
        // Content-Range???????????????[????????????????????????]-[????????????]/[???????????????]
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());

        BufferedOutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;
        //?????????????????????
        long transmitted = 0;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[4096];
            int len = 0;
            randomAccessFile.seek(startByte);
            //warning?????????????????????????????????4096???buff???length??????byte???????????????(transmitted + len) <= contentLength???????????????
            //?????????????????????randomAccessFile?????????????????????????????????;
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                outputStream.write(buff,  0, len);
                transmitted += len;
            }
            //????????????buff.length??????
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                outputStream.write(buff, 0, len);
                transmitted += len;
            }

            outputStream.flush();
            response.flushBuffer();
            randomAccessFile.close();
            log.info("???????????????" + startByte + "-" + endByte + "???" + transmitted);
        } catch (ClientAbortException e) {
            log.warn("?????????????????????" + startByte + "-" + endByte + "???" + transmitted);
            //???????????????????????????????????????
        } catch (IOException e) {
            log.error("????????????IO?????????Message???{}", e.getLocalizedMessage());
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
