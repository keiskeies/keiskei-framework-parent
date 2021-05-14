package top.keiskeiframework.common.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import top.keiskeiframework.common.annotation.validate.Select;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.annotation.validate.UpdatePart;
import top.keiskeiframework.common.util.SecurityUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 基础实体类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:51
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -8025795001235125591L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "行不能为空", groups = {Update.class, UpdatePart.class, Select.class})
    private Long id;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(updatable = false)
    @ApiModelProperty(value = "创建时间", dataType = "LocalDateTime")
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间", dataType = "LocalDateTime")
    private LocalDateTime updateTime;

    /**
     * 物理删除标识
     */
    @JsonIgnore
    private Boolean d;


    /**
     * 数据所属部门
     */
    private String p;

    /**
     * 数据创建人
     * 数据初始化来源 {@link top.keiskeiframework.common.base.service.AbstractAuditorAware}
     */
    @CreatedBy
    private Long createUserId;

    /**
     * 最后修改人
     * 数据初始化来源 {@link top.keiskeiframework.common.base.service.AbstractAuditorAware}
     */
    @LastModifiedBy
    private Long updateUserId;


    @PrePersist
    protected void onCreate() {
        d = Boolean.FALSE;
        p = SecurityUtils.getDepartment();
    }

    /**
     * 图表下标
     */
    @Transient
    protected String index;

    /**
     * 图表下标数量
     */
    @Transient
    protected Long indexNumber;

    BaseEntity(String index, Long indexNumber) {
        this.index = index;
        this.indexNumber = indexNumber;
    }





}
