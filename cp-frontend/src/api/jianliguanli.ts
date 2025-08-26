// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /resume/add */
export async function addResume(body: API.ResumeAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/resume/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /resume/admin/delete/${param0} */
export async function adminDeleteResume(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.adminDeleteResumeParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/resume/admin/delete/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /resume/admin/get/${param0} */
export async function adminGetResumeById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.adminGetResumeByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseResumeVo>(`/resume/admin/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /resume/admin/list/page */
export async function adminListResumeByPage(
  body: API.ResumeQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResumeVo>('/resume/admin/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /resume/copy/${param0} */
export async function copyResume(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.copyResumeParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseLong>(`/resume/copy/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /resume/delete/${param0} */
export async function deleteResume(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteResumeParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/resume/delete/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /resume/get/${param0} */
export async function getResumeById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getResumeByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseResumeVo>(`/resume/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /resume/list/page */
export async function listResumeByPage(
  body: API.ResumeQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResumeVo>('/resume/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /resume/publish/${param0} */
export async function publishResume(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.publishResumeParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/resume/publish/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /resume/share/${param0} */
export async function getResumeByShareCode(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getResumeByShareCodeParams,
  options?: { [key: string]: any }
) {
  const { shareCode: param0, ...queryParams } = params
  return request<API.BaseResponseResumeVo>(`/resume/share/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /resume/share/generate/${param0} */
export async function generateShareCode(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.generateShareCodeParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseString>(`/resume/share/generate/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /resume/update */
export async function updateResume(
  body: API.ResumeUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/resume/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
