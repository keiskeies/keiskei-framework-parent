package top.keiskeiframework.common.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.util.SecurityUtils;

import javax.persistence.*;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 01:15
 */
@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseIntegerIdEntity extends SuperEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
}
