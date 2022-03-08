package top.keiskeiframework.cpreading.book.entity;

import top.keiskeiframework.common.base.entity.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.cpreading.enums.*;

import io.swagger.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import top.keiskeiframework.common.annotation.validate.*;

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
public class BookFeeling extends TreeEntity<Long> {

    private static final long serialVersionUID = -3275305891050571851L;

    @NotNull(message = "图书不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "图书", dataType="Book")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Book book;

    @NotNull(message = "图书章节不能为空", groups = { Insert.class, Update.class })
    @ApiModelProperty(value = "图书章节", dataType = "BookSection")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private BookSection bookSection;

    @NotBlank(message = "内容不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "内容", dataType="String")
    @Column(columnDefinition = "mediumtext")
    private String content;

    @ApiModelProperty(value = "读者", dataType="Reader")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Reader reader;

    @PostPersist
    private void postPersist() {
        Reader reader = new Reader();
        reader.setId(super.createUserId);
        this.reader = reader;

    }

    @ApiModelProperty(value = "回复数量", dataType = "Long")
    private Long replyNum = 0L;

    @ApiModelProperty(value = "评论等级", dataType = "Integer")
    private Integer level = 1;

    @ApiModelProperty(value = "点赞数量", dataType = "Long")
    private Long likeNum = 0L;

}
