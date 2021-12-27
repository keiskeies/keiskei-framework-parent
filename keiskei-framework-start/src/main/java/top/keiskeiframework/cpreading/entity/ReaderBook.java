package top.keiskeiframework.cpreading.entity;

import com.fasterxml.jackson.annotation.*;
import org.springframework.data.annotation.CreatedDate;
import top.keiskeiframework.common.base.entity.*;
import top.keiskeiframework.common.util.data.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.cpreading.enums.*;

import io.swagger.annotations.*;

import javax.persistence.*;
import javax.validation.*;
import javax.validation.constraints.*;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.annotation.validate.*;
import top.keiskeiframework.common.annotation.data.*;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import java.time.*;
import java.util.*;

/**
 * <p>
 * 读者书库 实体类
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cpreading_reader_book")
@ApiModel(value="ReaderBook", description="读者书库")
public class ReaderBook extends ListEntity<Long> {

    private static final long serialVersionUID = 7197521997223047324L;

    @NotNull(message = "图书不能为空", groups = {Insert.class, Update.class})
    @Valid
    @ApiModelProperty(value = "图书", dataType="Book")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Book book;

    @ApiModelProperty(value = "阅读开始", dataType="LocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private LocalDate startDate;

    @ApiModelProperty(value = "阅读结束", dataType="LocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "读者", dataType="Reader")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Reader reader;

}
