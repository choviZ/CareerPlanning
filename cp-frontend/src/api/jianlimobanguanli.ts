// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 添加简历模板 POST /resumeTemplate/add */
export async function addResumeTemplate(
  body: API.ResumeTemplateAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong>('/resumeTemplate/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取可用的简历模板列表 GET /resumeTemplate/available */
export async function getAvailableTemplates(options?: { [key: string]: any }) {
  return request<API.BaseResponseListResumeTemplateVo>('/resumeTemplate/available', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 删除简历模板 POST /resumeTemplate/delete */
export async function deleteResumeTemplate(body: number, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/resumeTemplate/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据ID查询简历模板 GET /resumeTemplate/get/${param0} */
export async function queryResumeTemplateById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.queryResumeTemplateByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseResumeTemplateVo>(`/resumeTemplate/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 分页查询简历模板 POST /resumeTemplate/list/page */
export async function queryResumeTemplate(
  body: API.ResumeTemplateQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResumeTemplateVo>('/resumeTemplate/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 启用/禁用模板 POST /resumeTemplate/status/${param0}/${param1} */
export async function updateTemplateStatus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateTemplateStatusParams,
  options?: { [key: string]: any }
) {
  const { id: param0, status: param1, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/resumeTemplate/status/${param0}/${param1}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 更新简历模板 POST /resumeTemplate/update */
export async function updateResumeTemplate(
  body: API.ResumeTemplateUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/resumeTemplate/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
