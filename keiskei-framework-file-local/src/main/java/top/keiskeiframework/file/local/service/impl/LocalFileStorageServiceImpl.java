package top.keiskeiframework.file.local.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.cache.service.CacheStorageService;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.file.dto.FileInfo;
import top.keiskeiframework.file.dto.MultiFileInfo;
import top.keiskeiframework.file.enums.FileStorageExceptionEnum;
import top.keiskeiframework.file.local.config.FileLocalProperties;
import top.keiskeiframework.file.local.util.FileShowUtils;
import top.keiskeiframework.file.local.util.MultiFileUtils;
import top.keiskeiframework.file.service.FileStorageService;
import top.keiskeiframework.file.util.FileStorageUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * <p>
 * 本地文件服务类
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/12/9 22:06
 */
@Service
@Slf4j
public class LocalFileStorageServiceImpl implements FileStorageService {
    @Autowired
    private FileLocalProperties fileLocalProperties;
    @Autowired
    @Qualifier("cacheStorageServiceFile")
    private CacheStorageService cacheStorageService;


    @Override
    public FileInfo upload(MultiFileInfo fileInfo) {
        String fileName = MultiFileUtils.upload(fileInfo, fileLocalProperties.getPath());
        return getFileInfo(fileName);
    }

    @Override
    public FileInfo uploadWithProgress(MultiFileInfo fileInfo) {
        String fileSign;
        if (StringUtils.isEmpty(fileSign = fileInfo.getId())) {
            MultiFileUtils.upload(fileInfo, fileLocalProperties.getPath());
        }
        String fileName;
        try {
            //获取文件名称
            fileName = FileStorageUtils.getFileName(fileInfo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        }
        File file = new File(fileLocalProperties.getPath(), fileName);
        long size = FileStorageUtils.getFileSize(fileInfo);
        int progress = 0;
        try (FileOutputStream os = new FileOutputStream(file);
             InputStream is = fileInfo.getFile().getInputStream()) {
            int bufferLength;
            byte[] buffer = new byte[StreamUtils.BUFFER_SIZE];
            while ((bufferLength = is.read(buffer)) != -1) {
                os.write(buffer, 0, bufferLength);
                progress += bufferLength;
                cacheStorageService.save(String.format(FileStorageService.UPLOAD_PROGRESS_FORMAT, fileSign), (int) (progress * 100 / size));
            }
            return getFileInfo(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        }
    }

    @Override
    public void uploadPart(MultiFileInfo fileInfo) {
        try {
            //切片上传
            MultiFileUtils.savePartFile(fileInfo, fileLocalProperties.getTempPath());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        }
    }

    @Override
    public FileInfo mergingChunks(MultiFileInfo fileInfo) {
        String fileName = MultiFileUtils.mergingParts(fileInfo, fileLocalProperties.getTempPath(), fileLocalProperties.getPath());
        return getFileInfo(fileName);
    }


    @Override
    public FileInfo exist(String fileName) {
        String existFile = MultiFileUtils.exitFile(fileLocalProperties.getPath(), fileName);
        if (null != existFile) {
            return getFileInfo(fileName);
        }
        return null;
    }

    @Override
    public void delete(String fileName) {
        File file = new File(fileLocalProperties.getPath(), fileName);
        if (!file.delete()) {
            throw new RuntimeException("file delete fail");
        }
    }

    @Override
    public void deleteBatch(List<String> fileNames) {
        for (String fileName : fileNames) {
            this.delete(fileName);
        }
    }

    @Override
    public InputStream download(String fileName) {
        FileInfo fileInfo = exist(fileName);
        if (null != fileInfo) {
            File file = new File(fileLocalProperties.getPath(), fileName);
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return fileInputStream;
        }
        return null;
    }

    @Override
    public void show(String fileName, String process, HttpServletResponse response) {

        try {
            if (null == exist(fileName)) {
                throw new RuntimeException(FileStorageExceptionEnum.FILE_DOWN_FAIL.getMsg());
            }
            FileShowUtils.show(fileLocalProperties.getPath() + fileName, process, response);
        } catch (IOException e) {
            e.printStackTrace();
            response.reset();
            response.setContentType("application/json;charset=utf-8");
            try {
                response.getWriter().write(JSON.toJSONString(R.failed(FileStorageExceptionEnum.FILE_DOWN_FAIL)));
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * 判断是否输出文件路径
     *
     * @param fileName 文件名称
     * @return .
     */
    private FileInfo getFileInfo(String fileName) {
        return new FileInfo().setName(fileName).setUrl(fileLocalProperties.getUrlSuffix() + fileName);
    }

}
