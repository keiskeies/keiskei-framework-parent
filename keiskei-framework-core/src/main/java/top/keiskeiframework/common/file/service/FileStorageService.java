package top.keiskeiframework.common.file.service;

import top.keiskeiframework.common.file.dto.MultiFileInfo;
import top.keiskeiframework.common.file.dto.FileInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * 文件仓库工具类
 *
 * @author 陈加敏
 * @since 2019/7/23 16:36
 */
public interface FileStorageService {


    String PART_ETAG_FORMAT = "PART_ETAG::%s";
    String UPLOAD_ID_FORMAT = "UPLOAD_ID::%s";
    String UPLOAD_PROGRESS_FORMAT = "UPLOAD_PROGRESS::%s";


    /**
     * 文件流上传
     *
     * @param fileInfo 文件信息
     * @return 文件链接
     */
    FileInfo upload(MultiFileInfo fileInfo);

    /**
     * 带进度条上传
     *
     * @param fileInfo 文件信息
     * @return 文件文件上传信息
     */
    FileInfo uploadWithProgress(MultiFileInfo fileInfo);

    /**
     * 上传单个分片
     *
     * @param fileInfo 文件分片信息
     */
    void uploadPart(MultiFileInfo fileInfo);

    /**
     * 合并文件分片
     *
     * @param fileInfo 文件信息
     * @return .
     */
    FileInfo mergingChunks(MultiFileInfo fileInfo);

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名
     * @return boolean
     */
    FileInfo exist(String fileName);

    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    void delete(String fileName);

    /**
     * 批量删除文件
     *
     * @param fileNames 文件名集合
     */
    void deleteBatch(List<String> fileNames);

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @return 文件流
     */
    InputStream download(String fileName);

    /**
     * 文件预览
     *
     * @param fileName 文件名
     * @param process  预览条件
     * @param response  返回
     */
    void show(String fileName, String process, HttpServletResponse response);

}
