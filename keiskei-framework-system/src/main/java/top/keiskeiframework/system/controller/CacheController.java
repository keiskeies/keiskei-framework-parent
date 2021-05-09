package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.cache.serivce.CacheStorageService;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.common.vo.cache.CacheDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 缓存管理 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@RestController
@RequestMapping("/admin/v1/system/cache")
@Api(tags = "系统设置 - 缓存管理")
public class CacheController {
    @Autowired
    private CacheStorageService cacheStorageService;


    @GetMapping
    @ApiOperation("缓存列表")
    public R<List<CacheDTO>> list() {
        Map<String, Object> data = cacheStorageService.list();
        return R.ok(CacheDTO.of(data));
    }

    @GetMapping("/{key}")
    @ApiOperation("缓存详情")
    public R<CacheDTO> getOne(@PathVariable String key) {
        return R.ok(new CacheDTO(key, cacheStorageService.get(key)));
    }

    @PostMapping
    @ApiOperation("缓存新增")
    public R<CacheDTO> save(@Validated(Insert.class) @RequestBody CacheDTO cacheDTO) {
        cacheStorageService.save(cacheDTO.getKey(), cacheDTO.getValue());
        return R.ok(cacheDTO);
    }

    @PutMapping
    @ApiOperation("缓存更新")
    public R<CacheDTO> update(@Validated(Update.class) @RequestBody CacheDTO cacheDTO) {
        cacheStorageService.update(cacheDTO.getKey(), cacheDTO.getValue());
        return R.ok(cacheDTO);
    }

    @DeleteMapping
    @ApiOperation("缓存清理")
    public R<Boolean> delete() {
        cacheStorageService.delAll();
        return R.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{key}")
    @ApiOperation("缓存删除")
    public R<Boolean> delete(@PathVariable String key) {
        cacheStorageService.del(key);
        return R.ok(Boolean.TRUE);
    }

}
