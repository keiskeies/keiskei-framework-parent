package top.keiskeiframework.cpreading.entity;

import com.fasterxml.jackson.annotation.*;
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
 * 图书感悟 实体类
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
@Table(name = "cpreading_book_feeling")
@ApiModel(value="BookFeeling", description="图书感悟")
public class BookFeeling extends ListEntity<Long> {

    private static final long serialVersionUID = -3275305891050571851L;

    @NotNull(message = "图书不能为空", groups = {Insert.class, Update.class})
    @Valid
    @ApiModelProperty(value = "图书", dataType="Book")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Book book;

    @NotBlank(message = "图书章节不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "图书章节", dataType="String")
    private String bookSection;

    @NotNull(message = "图书章节编号不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "图书章节编号", dataType="Integer")
    private Integer bookSectionNumber;

    @NotBlank(message = "内容不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "内容", dataType="String")
    @Column(columnDefinition = "mediumtext")
    private String content;

}
