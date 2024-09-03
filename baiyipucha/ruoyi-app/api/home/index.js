import request from "@/utils/request";

// 获取项目列表
export function getProjects(userid) {
  return request({
    url: "/rest/getprojectlistbyuser?userid=" + userid,
    method: "get",
  });
}

// 获取首页统计信息
export function getHomeInfo(params) {
  return request({
    url:
      "/rest/indexinfo?userid=" +
      params.userId +
      "&projectid=" +
      params.projectId,
    method: "get",
  });
}
