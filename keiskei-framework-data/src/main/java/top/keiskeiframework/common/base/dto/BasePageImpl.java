package top.keiskeiframework.common.base.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import top.keiskeiframework.common.base.util.BaseSortDeserializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Feign分页封装类
 * 解决Page序列化失败问题
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/4 12:36
 */
@Getter
@Setter
public class BasePageImpl<T> extends PageImpl<T> implements Serializable {

    private static final long serialVersionUID = -187487956485796384L;

    private int number;
    private int size = 20;
    private int totalPages;
    private int numberOfElements;
    private long totalElements;
    private boolean previousPage;
    private boolean first;
    private boolean nextPage;
    private boolean last;
    private List<T> content;
    @JsonDeserialize(using = BaseSortDeserializer.class)
    private Sort sort;


    public BasePageImpl() {
        super(new ArrayList<>());
    }

    public BasePageImpl(List<T> content) {
        super(content);
    }

    public BasePageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageImpl<T> pageImpl() {
        return new PageImpl<T>(getContent(), PageRequest.of(getNumber(),
                getSize(), getSort()), getTotalElements());
    }
}
