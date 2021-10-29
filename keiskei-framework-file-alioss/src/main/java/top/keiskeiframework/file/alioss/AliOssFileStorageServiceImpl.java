package top.keiskeiframework.file.alioss;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.file.alioss.config.FileAliOssProperties;
import top.keiskeiframework.file.dto.FileInfo;
import top.keiskeiframework.file.dto.MultiFileInfo;
import top.keiskeiframework.file.enums.FileStorageExceptionEnum;
import top.keiskeiframework.file.service.FileStorageService;
import top.keiskeiframework.file.util.FileStorageUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 阿里OSS文件管理工具
 *
 * @author 陈加敏
 * @since 2019/7/23 17:26
 */
@Component
@Slf4j
public class AliOssFileStorageServiceImpl implements FileStorageService, ProgressListener {


    @Autowired
    private FileAliOssProperties fileAliOssProperties;
    @Autowired
    private CacheStorageService cacheStorageService;
    @Resource(name = "longOssClient")
    private OSS longOssClient;


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

    @Override
    public FileInfo upload(MultiFileInfo fileInfo) {
        InputStream is = null;
        try {
            this.currentFileName = FileStorageUtils.getFileName(fileInfo);
            is = fileInfo.getFile().getInputStream();
            longOssClient.putObject(new PutObjectRequest(fileAliOssProperties.getBucket(), this.currentFileName, is));
            return this.getFileInfo(this.currentFileName);
        } catch (IOException e) {
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
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        }
        longOssClient.putObject(new PutObjectRequest(fileAliOssProperties.getBucket(), this.currentFileName, is).
                withProgressListener(new AliOssFileStorageServiceImpl()));
        return this.getFileInfo(this.currentFileName);
    }

    /**
     * 获取alioss上传id
     * 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
     *
     * @param fileName 文件名
     * @return .
     */
    private String getUploadId(String fileName) {
        String uploadId;
        String key = String.format(UPLOAD_ID_FORMAT, fileName);
        if (cacheStorageService.exist(key)) {
            uploadId = String.valueOf(cacheStorageService.get(key));
        } else {
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(fileAliOssProperties.getBucket(), fileName);
            InitiateMultipartUploadResult result = longOssClient.initiateMultipartUpload(request);
            uploadId = result.getUploadId();
            cacheStorageService.save(key, uploadId);
        }
        return uploadId;
    }

    @Override
    public void uploadPart(MultiFileInfo fileInfo) {
        //定义文件标识
        String fileSign = fileInfo.getId();
        //初始化文件分片记录
        List<PartETag> partEtagList;
        if (cacheStorageService.exist(String.format(PART_ETAG_FORMAT, fileSign))) {
            partEtagList = JSON.parseArray(String.valueOf(cacheStorageService.get(String.format(PART_ETAG_FORMAT, fileSign))), PartETag.class);
        } else {
            partEtagList = new ArrayList<>();
        }
        String fileName = null;
        String uploadId = null;
        try {
            fileName = FileStorageUtils.getFileName(fileInfo);
            uploadId = this.getUploadId(fileName);

            // 跳过已经上传的分片。
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(fileAliOssProperties.getBucket());
            uploadPartRequest.setKey(fileName);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(fileInfo.getFile().getInputStream());
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
            uploadPartRequest.setPartSize(fileInfo.getFile().getSize());
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
            uploadPartRequest.setPartNumber(fileInfo.getChunk() + 1);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = longOssClient.uploadPart(uploadPartRequest);
            partEtagList.add(uploadPartResult.getPartETag());
            cacheStorageService.save(String.format(PART_ETAG_FORMAT, fileSign), JSON.toJSONString(partEtagList));
        } catch (Exception e) {
            e.printStackTrace();
            if (!StringUtils.isEmpty(fileName)) {

                cacheStorageService.del(String.format(UPLOAD_ID_FORMAT, fileName));
                if (StringUtils.isEmpty(uploadId)) {

                    AbortMultipartUploadRequest abortMultipartUploadRequest =
                            new AbortMultipartUploadRequest(fileAliOssProperties.getBucket(), fileName, uploadId);

                    longOssClient.abortMultipartUpload(abortMultipartUploadRequest);
                }
            }
            cacheStorageService.del(String.format(PART_ETAG_FORMAT, fileSign));
            // 取消分片上传，其中uploadId来自于InitiateMultipartUpload。
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        }
    }

    @Override
    public FileInfo mergingChunks(MultiFileInfo fileInfo) {
        //查看文件alioss分片标识集合是否存在
        String fileSign = fileInfo.getId();
        List<PartETag> partEtags = JSON.parseArray(String.valueOf(cacheStorageService.get(String.format(PART_ETAG_FORMAT, fileSign))), PartETag.class);
        if (CollectionUtils.isEmpty(partEtags) || partEtags.size() != fileInfo.getChunks()) {
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        }
        String fileName = null;
        String uploadId = null;
        try {
            fileName = FileStorageUtils.getFileName(fileInfo);
            uploadId = String.valueOf(cacheStorageService.get(String.format(UPLOAD_ID_FORMAT, fileName)));
            //查看文件上传id是否存在
            if (StringUtils.isEmpty(uploadId)) {
                throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
            }
            // 排序。partETags必须按分片号升序排列。
            partEtags.sort(Comparator.comparingInt(PartETag::getPartNumber));
            // 在执行该操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，
            // 会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(fileAliOssProperties.getBucket(), fileName, uploadId, partEtags);
            longOssClient.completeMultipartUpload(completeMultipartUploadRequest);
            log.info("文件: {} 上传结束", fileName);
            return this.getFileInfo(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            // 取消分片上传，其中uploadId来自于InitiateMultipartUpload。
            AbortMultipartUploadRequest abortMultipartUploadRequest =
                    new AbortMultipartUploadRequest(fileAliOssProperties.getBucket(), fileName, uploadId);
            longOssClient.abortMultipartUpload(abortMultipartUploadRequest);
            throw new BizException(FileStorageExceptionEnum.FILE_UPLOAD_FAIL);
        } finally {
            cacheStorageService.del(String.format(UPLOAD_ID_FORMAT, fileName));
            cacheStorageService.del(String.format(PART_ETAG_FORMAT, fileSign));

        }
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                log.info("file {} Start to upload with progress......",
                        this.currentFileName);
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                log.info("file {} {} bytes in total will be uploaded to OSS",
                        this.currentFileName, this.totalBytes);
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    cacheStorageService.save(String.format(UPLOAD_PROGRESS_FORMAT, fileSign), (int) (this.bytesWritten * 100 / this.totalBytes));
                } else {
                    log.error("file {} {} bytes have been written at this time, upload ratio: unknown ( {}/...)",
                            this.currentFileName, bytes, this.bytesWritten);
                    cacheStorageService.del(String.format(UPLOAD_PROGRESS_FORMAT, fileSign));
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                log.info("file {} upload success", this.currentFileName);
                cacheStorageService.del(String.format(UPLOAD_PROGRESS_FORMAT, fileSign));
                break;
            case TRANSFER_FAILED_EVENT:
                log.error("file {} Failed to upload, {} bytes have been transferred",
                        this.currentFileName, this.bytesWritten);
                break;
            default:
                break;
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
        if (fileAliOssProperties.getOutsideNet()) {
            return fileInfo.setUrl(fileAliOssProperties.getProtocol() + String.format(URL_FORMAT, fileAliOssProperties.getBucket(), fileAliOssProperties.getEndpoint(), fileName));
        } else {
            return fileInfo.setUrl(fileName);
        }
    }


    @Override
    public FileInfo exist(String fileName) {
        if (longOssClient.doesObjectExist(fileAliOssProperties.getBucket(), fileName)) {
            return getFileInfo(fileName);
        }
        return null;
    }

    @Override
    public void delete(String fileName) {
        longOssClient.deleteObject(fileAliOssProperties.getBucket(), fileName);
    }

    @Override
    public void deleteBatch(List<String> fileNames) {
        longOssClient.deleteObjects(new DeleteObjectsRequest(fileAliOssProperties.getBucket()).withKeys(fileNames));
    }

    @Override
    public InputStream download(String fileName) {
        return longOssClient.getObject(fileAliOssProperties.getBucket(), fileName).getObjectContent();
    }

    @Override
    public void show(String fileName, String process, HttpServletRequest request, HttpServletResponse response) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(fileAliOssProperties.getBucket(), fileName);
        if (!StringUtils.isEmpty(process)) {
            getObjectRequest.setProcess(process);
        }
        OSSObject ossObject = longOssClient.getObject(getObjectRequest);
        String contentType = ossObject.getObjectMetadata().getContentType();
        InputStream is = ossObject.getObjectContent();

        response.setContentType(contentType);

        ServletOutputStream os = null;
        try {
            if (null == is) {
                throw new RuntimeException(FileStorageExceptionEnum.FILE_NOT_FOUND.getMsg());
            }
            os = response.getOutputStream();
            int bufferLength;
            byte[] buffer = new byte[StreamUtils.BUFFER_SIZE];
            while ((bufferLength = is.read(buffer)) != -1) {
                os.write(buffer, 0, bufferLength);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.reset();
            response.setContentType("application/json;charset=utf-8");
            try {
                response.getWriter().write(JSON.toJSONString(R.failed(FileStorageExceptionEnum.FILE_DOWN_FAIL)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
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
