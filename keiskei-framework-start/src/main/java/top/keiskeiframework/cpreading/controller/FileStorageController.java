package top.keiskeiframework.cpreading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.file.dto.FileInfo;
import top.keiskeiframework.file.dto.MultiFileInfo;
import top.keiskeiframework.file.service.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/11/29 11:53
 */
@Controller
@RequestMapping("/{api:api|admin}/v2/cpreading/file")
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/show/{fileName}")
    public void show(@PathVariable("fileName") String fileName,
                     HttpServletRequest request,
                     HttpServletResponse response,
                     @RequestParam(value = "x-oss-process", required = false) String process
    ) {
        try {
            fileStorageService.show(fileName.trim(), process, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public R<FileInfo> upload(
            MultipartFile file
    ) {
        MultiFileInfo fileInfo = new MultiFileInfo(file);
        return R.ok(fileStorageService.upload(fileInfo));
    }
}
