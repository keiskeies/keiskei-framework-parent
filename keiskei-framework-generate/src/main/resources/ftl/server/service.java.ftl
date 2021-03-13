package ${module.packageName}.service;

<#assign parentNam = table.type?lower_case?cap_first>
import top.keiskeiframework.common.base.service.${parentNam}Service;
import ${module.packageName}.entity.${table.name};

/**
 * <p>
 * ${table.comment!} 业务接口层
 * </p>
 *
 * @author ${author}
 * @since ${since}
 */
public interface I${table.name}Service extends ${parentNam}Service<${table.name}> {

}
