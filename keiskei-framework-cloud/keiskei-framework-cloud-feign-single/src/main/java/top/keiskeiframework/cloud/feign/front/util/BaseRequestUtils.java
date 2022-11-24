package top.keiskeiframework.cloud.feign.front.util;

import org.springframework.beans.BeanUtils;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/24 20:57
 */
public class BaseRequestUtils {


    public static <DTO extends IBaseEntity<ID>, T extends IBaseEntity<ID>, ID extends Serializable> BaseRequestVO<T, ID> translate2T(BaseRequestVO<DTO, ID> requestVO) {
        BaseRequestVO<T, ID> requestVO1 = new BaseRequestVO<>();
        BeanUtils.copyProperties(requestVO, requestVO1);
        return requestVO1;
    }


    public static <DTO extends IBaseEntity<ID>, T extends IBaseEntity<ID>, ID extends Serializable> T translate2T(DTO dto, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            BeanUtils.copyProperties(dto, t);
            return t;
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }


    public static <DTO extends IBaseEntity<ID>, T extends IBaseEntity<ID>, ID extends Serializable> List<T> translate2T(List<DTO> dtoList, Class<T> tClass) {
        return dtoList.stream().map(e -> {
                    T t = null;
                    try {
                        t = tClass.newInstance();
                        BeanUtils.copyProperties(e, t);
                        return t;
                    } catch (InstantiationException | IllegalAccessException ex) {
                        return null;
                    }
                }
        ).collect(Collectors.toList());
    }


    public static <DTO extends IBaseEntity<ID>, T extends IBaseEntity<ID>, ID extends Serializable> BaseRequestVO<DTO, ID> translate2Dto(BaseRequestVO<T, ID> requestVO) {
        BaseRequestVO<DTO, ID> requestVO1 = new BaseRequestVO<>();
        BeanUtils.copyProperties(requestVO, requestVO1);
        return requestVO1;
    }


    public static <DTO extends IBaseEntity<ID>, T extends IBaseEntity<ID>, ID extends Serializable> PageResultVO<DTO> translate2Dto(PageResultVO<T> pageResult, Class<DTO> dtoClass) {
        PageResultVO<DTO> result = new PageResultVO<>();
        BeanUtils.copyProperties(pageResult, result);
        result.setData(pageResult.getData().stream().map(e -> {
                    DTO dto = null;
                    try {
                        dto = dtoClass.newInstance();
                        BeanUtils.copyProperties(e, dto);
                        return dto;
                    } catch (InstantiationException | IllegalAccessException ex) {
                        return null;
                    }
                }
        ).collect(Collectors.toList()));
        return result;
    }

    public static <DTO extends IBaseEntity<ID>, T extends IBaseEntity<ID>, ID extends Serializable> List<DTO> translate2Dto(List<T> ts, Class<DTO> dtoClass) {
        return ts.stream().map(e -> {
                    DTO dto = null;
                    try {
                        dto = dtoClass.newInstance();
                        BeanUtils.copyProperties(e, dto);
                        return dto;
                    } catch (InstantiationException | IllegalAccessException ex) {
                        return null;
                    }
                }
        ).collect(Collectors.toList());
    }

    public static <DTO extends IBaseEntity<ID>, T extends IBaseEntity<ID>, ID extends Serializable> DTO translate2Dto(T t, Class<DTO> dtoClass) {
        try {
            DTO dto = dtoClass.newInstance();
            BeanUtils.copyProperties(t, dto);
            return dto;
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }
}
