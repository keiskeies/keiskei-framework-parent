package ${module.packageName}.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<#assign parentName = table.type?lower_case?cap_first>
import top.keiskeiframework.common.base.controller.*;

import ${module.packageName}.entity.${table.name};
import ${module.packageName}.service.I${table.name}Service;

/**
 * <p>
 * ${table.comment!} controller层
 * </p>
 *
 * @author ${author}
 * @since ${since}
 */
@RestController
@RequestMapping("/admin/v2/${module.path?uncap_first}/${table.name?uncap_first}")
@Api(tags = "${module.comment!} - ${table.comment!}")
public class ${table.name}Controller extends ${parentName}Controller<${table.name}>{

    @Autowired
    private I${table.name}Service ${table.name?uncap_first}Service;



}
