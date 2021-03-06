package top.keiskeiframework.file.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import top.keiskeiframework.file.annotations.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/12/9 15:09
 */
@Data
public class MultiFileInfo {

    @NotBlank(groups = {UploadPart.class, MergingChunks.class, UploadWithProgress.class})
    private String id;
    /**
     * 文件md5
     */
    private String md5;
    /**
     * 文件名称
     */
    @NotBlank(groups = {UploadPart.class, MergingChunks.class})
    private String name;

    /**
     * 是否转变hash文件名
     */
    private Boolean hashName = true;
    /**
     * 文件大小
     */
    @NotNull(groups = {UploadPart.class})
    private Long size;
    /**
     * 当前分片下标
     */
    @NotNull(groups = {UploadPart.class})
    @Min(value = 0,groups = {UploadPart.class})
    private Integer chunk;
    /**
     * 总分片数
     */
    @NotNull(groups = {UploadPart.class})
    @Min(value = 1,groups = {UploadPart.class})
    private Integer chunks;
    /**
     * 当前文件分片
     */
    @NotNull(groups = {UploadPart.class, UploadWithProgress.class, Upload.class})
    private MultipartFile file;

    public MultiFileInfo(MultipartFile file) {
        this.file = file;
    }
}
