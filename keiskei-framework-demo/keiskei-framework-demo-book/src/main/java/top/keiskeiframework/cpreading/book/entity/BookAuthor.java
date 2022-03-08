package top.keiskeiframework.cpreading.book.entity;

import top.keiskeiframework.common.base.entity.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.cpreading.book.enums.BookAuthorSourceEnum;
import top.keiskeiframework.cpreading.enums.*;

import io.swagger.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import top.keiskeiframework.common.annotation.validate.*;

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

    @ApiModelProperty(value = "来源", dataType="String")
    private BookAuthorSourceEnum source;

    @ApiModelProperty(value = "作者简介", dataType="String")
    @Column(columnDefinition = "longtext")
    private String content;

    @ApiModelProperty(value = "上一次更新者", dataType="Integer")
    private Integer lastUpdateUserId;

    @ApiModelProperty(value = "更新次数", dataType="Integer")
    private Integer updateTimes;

}
