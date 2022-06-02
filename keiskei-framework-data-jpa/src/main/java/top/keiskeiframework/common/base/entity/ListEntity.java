package top.keiskeiframework.common.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.keiskeiframework.common.aop.AbstractAuditorAware;
import top.keiskeiframework.common.util.MdcUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 基础实体类
 * </p>
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:51
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class ListEntity<ID extends Serializable> extends BaseEntity<ID> implements IListEntity<ID> {
    private static final long serialVersionUID = -8025795001235125591L;


    /**
     * 数据所属部门
     */
    protected String p;

    @PrePersist
    protected void onCreate() {
        p = MdcUtils.getUserDepartment();
    }

    /**
     * 数据创建人
     * 数据初始化来源 {@link AbstractAuditorAware}
     */
    @CreatedBy
    protected Integer createUserId;

    /**
     * 最后修改人
     * 数据初始化来源 {@link AbstractAuditorAware}
     */
    @LastModifiedBy
    protected Integer updateUserId;


    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(updatable = false)
    @ApiModelProperty(value = "创建时间", dataType = "LocalDateTime")
    protected LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间", dataType = "LocalDateTime")
    protected LocalDateTime updateTime;


}
