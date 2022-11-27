package top.keiskeiframework.performance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;
import top.keiskeiframework.common.base.mp.annotation.MpManyToOne;
import top.keiskeiframework.common.util.data.MoneyDeserializer;
import top.keiskeiframework.common.util.data.MoneySerializer;
import top.keiskeiframework.common.util.data.RateDeserializer;
import top.keiskeiframework.common.util.data.RateSerializer;
import top.keiskeiframework.system.entity.Department;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * @author cjm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "per_quarter_table")
@TableName(value = "per_quarter_table")
@ApiModel(value = "QuarterTable", description = "季度")
public class QuarterTable extends ListEntityImpl<Integer> {

    private static final long serialVersionUID = -2592531384081715031L;

    @NotBlank(message = "请输入季度名称")
    private String name;

    @MpManyToOne(filedName = "departmentId")
    @TableField(exist = false)
    @Transient
    private transient Department department;
    private Integer departmentId;


    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    @NotEmpty(message = "请输入总GMV")
    private Long allGmv;

    @NotEmpty(message = "请输入商品毛利")
    @JsonDeserialize(converter = RateDeserializer.class)
    @JsonSerialize(converter = RateSerializer.class)
    private Integer productGross;

    @NotEmpty(message = "请输入提佣标准")
    @JsonDeserialize(converter = RateDeserializer.class)
    @JsonSerialize(converter = RateSerializer.class)
    private Integer commissionRate;

    @NotEmpty(message = "请输入商品销售佣金")
    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    private Long productSellCommission;

    @NotEmpty(message = "系统利润")
    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    private Long systemProfits;

    @NotEmpty(message = "请输入项目激励占比")
    @JsonDeserialize(converter = RateDeserializer.class)
    @JsonSerialize(converter = RateSerializer.class)
    private Integer stimulateRate;

    @NotEmpty(message = "请输入项目总激励")
    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    private Long projectAllStimulate;

}
