/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout'

const ${module.path?uncap_first}Router = {
    path: '/${module.path?uncap_first}',
    component: Layout,
    redirect: '/${module.path?uncap_first}/<#list module.tables as table><#if table_index = 0>${table.name?uncap_first}</#if></#list>',
    name: '${module.comment}',
    meta: { title: '${module.comment}', icon: 'example', systemRole: '${module.path?uncap_first}' },
    children: [
<#list module.tables as table>
        {
            path: '/${module.path?uncap_first}/${table.name?uncap_first}',
            name: '${table.comment}',
            component: () => import('@/views/${module.path?uncap_first}/${table.name?uncap_first}/index'),
            meta: { title: '${table.comment}', icon: 'table', systemRole: '${module.path?uncap_first}:${table.name?uncap_first}' }
        },
</#list>
    ]
}

export default ${module.path?uncap_first}Router
