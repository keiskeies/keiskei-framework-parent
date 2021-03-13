<template>
  <div class="app-container">
    <div class="table-container">
      <base-list ref="table" url="/${cfg.module}/${table.name?uncap_first}" :options="options" :columns="columns" :format="format" :rules="rules"
                 @reloadOptions="handleGetOptions" permission="${cfg.module}:${table.name?uncap_first}" :treeTable="${table.type == 'TREE'}"></base-list>
    </div>
  </div>
</template>

<script>
  import {getBaseList, getBaseDetail, getBaseOptions, addBase, editBase, deleteBase} from '@/api/common'
  import BaseList from '@/components/BaseList'

  export default {
    name: '${cfg.module?cap_first}${table.name}',
    components: {BaseList},
    data() {
      return {
        columns: [
<#list table.fields as field>
  <#if !field.jsonIgnore>
          {show: ${field.show}, edit: ${field.edit}, queryFlag: ${field.queryColumn}, sortable: ${field.sortable}, width: ${field.tableWidth}, type: '${field.type}', key: '${field.name}', label: '${field.comment}', <#if field.oneToOne??>optionKey: '${field.oneToOne?uncap_first}Options', </#if><#if field.manyToMany??>optionKey: '${field.manyToMany?uncap_first}Options', </#if><#if field.type?contains('SELECT')>optionKey: '${field.name}Options', </#if>tooltip: '${field.tooltip}'},
  </#if>
</#list>
        ],
        format: {
<#list table.fields as field>
  <#if field.type =='SELECT'>
          ${field.name}: (data, index) => {return (options.${field.name}Options.find(e => e.id === data.${field.name}) || {name: ''}).name},
  </#if>
  <#if field.type =='MULTI_SELECT'>
          ${field.name}: (data, index) => {return options.${field.name}Options.filter(e => data.${field.name}.includes(e.id)).map(e => e.name).join(',')},
  </#if>
</#list>
        },
        rules: {
          add: {
<#list table.fields as field>
  <#if !field.keyFlag && field.edit && field.createRequire>
            <#assign type=field.type?contains('SELECT')/>
            ${field.name} : [{required: true, message: '${field.comment}不能为空', trigger: '${type?string('change','blur')}'}],
  </#if>
</#list>
          },
          edit: {
<#list table.fields as field>
  <#if !field.keyFlag && field.edit && field.updateRequire>
            <#assign type=field.type?contains('SELECT')/>
            ${field.name} : [{required: true, message: '${field.comment}不能为空', trigger: '${type?string('change','blur')}'}],
  </#if>
</#list>
          }
        },
        options: {}
      }
    },

    created() {
      this.handleGetOptions()
    },
    mounted() {
    },
    methods: {
      handleGetOptions() {
        this.options = JSON.parse(localStorage.getItem('allOptions')) || this.options
<#list table.fields as field>
  <#if field.oneToOne??>
        getBaseOptions('/${cfg.tableModuleMap[field.oneToOne]!''}/${field.oneToOne?uncap_first}',{}).then(res => {
          this.options.${field.oneToOne?uncap_first}Options = res.data
        })
  </#if>
  <#if field.manyToMany??>
        getBaseOptions('/${cfg.tableModuleMap[field.manyToMany]!''}/${field.manyToMany?uncap_first}',{}).then(res => {
          this.options.${field.manyToMany?uncap_first}Options = res.data
        })
  </#if>
</#list>
      }
    }
  }
</script>

