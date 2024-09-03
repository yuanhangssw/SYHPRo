import request from "@/utils/request";

export function uploadFile(fielPath) {
  return request({
    url: "/common/upload",
    method: "post",
    data: fielPath,
  });
}

// 添加巡查
export function addPatrol(params) {
  return request({
    url: "/system/patrol",
    method: "post",
    data: params,
  });
}

// 修改巡查信息
export function editPatrol(params) {
  return request({
    url: "/system/patrol",
    method: "put",
    data: params,
  });
}

// 获取项目下所有单元列表
export function getUnits(params) {
  return request({
    url:
      "/rest/unitlist?userid=" +
      params.userId +
      "&projectid=" +
      params.projectId,
    method: "get",
  });
}

// 新增巡查信息时，获取巡查单元列表
export function getUnits2(params) {
  return request({
    url: "/rest/unitlist2",
    method: "get",
    params: params,
  });
}

// 获取巡查单元详情
export function getUnitInfo(params) {
  return request({
    url: "/system/patrol/list",
    method: "get",
    params: params,
  });
}

// 获取巡查详情
export function getPatrol(id) {
  return request({
    url: "/system/patrol/" + id,
    method: "get",
  });
}

// 获取巡查数量
export function getPatrolTotalByStatus(params) {
  return request({
    url: "/rest/getdatatotalbystatus",
    method: "get",
    params: params,
  });
}

// 查询单元填报信息
export function getUnitFill(params) {
  return request({
    url: "/system/fill/list",
    method: "get",
    params: params,
  });
}

// 添加单元
export function addUnit(params) {
  return request({
    url: "/system/fill",
    method: "post",
    data: params,
  });
}

// 修改单元信息
// 添加单元
export function editUnit(params) {
  return request({
    url: "/system/fill",
    method: "put",
    data: params,
  });
}

// 获取巡查类型 patrolType:1 白蚁   2 獾狐鼠
export function getPatrolTypeList(patrolType) {
  return request({
    url: "/bPatrol/type/list?patrolType=" + patrolType,
    method: "get",
  });
}
