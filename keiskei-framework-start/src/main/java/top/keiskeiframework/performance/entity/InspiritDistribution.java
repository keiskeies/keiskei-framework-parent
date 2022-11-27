package top.keiskeiframework.performance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.base.entity.impl.TreeEntityImpl;
import top.keiskeiframework.common.base.mp.annotation.MpManyToOne;
import top.keiskeiframework.common.util.data.RateDeserializer;
import top.keiskeiframework.common.util.data.RateSerializer;
import top.keiskeiframework.system.entity.Department;
import top.keiskeiframework.system.entity.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author cjm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "per_inspirit_distribution")
@TableName(value = "per_inspirit_distribution")
@ApiModel(value = "InspiritDistribution", description = "分配比例")
public class InspiritDistribution extends TreeEntityImpl<Integer> {

    private static final long serialVersionUID = 6293274760974837014L;

    private String name;

    @MpManyToOne(filedName = "quarterTableId")
    @TableField(exist = false)
    @Transient
    private transient QuarterTable quarterTable;
    private Integer quarterTableId;

    @MpManyToOne(filedName = "departmentId")
    @TableField(exist = false)
    @Transient
    private transient Department department;
    private Integer departmentId;

    @MpManyToOne(filedName = "userId")
    @TableField(exist = false)
    @Transient
    private transient User user;
    private Integer userId;

    @JsonDeserialize(converter = RateDeserializer.class)
    @JsonSerialize(converter = RateSerializer.class)
    private Integer percentage;

}
