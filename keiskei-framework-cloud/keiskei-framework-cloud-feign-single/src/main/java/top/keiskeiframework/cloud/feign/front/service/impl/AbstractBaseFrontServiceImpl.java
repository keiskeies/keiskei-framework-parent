package top.keiskeiframework.cloud.feign.front.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.cloud.feign.front.service.IFrontService;
import top.keiskeiframework.cloud.feign.front.util.BaseRequestUtils;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/24 21:24
 */
public class AbstractBaseFrontServiceImpl<DTO extends IBaseEntity<ID>, T extends IBaseEntity<ID>, ID extends Serializable>
        implements IFrontService<DTO, ID> {

    protected Class<DTO> dtoClass;
    protected Class<T> tClass;
    protected Class<ID> idClass;

    public AbstractBaseFrontServiceImpl() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        this.dtoClass = (Class<DTO>) types[0];
        this.tClass = (Class<T>) types[1];
        this.idClass = (Class<ID>) types[2];
    }

    @Autowired
    protected IControllerService<T, ID> baseFeignService;

    @Override
    public PageResultVO<DTO> page(BaseRequestVO requestVO, BasePageVO pageVO) {
        BaseRequestVO requestVO1 = BaseRequestUtils.translate2T(requestVO);
        PageResultVO<T> tResult = baseFeignService.page(requestVO1, pageVO).getData();
        return BaseRequestUtils.translate2Dto(tResult, dtoClass);
    }

    @Override
    public List<DTO> options(BaseRequestVO requestVO) {
        BaseRequestVO requestVO1 = BaseRequestUtils.translate2T(requestVO);
        List<T> tResult = baseFeignService.options(requestVO1).getData();
        return BaseRequestUtils.translate2Dto(tResult, dtoClass);
    }

    @Override
    public DTO findById(ID id) {
        T t = baseFeignService.getOne(id).getData();
        return BaseRequestUtils.translate2Dto(t, dtoClass);
    }


    @Override
    public DTO getOne(String conditions) {
        BaseRequestVO requestVO = new BaseRequestVO();
        requestVO.setConditions(conditions);
        T t = baseFeignService.getOne(requestVO).getData();
        return BaseRequestUtils.translate2Dto(t, dtoClass);
    }

    @Override
    public Long count(String conditions) {
        BaseRequestVO requestVO = new BaseRequestVO();
        requestVO.setConditions(conditions);
        return baseFeignService.count(requestVO).getData();
    }

    @Override
    public DTO save(DTO dto) {
        T t = BaseRequestUtils.translate2T(dto, tClass);
        t = baseFeignService.save(t).getData();
        return BaseRequestUtils.translate2Dto(t, dtoClass);
    }

    @Override
    public List<DTO> save(List<DTO> dtoList) {
        List<T> ts = BaseRequestUtils.translate2T(dtoList, tClass);
        ts = baseFeignService.save(ts).getData();
        return BaseRequestUtils.translate2Dto(ts, dtoClass);
    }

    @Override
    public DTO update(DTO dto) {
        T t = BaseRequestUtils.translate2T(dto, tClass);
        t = baseFeignService.update(t).getData();
        return BaseRequestUtils.translate2Dto(t, dtoClass);
    }

    @Override
    public List<DTO> update(List<DTO> dtoList) {
        List<T> ts = BaseRequestUtils.translate2T(dtoList, tClass);
        ts = baseFeignService.update(ts).getData();
        return BaseRequestUtils.translate2Dto(ts, dtoClass);
    }

    @Override
    public void deleteById(ID id) {
        baseFeignService.deleteById(id);
    }

    @Override
    public Boolean delete(List<ID> ids) {
        return baseFeignService.deleteMulti(ids).getData();
    }

    @Override
    public Map<String, Double> statistic(
            String column,
            ColumnType columnType,
            String timeField,
            TimeDeltaEnum timeDelta,
            String start,
            String end,
            CalcType calcType,
            String sumColumn,
            String conditions
    ) {
        return baseFeignService.statistic(
                column,
                columnType,
                timeField,
                timeDelta,
                start,
                end,
                calcType,
                sumColumn,
                conditions
        ).getData();
    }
}
