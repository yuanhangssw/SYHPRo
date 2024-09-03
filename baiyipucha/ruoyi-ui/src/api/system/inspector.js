import request from '@/utils/request'

// 查询巡查员用户列表
export function listInspector (query) {
  return request({
    url: '/system/inspector/list',
    method: 'get',
    params: query
  })
}

// 查询巡查员用户详细
export function getInspector (id) {
  return request({
    url: '/system/inspector/' + id,
    method: 'get'
  })
}

// 新增巡查员用户
export function addInspector (data) {
  return request({
    url: '/system/inspector',
    method: 'post',
    data: data
  })
}

// 修改巡查员用户
export function updateInspector (data) {
  return request({
    url: '/system/inspector',
    method: 'put',
    data: data
  })
}

// 删除巡查员用户
export function delInspector (id) {
  return request({
    url: '/system/inspector/' + id,
    method: 'delete'
  })
}

// 查询巡查员用户下的单元
// /bInspectorUnit/unit/list
export function blistUnit (query) {
  return request({
    url: '/bInspectorUnit/unit/list',
    method: 'get',
    params: query
  })
}