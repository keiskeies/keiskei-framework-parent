package top.keiskeiframework.common.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
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
import java.util.List;

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
public class TreeEntity implements Serializable {


    private static final long serialVersionUID = -802579500126524571L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "行不能为空", groups = {Update.class, UpdatePart.class, Select.class})
    private Long id;

    private Long parentId;

    private String sign;

    @Transient
    private List<? extends TreeEntity> children;

    /**
     * 物理删除标识
     */
    @JsonIgnore
    private Boolean d = Boolean.FALSE;

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


}
