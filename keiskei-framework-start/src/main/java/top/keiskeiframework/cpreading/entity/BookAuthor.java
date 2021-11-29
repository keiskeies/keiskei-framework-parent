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
 * 图书作者 实体类
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
@Table(name = "cpreading_book_author")
@ApiModel(value="BookAuthor", description="图书作者")
public class BookAuthor extends ListEntity<Long> {

    private static final long serialVersionUID = 3633147995913488558L;

    @NotBlank(message = "姓名不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "姓名", dataType="String")
    private String name;

    @NotNull(message = "来源不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "来源", dataType="String")
    private BookAuthorSourceEnum source;

    @NotBlank(message = "作者简介不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "作者简介", dataType="String")
    @Column(columnDefinition = "longtext")
    private String content;

    @NotNull(message = "上一次更新者不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "上一次更新者", dataType="Integer")
    private Integer lastUpdateUserId;

    @NotNull(message = "更新次数不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "更新次数", dataType="Integer")
    private Integer updateTimes;

}
