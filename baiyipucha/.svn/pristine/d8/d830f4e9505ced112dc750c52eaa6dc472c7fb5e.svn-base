import request from '@/utils/request'

// 查询审核记录列表
export function listAudit (query) {
  return request({
    url: '/system/audit/checklist',
    method: 'get',
    params: query
  })
}

// 查询审核记录详细
export function getAudit (id) {
  return request({
    url: '/system/audit/' + id,
    method: 'get'
  })
}

// 新增审核记录
export function addAudit (data) {
  return request({
    url: '/system/audit',
    method: 'post',
    data: data
  })
}

// 修改审核记录
export function updateAudit (data) {
  return request({
    url: '/system/audit',
    method: 'put',
    data: data
  })
}

// 删除审核记录
export function delAudit (id) {
  return request({
    url: '/system/audit/' + id,
    method: 'delete'
  })
}

// /system/patrol/{id}
// 获取巡查详情
export function getPatrolDetail (id) {
  return request({
    url: '/system/patrol/' + id,
    method: 'get'
  })
}