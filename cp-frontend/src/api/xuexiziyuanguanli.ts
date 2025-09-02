// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 添加学习资源 POST /learningResource/add */
export async function addLearningResource(
  body: API.LearningResourceAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong>('/learningResource/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据分类查询学习资源 GET /learningResource/category/${param0} */
export async function getLearningResourcesByCategory(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLearningResourcesByCategoryParams,
  options?: { [key: string]: any }
) {
  const { category: param0, ...queryParams } = params
  return request<API.BaseResponsePageLearningResourceVO>(`/learningResource/category/${param0}`, {
    method: 'GET',
    params: {
      // current has a default value: 1
      current: '1',
      // pageSize has a default value: 10
      pageSize: '10',
      ...queryParams,
    },
    ...(options || {}),
  })
}

/** 删除学习资源 POST /learningResource/delete/${param0} */
export async function deleteLearningResource(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteLearningResourceParams,
  options?: { [key: string]: any }
) {
  const { resourceId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/learningResource/delete/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 编辑学习资源 POST /learningResource/edit */
export async function editLearningResource(
  body: API.LearningResourceEditRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/learningResource/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取学习资源详情 GET /learningResource/get/${param0} */
export async function getLearningResourceById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLearningResourceByIdParams,
  options?: { [key: string]: any }
) {
  const { resourceId: param0, ...queryParams } = params
  return request<API.BaseResponseLearningResourceVO>(`/learningResource/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 分页查询学习资源 POST /learningResource/list/page */
export async function queryLearningResourcePage(
  body: API.LearningResourceQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageLearningResourceVO>('/learningResource/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 搜索学习资源 GET /learningResource/search */
export async function searchLearningResources(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchLearningResourcesParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageLearningResourceVO>('/learningResource/search', {
    method: 'GET',
    params: {
      // current has a default value: 1
      current: '1',
      // pageSize has a default value: 10
      pageSize: '10',
      ...params,
    },
    ...(options || {}),
  })
}

/** 切换学习资源置顶状态 POST /learningResource/top/${param0} */
export async function toggleTopStatus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.toggleTopStatusParams,
  options?: { [key: string]: any }
) {
  const { resourceId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/learningResource/top/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}
