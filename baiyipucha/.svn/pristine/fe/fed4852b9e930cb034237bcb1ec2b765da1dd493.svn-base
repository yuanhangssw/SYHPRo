import request from '@/utils/request'

// 查询巡查单元列表
export function listUnit (query) {
  return request({
    url: '/bPatrol/unit/list',
    method: 'get',
    params: query
  })
}

// 查询巡查单元详细
export function getUnit (id) {
  return request({
    url: '/bPatrol/unit/' + id,
    method: 'get'
  })
}

// 新增巡查单元
export function addUnit (data) {
  return request({
    url: '/bPatrol/unit',
    method: 'post',
    data: data
  })
}

// 修改巡查单元
export function updateUnit (data) {
  return request({
    url: '/bPatrol/unit',
    method: 'put',
    data: data
  })
}

// 删除巡查单元
export function delUnit (id) {
  return request({
    url: '/bPatrol/unit/' + id,
    method: 'delete'
  })
}

// 巡查记录
// /system/patrol/list
export function listPatrol (query) {
  return request({
    url: '/system/patrol/list',
    method: 'get',
    params: query
  })
}

// 填报查询接口
// /system/fill/list
export function listFill (query) {
  return request({
    url: '/system/fill/list',
    method: 'get',
    params: query
  })
}