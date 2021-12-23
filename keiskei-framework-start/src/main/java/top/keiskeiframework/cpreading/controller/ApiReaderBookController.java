package top.keiskeiframework.cpreading.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.common.enums.dashboard.TimeDeltaEnum;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.cpreading.entity.Book;
import top.keiskeiframework.cpreading.entity.ReaderBook;
import top.keiskeiframework.cpreading.service.IReaderBookService;
import top.keiskeiframework.cpreading.vo.BookTerritoryVO;
import top.keiskeiframework.cpreading.vo.BookTimelineVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 读者书库 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/api/v2/cpreading/readerBook")
@Api(tags = "碎片阅读 - 读者书库")
public class ApiReaderBookController extends ListController<ReaderBook, Long> {

    @Autowired
    private IReaderBookService readerBookService;

    @GetMapping("/territory")
    @ApiOperation("领域")
    public R<List<BookTerritoryVO>> territory() {
        return R.ok(readerBookService.territory());
    }


    @GetMapping("/territory/detail/{typeId}")
    @ApiOperation("领域")
    public R<List<Book>> territoryDetail(
            @PathVariable Long typeId
    ) {
        return R.ok(readerBookService.territoryDetail(typeId));
    }

    @GetMapping("/timeline")
    @ApiOperation("碎片空间")
    public R<List<BookTimelineVO>> timeLine(
            @RequestParam TimeDeltaEnum timeDelta
    ) {
        return R.ok(readerBookService.timeLine(timeDelta));
    }


}
