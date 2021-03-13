import request from '@/utils/request'
<#assign hasStatus=false>
<#assign hasChild=false>
<#assign hasSort=false>
<#list table.fields as field><#if field.propertyName=="status"><#assign hasStatus=true></#if><#if field.propertyName=="parentId"><#assign hasChild=true></#if><#if field.propertyName=="sortBy"><#assign hasSort=true></#if></#list>
<#if !hasChild>
<#if table.fields!?size gt cfg.minExport>
import downloadExcel from '@/utils/export'
</#if>
</#if>


export function getList(params) {
  return request({
    url: '/${cfg.cjmModuleName}/${entity?uncap_first}',
    method: 'get',
    params
  })
}

export function getOptions(params) {
  return request({
    url: '/${cfg.cjmModuleName}/${entity?uncap_first}/options',
    method: 'get',
    params
  })
}

export function create(data) {
  return request({
    url: '/${cfg.cjmModuleName}/${entity?uncap_first}',
    method: 'post',
    data
  })
}

export function detail(id) {
  return request({
    url: '/${cfg.cjmModuleName}/${entity?uncap_first}/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: '/${cfg.cjmModuleName}/${entity?uncap_first}',
    method: 'put',
    data
  })
}

<#if hasSort>
  export function changeSort(data) {
    return request({
      url: '/${cfg.cjmModuleName}/${entity?uncap_first}/sort',
      method: 'patch',
      data
    })
  }

</#if>
export function updateSate(id, state) {
  return request({
    url: '/${cfg.cjmModuleName}/${entity?uncap_first}/status/' + state,
    method: 'patch',
    data: {
      id: id
    }
  })
}

export function deleteById(id) {
  return request({
    url: '/${cfg.cjmModuleName}/${entity?uncap_first}/' + id,
    method: 'delete'
  })
}

export function exportExcel(params) {
  request({
    url: '/${cfg.cjmModuleName}/${entity?uncap_first}/export',
    responseType: 'blob',
    method: 'get',
    params
  }).then(response => {
    downloadExcel(response)
  })
}
