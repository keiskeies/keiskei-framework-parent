package top.keiskeiframework.cpreading.feeling.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.TreeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@ApiModel(value = "BookFeeling", description = "图书感悟")
public class BookFeeling extends TreeEntity<Long> {

    private static final long serialVersionUID = -3275305891050571851L;

    @NotNull(message = "图书不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "图书", dataType = "Book")
    private Long bookId;

    @NotNull(message = "图书章节不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "图书章节", dataType = "BookSection")
    private Long bookSectionId;

    @NotBlank(message = "内容不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "内容", dataType = "String")
    @Column(columnDefinition = "mediumtext")
    private String content;

    @ApiModelProperty(value = "读者", dataType = "Reader")
    private Long readerId;

    @PostPersist
    private void postPersist() {
        this.readerId = super.createUserId;
    }

    @ApiModelProperty(value = "回复数量", dataType = "Long")
    private Long replyNum = 0L;

    @ApiModelProperty(value = "评论等级", dataType = "Integer")
    private Integer level = 1;

    @ApiModelProperty(value = "点赞数量", dataType = "Long")
    private Long likeNum = 0L;

}
