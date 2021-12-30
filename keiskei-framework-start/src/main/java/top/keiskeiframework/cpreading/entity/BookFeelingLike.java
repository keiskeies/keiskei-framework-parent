package top.keiskeiframework.cpreading.entity;

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

import javax.persistence.*;
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
@Table(name = "cpreading_book_feeling_like")
@ApiModel(value="BookFeelingLike", description="图书感悟点赞")
public class BookFeelingLike extends ListEntity<Long> {

    private static final long serialVersionUID = -3275305891050571851L;

    @NotNull(message = "图书不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "评论", dataType="BookFeeling")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private BookFeeling bookFeeling;

    @ApiModelProperty(value = "读者", dataType="Reader")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Reader reader;

    @PostPersist
    private void postPersist() {
        Reader reader = new Reader();
        reader.setId(super.createUserId);
        this.reader = reader;
    }

}
