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
 * 图书章节 实体类
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
@Table(name = "cpreading_book_section")
@ApiModel(value="BookSection", description="图书章节")
public class BookSection extends ListEntity<Long> {

    private static final long serialVersionUID = -2161398866932913302L;

    @NotBlank(message = "章节名称不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "章节名称", dataType="String")
    private String name;

    @NotNull(message = "章节编号不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "章节编号", dataType="Integer")
    private Integer number;

    @ApiModelProperty(value = "排序", dataType = "ID")
    @SortBy(desc = false)
    private Long sortBy;

    @PostPersist
    private void postPersist() {
        this.sortBy = super.getId();
    }

    @ApiModelProperty(value = "章节内容", dataType="String")
    @Column(columnDefinition = "longtext")
    private String content;

    @NotNull(message = "图书不能为空", groups = {Insert.class, Update.class})
    @Valid
    @ApiModelProperty(value = "图书", dataType="Book")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Book book;

}
