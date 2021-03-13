package top.keiskeiframework.common.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.keiskeiframework.common.annotation.validate.Select;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.annotation.validate.UpdatePart;

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


}
