package top.keiskeiframework.cpreading.book.entity;

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

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 图书管理 实体类
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
@Table(name = "cpreading_book")
@ApiModel(value = "Book", description = "图书管理")
public class Book extends ListEntity<Long> {

    private static final long serialVersionUID = -9102275816898583136L;

    @NotBlank(message = "书名不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "书名", dataType = "String")
    private String name;

    @NotEmpty(message = "作者不能为空", groups = {Insert.class, Update.class})
    @Valid
    @ApiModelProperty(value = "作者", dataType = "List<BookAuthor>")
    @ManyToMany(targetEntity = BookAuthor.class, cascade = CascadeType.DETACH)
    @JoinTable(name = "cpreading_book_bookauthor",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "bookauthor_id", referencedColumnName = "id")})
    private List<BookAuthor> authors;

    @ApiModelProperty(value = "译者", dataType = "String")
    private String translator;

    @ApiModelProperty(value = "主图", dataType = "String")
    private String image;

    @ApiModelProperty(value = "主视频", dataType = "String")
    private String video;

    @NotNull(message = "图书分类不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "图书分类", dataType = "BookType")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private BookType type;

    @ApiModelProperty(value = "出版社", dataType = "String")
    private String publication;


    @ApiModelProperty(value = "简介", dataType = "String")
    @Column(columnDefinition = "text")
    private String description;

}
