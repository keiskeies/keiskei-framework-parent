package top.keiskeiframework.common.base.entity.impl;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.util.MdcUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * List entity
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 17:04
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class ListEntityImpl<ID extends Serializable> implements IListEntity<ID> {

    private static final long serialVersionUID = -6846795795755133606L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    protected ID id;

    /**
     * 删除标记
     * 数据初始化来源 {@link top.keiskeiframework.common.base.mp.config.MyMetaObjectHandler}
     */
    @TableField(fill = FieldFill.INSERT)
    @Column(columnDefinition = "tinyint(1) DEFAULT '0'")
    protected Integer d;
    /**
     * 数据所属部门
     * 数据初始化来源 {@link top.keiskeiframework.common.base.mp.config.MyMetaObjectHandler}
     */
    @TableField(fill = FieldFill.INSERT)
    protected String p;

    @PrePersist
    protected void onCreate() {
        p = MdcUtils.getUserDepartment();
    }

    /**
     * 数据创建人
     * 数据初始化来源 {@link top.keiskeiframework.common.base.mp.config.MyMetaObjectHandler}
     */
    @TableField(fill = FieldFill.INSERT)
    @CreatedBy
    @Column(length = 128)
    protected String createUserId;

    /**
     * 最后修改人
     * 数据初始化来源 {@link top.keiskeiframework.common.base.mp.config.MyMetaObjectHandler}
     */
    @TableField(fill = FieldFill.UPDATE)
    @LastModifiedBy
    @Column(length = 128)
    protected String updateUserId;


    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", dataType = "LocalDateTime")
    @TableField(fill = FieldFill.INSERT)
    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", dataType = "LocalDateTime")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @UpdateTimestamp
    protected LocalDateTime updateTime;
}
