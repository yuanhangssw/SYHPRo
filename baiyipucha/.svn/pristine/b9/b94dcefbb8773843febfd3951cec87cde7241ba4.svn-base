import request from '@/utils/request'

// 查询项目信息列表
export function listProject (query) {
  return request({
    url: '/system/project/list',
    method: 'get',
    params: query
  })
}

// 查询项目信息详细
export function getProject (id) {
  return request({
    url: '/system/project/' + id,
    method: 'get'
  })
}

// 新增项目信息
export function addProject (data) {
  return request({
    url: '/system/project',
    method: 'post',
    data: data
  })
}

// 修改项目信息
export function updateProject (data) {
  return request({
    url: '/system/project',
    method: 'put',
    data: data
  })
}

// 删除项目信息
export function delProject (id) {
  return request({
    url: '/system/project/' + id,
    method: 'delete'
  })
}

//审批及分配 查询项目信息
// /system/project/listbyDeptId
export function listbyDeptId (query) {
  return request({
    url: '/system/project/listbyDeptId',
    method: 'get',
    params: query
  })
}

//审批及分配 查询项目信息下的单元
// /bPatrol/unit/list
export function listUnit (query) {
  return request({
    url: '/bPatrol/unit/list',
    method: 'get',
    params: query
  })
}

//提交分配单元
// /bInspectorUnit/unit/addInspectorUnit
export function addInspectorUnit (data) {
  return request({
    url: '/bInspectorUnit/unit/addInspectorUnit',
    method: 'post',
    data: data
  })
}
