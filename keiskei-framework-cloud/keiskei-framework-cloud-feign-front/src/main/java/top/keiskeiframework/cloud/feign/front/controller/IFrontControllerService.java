package top.keiskeiframework.cloud.feign.front.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * controller通用接口
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/21 22:16
 */
public interface IFrontControllerService<T extends IBaseEntity<ID>, ID extends Serializable> {


    /**
     * 分页查询
     *
     * @param requestVO 查询条件
     * @param pageVO    分页条件
     * @return 。
     */
    @GetMapping
    R<PageResultVO<T>> page(BaseRequestVO requestVO, BasePageVO pageVO);

    /**
     * 下拉框
     *
     * @param requestVO 查询条件
     * @return 。
     */
    @GetMapping("/options")
    R<List<T>> options(BaseRequestVO requestVO);

    /**
     * 详情
     *
     * @param id ID
     * @return data
     */
    @GetMapping("/{id}")
    R<T> getOne(@PathVariable("id") ID id);

    /**
     * 保存
     *
     * @param t t
     * @return t with id
     */
    @PostMapping
    R<T> save(@RequestBody @Validated(Insert.class) T t);

    /**
     * 更新
     *
     * @param t t
     * @return t
     */
    @PutMapping
    R<T> update(@RequestBody @Validated(Update.class) T t);


    /**
     * 删除
     *
     * @param id ID
     * @return boolean
     */
    @DeleteMapping("/{id}")
    R<Boolean> delete(@PathVariable("id") ID id);


}
