package top.keiskeiframework.cpreading.reader.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListControllerImpl;
import top.keiskeiframework.cpreading.reader.entity.Reader;
import top.keiskeiframework.cpreading.reader.service.IReaderService;

/**
 * <p>
 * 读者管理 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/cpreading/reader")
@Api(tags = "碎片阅读 - 读者管理")
public class ReaderController extends ListControllerImpl<Reader, Long>{

    @Autowired
    private IReaderService readerService;



}
