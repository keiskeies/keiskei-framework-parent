package top.keiskeiframework.cpreading.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;

/**
 * <p>
 * 图书拓展 实体类
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
@Table(name = "cpreading_book_expand")
@ApiModel(value = "BookExpand", description = "图书拓展")
public class BookExpand extends ListEntity<Long> {

    private static final long serialVersionUID = -6769552817545368272L;

    @NotNull(message = "图书不能为空", groups = { Insert.class, Update.class })
    @Valid
    @ApiModelProperty(value = "图书", dataType = "Book")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Book book;

    @NotNull(message = "图书章节不能为空", groups = { Insert.class, Update.class })
    @Valid
    @ApiModelProperty(value = "图书章节", dataType = "BookSection")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private BookSection bookSection;

    @NotNull(message = "关联图书不能为空", groups = { Insert.class, Update.class })
    @Valid
    @ApiModelProperty(value = "关联图书", dataType = "Book")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Book expendBook;
}
