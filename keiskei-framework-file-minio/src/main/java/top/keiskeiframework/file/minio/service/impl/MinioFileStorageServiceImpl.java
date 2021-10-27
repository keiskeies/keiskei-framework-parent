package top.keiskeiframework.file.minio.service.impl;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xml.internal.utils.ObjectStack;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.file.dto.FileInfo;
import top.keiskeiframework.file.dto.MultiFileInfo;
import top.keiskeiframework.file.enums.FileStorageExceptionEnum;
import top.keiskeiframework.file.minio.config.FileMinioProperties;
import top.keiskeiframework.file.service.FileStorageService;
import top.keiskeiframework.file.util.FileStorageUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 阿里OSS文件管理工具
 *
 * @author 陈加敏
 * @since 2019/7/23 17:26
 */
@Component
@Slf4j
public class MinioFileStorageServiceImpl implements FileStorageService {


    @Autowired
    private FileMinioProperties fileMinioProperties;
    @Autowired
    private CacheStorageService cacheStorageService;
    @Autowired
    private MinioClient minioClient;


    /**
     * 文件已传输
     */
    private long bytesWritten = 0;
    /**
     * 文件总大小
     */
    private long totalBytes = -1;
    /**
     * 文件名称
     */
    private String currentFileName;
    /**
     * 文件标记, 用于记录上传比例
     */
    private String fileSign;
    /**
     * oss 路径通配
     */
    private final static String URL_FORMAT = "%s.%s/%s";
    private final static String UPLOAD_PART_FORMAT = "UPLOAD_PART:%s%s";

    @Override
    public FileInfo upload(MultiFileInfo fileInfo) {
        InputStream is = null;
        try {
            this.currentFileName = FileStorageUtils.getFileName(fileInfo);
            is = fileInfo.getFile().getInputStream();
            String fileName = FileStorageUtils.getFileName(fileInfo);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(fileMinioProperties.getBucket())
                    .object(fileName)
                    .stream(is, fileInfo.getFile().getSize(), -1)
                    .contentType(fileInfo.getFile().getContentType())
                    .build()
            );
            return this.getFileInfo(this.currentFileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public FileInfo uploadWithProgress(MultiFileInfo fileInfo) {

        this.fileSign = fileInfo.getId();
        cacheStorageService.save(String.format(UPLOAD_PROGRESS_FORMAT, fileSign), 0);
        InputStream is;
        try {
            this.currentFileName = FileStorageUtils.getFileName(fileInfo);
            is = fileInfo.getFile().getInputStream();
            String fileName = FileStorageUtils.getFileName(fileInfo);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(fileMinioProperties.getBucket())
                    .object(fileName)
                    .stream(is, fileInfo.getFile().getSize(), -1)
                    .contentType(fileInfo.getFile().getContentType())
                    .build()
            );
            return this.getFileInfo(this.currentFileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cacheStorageService.save(String.format(UPLOAD_PROGRESS_FORMAT, fileSign), 100);
        }
        return this.getFileInfo(this.currentFileName);
    }


    @Override
    public void uploadPart(MultiFileInfo fileInfo) {

        InputStream is;
        try {
            this.currentFileName = FileStorageUtils.getFileName(fileInfo);
            is = fileInfo.getFile().getInputStream();
            String fileName = fileInfo.getId() + "/" + fileInfo.getChunk();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(fileMinioProperties.getBucket())
                    .object(fileName)
                    .stream(is, fileInfo.getFile().getSize(), -1)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        }

    }

    @Override
    public FileInfo mergingChunks(MultiFileInfo fileInfo) {
        try {
            String fileName = FileStorageUtils.getFileName(fileInfo);
            List<ComposeSource> sourceObjectList = new ArrayList<>();
            for (int i = 0; i < fileInfo.getChunks(); i++) {
                sourceObjectList.add(
                    ComposeSource.builder()
                            .bucket(fileMinioProperties.getBucket())
                            .object(fileInfo.getId() + "/" + i)
                            .build()
                );
            }
            minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(fileMinioProperties.getBucket())
                            .object(fileName)
                            .sources(sourceObjectList)
                            .build());

            return this.getFileInfo(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        } finally {
            for (int i = 0; i < fileInfo.getChunks(); i++) {
                try {
                    delete(fileInfo.getId() + "/" + i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 判断是否输出oss全路径
     *
     * @param fileName 文件名称
     * @return .
     */
    private FileInfo getFileInfo(String fileName) {
        FileInfo fileInfo = new FileInfo().setName(fileName);
        if (fileMinioProperties.getOutsideNet()) {
            return fileInfo.setUrl(fileMinioProperties.getProtocol() + String.format(URL_FORMAT, fileMinioProperties.getBucket(), fileMinioProperties.getEndpoint(), fileName));
        } else {
            return fileInfo.setUrl(fileMinioProperties.getUrlSuffix() + fileName);
        }
    }


    @Override
    public FileInfo exist(String fileName) {
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(fileMinioProperties.getBucket())
                            .object(fileName)
                            .build()
            );
            if (null == statObjectResponse) {
                return null;
            } else {
                return getFileInfo(fileName);
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(fileMinioProperties.getBucket())
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_DELETE_FAIL);
        }
    }

    @Override
    public void deleteBatch(List<String> fileNames) {
        try {
            minioClient.removeObjects(

                    RemoveObjectsArgs.builder()
                            .bucket(fileMinioProperties.getBucket())
                            .objects(fileNames.stream().map(DeleteObject::new).collect(Collectors.toList()))
                            .build()

            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_DELETE_FAIL);
        }
    }

    @Override
    public InputStream download(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(fileMinioProperties.getBucket())
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_DOWN_FAIL);
        }
    }

    @Override
    public void show(String fileName, String process, HttpServletResponse response) {
        ServletOutputStream os = null;
        GetObjectResponse getObjectResponse = null;
        try {
            getObjectResponse = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(fileMinioProperties.getBucket())
                            .object(fileName)
                            .build()
            );
            if (null == getObjectResponse) {
                throw new RuntimeException(FileStorageExceptionEnum.FILE_NOT_FOUND.getMsg());
            }
            response.setContentType(getObjectResponse.headers().get("ContentType"));


            os = response.getOutputStream();
            int bufferLength;
            byte[] buffer = new byte[StreamUtils.BUFFER_SIZE];
            while ((bufferLength = getObjectResponse.read(buffer)) != -1) {
                os.write(buffer, 0, bufferLength);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.reset();
            response.setContentType("application/json;charset=utf-8");
            try {
                response.getWriter().write(JSON.toJSONString(R.failed(FileStorageExceptionEnum.FILE_DOWN_FAIL)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (null != getObjectResponse) {
                try {
                    getObjectResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
