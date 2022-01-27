package top.keiskeiframework.demo.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.util.data.MoneyDeserializer;
import top.keiskeiframework.common.util.data.MoneySerializer;
import top.keiskeiframework.demo.order.enums.OrderStatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/21 23:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_order")
@ApiModel(value = "Order", description = "订单")
public class Order extends ListEntity<Long> {

    @ApiModelProperty(value = "订单号", dataType = "String")
    private String orderNumber;

    @ApiModelProperty(value = "交易单号", dataType = "String")
    private String transactionCode;

    @ApiModelProperty(value = "收件人ID", dataType = "Long")
    private Long userId;

    @ApiModelProperty(value = "收件人姓名", dataType = "String")
    private String userName;

    @ApiModelProperty(value = "收件人手机号", dataType = "String")
    private String userPhone;

    @ApiModelProperty(value = "国家", dataType = "Long")
    private Long countryId;
    private String countryName;

    @ApiModelProperty(value = "省", dataType = "Long")
    private Long provinceId;
    private String provinceName;

    @ApiModelProperty(value = "市", dataType = "Long")
    private Long cityId;
    private String cityName;

    @ApiModelProperty(value = "区/县", dataType = "Long")
    private Long prefectureId;
    private String prefectureName;

    @ApiModelProperty(value = "详细地址", dataType = "String")
    private String address;

    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    @ApiModelProperty(value = "总价", dataType = "Double")
    private Long allPrice;

    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    @ApiModelProperty(value = "优惠", dataType = "Double")
    private Long freePrice;

    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    @ApiModelProperty(value = "实付款", dataType = "Double")
    private Long realPrice;

    @ApiModelProperty(value = "订单状态", dataType = "OrderStatusEnum")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty(value = "订单详情", dataType = "List<OrderDetail>")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order", orphanRemoval = true)
    private List<OrderDetail> details;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "付款时间", dataType = "LocalDateTime")
    protected LocalDateTime payTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "取消时间", dataType = "LocalDateTime")
    protected LocalDateTime cancelTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "关闭时间", dataType = "LocalDateTime")
    protected LocalDateTime closeTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "完成时间", dataType = "LocalDateTime")
    protected LocalDateTime completeTime;


}
