package ${module.packageName}.service.impl;

<#assign parentNam = table.type?lower_case?cap_first>
import top.keiskeiframework.common.base.service.impl.${parentNam}ServiceImpl;
import ${module.packageName}.entity.${table.name};
import ${module.packageName}.repository.${table.name}Repository;
import ${module.packageName}.service.I${table.name}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 业务实现层
 * </p>
 *
 * @author ${author}
 * @since ${since}
 */
@Service
public class I${table.name}ServiceImpl extends ${parentNam}ServiceImpl<${table.name}> implements I${table.name}Service {

    @Autowired
    private ${table.name}Repository ${table.name?uncap_first}Repository;

}
