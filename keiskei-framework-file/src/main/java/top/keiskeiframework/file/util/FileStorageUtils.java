package top.keiskeiframework.file.util;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.file.dto.MultiFileInfo;

import java.io.IOException;

/**
 * <p>
 *
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/11/1 21:34
 */
public class FileStorageUtils {


    /**
     * 获取文件MD5名称, 降低文件重复
     *
     * @param fileInfo 文件信息
     * @return .
     */
    public static String getFileName(MultiFileInfo fileInfo) throws IOException {
        String fileName;
        if (StringUtils.isEmpty(fileName = fileInfo.getName())) {
            fileName = fileInfo.getFile().getOriginalFilename();
        }
        if (!fileInfo.getHashName()) {
            return fileName;
        }
        String md5;
        if (StringUtils.isEmpty(md5 = fileInfo.getMd5())) {
            md5 = DigestUtils.md5DigestAsHex(fileInfo.getFile().getInputStream());
        }
        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(md5)) {
            throw new RuntimeException("fileName get error!");
        }
        return getFileName(fileName, md5);
    }

    /**
     * 获取文件名称
     *
     * @param fileName 原文件名称
     * @param md5      文件MD5
     * @return .
     */
    public static String getFileName(String fileName, String md5) {
        return fileName.replaceAll("[\\s\\S]+\\.(.*?)", md5 + ".$1").toLowerCase();
    }

    /**
     * 获取文件大小
     *
     * @param fileInfo 文件信息
     * @return long
     */
    public static long getFileSize(MultiFileInfo fileInfo) {
        long size;
        if (fileInfo.getSize() != null) {
            size = fileInfo.getSize();
        } else {
            size = fileInfo.getFile().getSize();
        }
        return size;
    }

    public static String getFileSuffix(MultiFileInfo fileInfo) {
        String fileName = fileInfo.getName();
        if (StringUtils.isEmpty(fileName)) {
            fileName = fileInfo.getFile().getOriginalFilename();
        } if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("file is null");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

//    public static boolean validFileType(MultiFileInfo fileInfo)  {
//
//        String fileSuffix = getFileSuffix(fileInfo);
//        try {
//            String fileMagicNumber = FileTypeEnum.get(fileSuffix);
//            if (!StringUtils.isEmpty(fileMagicNumber)) {
//                InputStream is = fileInfo.getFile().getInputStream();
//                if (is.available() > 0) {
//                    byte[] b = new byte[fileMagicNumber.length() / 2];
//                    is.read(b);
//                    if (getHex(b).equalsIgnoreCase(fileMagicNumber)) {
//                        return true;
//                    } else {
//                        System.out.println(fileSuffix + " : " + getHex(b));
//                    }
//                }
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    /**
     * 获取16进制表示的魔数
     *
     * @param data 字节数组形式的文件数据
     * @return .
     */
    public static String getHex(byte[] data) {
        //提取文件的魔数
        StringBuilder magicNumber = new StringBuilder();
        //一个字节对应魔数的两位
        for (byte b : data) {
            magicNumber.append(Integer.toHexString(b >> 4 & 0xF));
            magicNumber.append(Integer.toHexString(b & 0xF));
        }

        return magicNumber.toString().toUpperCase();
    }
}
