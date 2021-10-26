package top.keiskeiframework.file.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.file.annotations.MergingChunks;
import top.keiskeiframework.file.annotations.UploadPart;
import top.keiskeiframework.file.annotations.UploadWithProgress;
import top.keiskeiframework.file.dto.FileInfo;
import top.keiskeiframework.file.dto.MultiFileInfo;
import top.keiskeiframework.file.service.FileStorageService;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件前端控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2021/2/19 15:25
 */
@Api(tags = "文件管理")
@Controller
@RequestMapping
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private CacheStorageService cacheStorageService;

    /**
     * 普通上传
     *
     * @param file 文件信息
     * @return .
     */
    @PostMapping("/admin/v1/common/file/upload")
    @ResponseBody
    public R<FileInfo> upload(MultipartFile file) {
        MultiFileInfo fileInfo = new MultiFileInfo(file);
        return R.ok(fileStorageService.upload(fileInfo));
    }

    /**
     * 带进度条上传
     *
     * @param fileInfo 文件信息
     * @return .
     */
    @PostMapping("/admin/v1/common/file/uploadWithProgress")
    @ResponseBody
    public R<FileInfo> uploadWithProgress(@Validated({UploadWithProgress.class}) MultiFileInfo fileInfo) {
        return R.ok(fileStorageService.uploadWithProgress(fileInfo));
    }

    /**
     * 上传文件分片
     *
     * @param fileInfo 文件分片信息
     * @return .
     */
    @ResponseBody
    @PostMapping("/admin/v1/common/file/uploadPart")
    public R<Boolean> uploadPart(@Validated({UploadPart.class}) MultiFileInfo fileInfo) {
        fileStorageService.uploadPart(fileInfo);
        return R.ok(true);
    }

    /**
     * 合并文件分片
     *
     * @param fileInfo 文件信息
     * @return .
     */
    @ResponseBody
    @PostMapping("/admin/v1/common/file/mergingChunks")
    public R<FileInfo> mergingChunks(@Validated({MergingChunks.class}) MultiFileInfo fileInfo) {
        return R.ok(fileStorageService.mergingChunks(fileInfo));
    }

    /**
     * 获取文件上传进度
     *
     * @param fileSign 文件标识
     * @return .
     */
    @GetMapping("/admin/v1/common/file/progress")
    @ResponseBody
    public R<Integer> progress(@RequestParam("fileSign") String fileSign) {
        fileSign = String.format(FileStorageService.UPLOAD_PROGRESS_FORMAT, fileSign);
        int progress;
        if (cacheStorageService.exist(fileSign)) {
            progress = Integer.parseInt(String.valueOf(cacheStorageService.get(fileSign)));
        } else {
            progress = 0;
        }
        return R.ok(progress);
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名
     * @return .
     */
    @GetMapping("/admin/v1/common/file/exist/{fileName:.+}")
    @ResponseBody
    public R<FileInfo> exist(@PathVariable("fileName") String fileName) {
        return R.ok(fileStorageService.exist(fileName));
    }

    /**
     * 获取图片
     *
     * @param fileName 文件类型
     * @param response 相应
     * @param process  文件处理参数
     */
    @GetMapping("/{api:api|admin}/v1/common/file/show/{fileName:.+}")
    public void show(@PathVariable("fileName") String fileName, HttpServletResponse response, @RequestParam(value = "x-oss-process", required = false) String process, @PathVariable String api) {
        try {
            fileStorageService.show(fileName, process, response);
        } catch (Exception ignored) {
        }
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param response 响应
     */
    @GetMapping("/{api:api|admin}/v1/common/file/download/{fileName:.+}")
    public void download(@PathVariable("fileName") String fileName,
                         HttpServletResponse response, @PathVariable String api) {
        fileStorageService.show(fileName, null, response);
    }
}
