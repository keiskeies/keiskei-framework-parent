package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author cjm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_department_level")
@TableName(value = "sys_department_level")
@ApiModel(value = "DepartmentLevel", description = "组织级别")
public class DepartmentLevel extends ListEntityImpl<Integer> implements IListEntity<Integer> {

    private static final long serialVersionUID = 5579562081000299744L;

    @OrderBy(asc = true)
    private Integer levelNum;

    private String name;


}
