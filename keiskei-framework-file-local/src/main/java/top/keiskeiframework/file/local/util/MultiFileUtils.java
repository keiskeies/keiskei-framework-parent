package top.keiskeiframework.file.local.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import top.keiskeiframework.file.dto.MultiFileInfo;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.ReentrantLock;


/**
 * <p>
 * 文件上传工具类
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/9/24 17:16
 */
@Slf4j
public class MultiFileUtils {

    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    public static String upload(MultiFileInfo fileInfo, String path) {
        String fileName;
        try {
            fileName = FileStorageUtils.getFileName(fileInfo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("file's name get fail!");
        }
        File file = new File(path, fileName);
        try (FileOutputStream os = new FileOutputStream(file);
             FileInputStream is = (FileInputStream) fileInfo.getFile().getInputStream();
             FileChannel inChannel = is.getChannel();
             FileChannel outChannel = os.getChannel()) {

            outChannel.transferFrom(inChannel, 0, fileInfo.getFile().getSize());
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("file upload fail!");
        }
    }


    /**
     * 检验文件夹是否存在
     *
     * @param path 文件夹路径
     */
    public static void checkDir(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("dir make fail!");
            }
            log.info("dir {} make success!", path);
        }
    }

    public static String exitFile(String path, String fileName) {
        File pathFile = new File(path);
        String[] fileList = pathFile.list();
        if (null != fileList && fileList.length > 0) {
            for (String file : fileList) {
                File fileTemp = new File(path, file);
                if (fileTemp.isFile()) {
                    if (file.contains(fileName)) {
                        return fileTemp.getPath();
                    }
                } else {
                    String filepath;
                    if (null != (filepath = exitFile(fileTemp.getPath(), fileName))) {
                        return filepath;
                    }
                }
            }
        }
        return null;
    }


    public static synchronized void savePartFile(MultiFileInfo fileInfo, String tmpPath) throws Exception {
        String fileDirName = getTmpName(fileInfo, tmpPath);
        //禁用FileInfo.exists()类, 防止缓存导致并发问题
        File tempFile = new File(fileDirName);
        if (!(tempFile.exists() && tempFile.isFile())) {
            //上锁
            REENTRANT_LOCK.lock();
            try {
                if (!(tempFile.exists() && tempFile.isFile())) {
                    MultiFileUtils.readySpaceFile(fileInfo, tempFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                REENTRANT_LOCK.unlock();
            }
            //释放锁

        }
        tempFile = new File(fileDirName);
        MultiFileUtils.spaceFileWriter(tempFile, fileInfo);
    }


    /**
     * 合并分片文件
     *
     * @param fileInfo 文件信息
     * @param tmpPath  临时路径
     * @param path     真实路径
     * @return .
     */
    public static String mergingParts(MultiFileInfo fileInfo, String tmpPath, String path) {
        File tempFile = null;
        try {
            String fileDirName = getTmpName(fileInfo, tmpPath);
            tempFile = new File(fileDirName);
            if (tempFile.exists() && tempFile.isFile()) {
                String targetDirName = FileStorageUtils.getFileName(fileInfo);
                File targetFile = new File(path, targetDirName);
                if (tempFile.renameTo(targetFile)) {
                    return targetDirName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != tempFile) {
                if (tempFile.delete()) {
                    log.info("temp file {} delete success", tempFile.getName());
                }
            }
        }
        return null;
    }


    /**
     * 获取文件临时名称
     *
     * @param fileInfo 文件分片信息
     * @param tmpPath  临时目录
     * @return .
     */
    private static String getTmpName(MultiFileInfo fileInfo, String tmpPath) throws IOException {

        String id = fileInfo.getId();
        String fileMd5Name = FileStorageUtils.getFileName(fileInfo);
        return tmpPath + id + "_" + fileMd5Name + ".temp";
    }

    /**
     * 创建空目标文件
     *
     * @throws IOException .
     */
    private static void readySpaceFile(MultiFileInfo fileInfo, File tempFile) throws IOException {
        RandomAccessFile targetSpaceFile = new RandomAccessFile(tempFile, "rws");
        targetSpaceFile.setLength(fileInfo.getSize());
        targetSpaceFile.close();
    }

    /**
     * 向空文件写入二进制数据
     */
    private static void spaceFileWriter(File tempFile, MultiFileInfo fileInfo) {
        long startPointer = getFileWriterStartPointer(fileInfo.getFile(), fileInfo);

        try (
                FileChannel inChannel = ((FileInputStream) fileInfo.getFile().getInputStream()).getChannel();
                FileOutputStream out = new FileOutputStream(tempFile, true);
                FileChannel outChannel = out.getChannel()
        ) {

            outChannel.transferFrom(inChannel, startPointer, fileInfo.getFile().getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 计算指针开始位置
     *
     * @param fileInfo:分片实体类
     */
    private synchronized static Long getFileWriterStartPointer(MultipartFile file, MultiFileInfo fileInfo) {
        // TODO Auto-generated method stub
        long chunkSize = file.getSize();
        Integer currChunk = fileInfo.getChunk();
        Integer allChunks = fileInfo.getChunks();
        Long allSize = fileInfo.getSize();
        if (currChunk < (allChunks - 1)) {
            return chunkSize * currChunk;
        } else if (currChunk == (allChunks - 1)) {
            return allSize - chunkSize;
        } else {
            throw new RuntimeException("file part error!");
        }
    }


}
