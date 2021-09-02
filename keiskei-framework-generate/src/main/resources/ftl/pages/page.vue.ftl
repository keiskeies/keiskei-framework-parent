<template>
  <div class="app-container">
    <div class="table-container">
      <base-list ref="table" url="/${module.path?uncap_first}/${table.name?uncap_first}" :options="options" :columns="columns" :format="format" :rules="rules"
                 @reloadOptions="handleGetOptions" permission="${module.path?uncap_first}:${table.name?uncap_first}" <#if table.type == "TREE">treeTable</#if>></base-list>
    </div>
  </div>
</template>

<script>
  import {getBaseList, getBaseDetail, getBaseOptions, addBase, editBase, deleteBase} from '@/api/common'
  import BaseList from '@/components/BaseList'

  export default {
    name: '${module.path?cap_first}${table.name}',
    components: {BaseList},
    data() {
      return {
        columns: [
<#if table.type == "TREE">
          {key: 'parentId', label: '上级数据', show: false, edit: true, queryFlag: false, sortable: false, width: 200,type:'TREE_SELECT',tooltip: '', optionKey: '${table.name?uncap_first}Options'},
</#if>
<#list table.fields as field>
  <#if !field.jsonIgnore>
          {key: '${field.name}', label: '${field.comment}', show: ${field.jsonIgnore?string}, edit: ${field.editAble?string}, queryFlag: ${field.queryAble?string}, sortable: ${field.sortAble?string}, width: ${field.tableWidth},type:<#if field.type == "MIDDLE_ID"><#if field.relation == "ONE_TO_ONE" || field.relation == "MANY_TO_ONE"><#if treeTables?seq_contains(field.relationEntity)>"TREE_SELECT",<#else>"SELECT",</#if><#elseif field.relation == "ONE_TO_MANY" || field.relation == "MANY_TO_MANY" ><#if treeTables?seq_contains(field.relationEntity)>"TREE_MULTI_SELECT",<#else>"MULTI_SELECT",</#if></#if><#else>'${field.type}',</#if><#if field.type == "MIDDLE_ID">optionKey: '${field.relationEntity?uncap_first}Options', <#elseif field.type == "DICTIONARY">optionKey: '${table.name?uncap_first}${field.name?cap_first}Options', </#if>tooltip: '${field.tooltip!}'},
  </#if>
</#list>
        ],
        format: {
<#list table.fields as field>
  <#if field.type == "DICTIONARY">
          ${field.name}: (data, index) => {return (options.${table.name?uncap_first}${field.name?cap_first}Options.find(e => e.id === data.${field.name}) || {name: ''}).name},
  <#elseif field.type == "MIDDLE_ID">
    <#if field.relation == "ONE_TO_ONE" || field.relation == "MANY_TO_ONE">
          ${field.name}: (data, index) => {return data.name},
    <#elseif field.relation == "ONE_TO_MANY" || field.relation == "MANY_TO_MANY" >
          ${field.name}: (data, index) => {return data.map(e => e.name).join(',')},
    </#if>
  </#if>
</#list>
        },
        rules: {
          add: {
<#list table.fields as field>
            ${field.name}: [<#if field.createRequire><#assign type=field.type?contains("MONNY") || field.type.value?contains("String")/>{required: true, message: '${field.comment}不能为空', trigger: '${type?string('change','blur')}'},</#if><#if field.type?contains("MONNY")>{type: 'number', message: '${field.comment}为数字值'},</#if><#if field.validate??>{pattern: ${field.validate!}, message: '${field.comment}数据格式错误' },</#if>],
</#list>
          },
          edit: {
<#list table.fields as field>
            ${field.name}: [<#if field.updateRequire><#assign type=field.type?contains("MONNY") || field.type.value?contains("String")/>{required: true, message: '${field.comment}不能为空', trigger: '${type?string('change','blur')}'},</#if><#if field.type?contains("MONNY")>{type: 'number', message: '${field.comment}为数字值'},</#if><#if field.validate??>{pattern: ${field.validate!}, message: '${field.comment}数据格式错误' },</#if>],
</#list>
          }
        },
        options: {
<#list table.fields as field>
    <#if field.type == "DICTIONARY">
          ${table.name?uncap_first}${field.name?cap_first}Options: [
      <#list field.fieldEnums as enum>
            {id: '${enum.name}', name: '${enum.comment}', type: '${enum.type?lower_case}', effect: '${enum.effect?lower_case}'},
      </#list>
          ],
    <#elseif field.type == "MIDDLE_ID">
          ${field.relationEntity?uncap_first}Options: [],
    </#if>
</#list>
<#if table.type == "TREE">
          ${table.name?uncap_first}Options: []
</#if>
        }
      }
    },

    created() {
      this.handleGetOptions()
    },
    mounted() {
    },
    methods: {
      handleGetOptions() {
        const options = JSON.parse(localStorage.getItem('allOptions'))
        if (options) {
          this.options = Object.assign(this.options, options)
        }
        const self = this
<#list table.fields as field>
  <#if field.type == "MIDDLE_ID">
        getBaseOptions('/${tableModuleMap[field.relationEntity]!''}/${field.relationEntity?uncap_first}',{}).then(res => {
          self.options.${field.relationEntity?uncap_first}Options = res.data
        })
  </#if>
</#list>
<#if table.type == "TREE">
        getBaseOptions('/${module.path?uncap_first}/${table.name?uncap_first}',{}).then(res => {
          self.options.${table.name?uncap_first}Options = res.data
        })
</#if>
      }
    }
  }
</script>

