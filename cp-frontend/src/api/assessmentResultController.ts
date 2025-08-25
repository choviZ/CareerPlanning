// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /assessmentResult/add */
export async function addAssessmentResult(
  body: API.AddAssessmentResultRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/assessmentResult/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /assessmentResult/delete/${param0} */
export async function deleteAssessmentResult(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteAssessmentResultParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/assessmentResult/delete/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /assessmentResult/get/${param0} */
export async function getAssessmentResultById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAssessmentResultByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseAssessmentResultVo>(`/assessmentResult/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /assessmentResult/list/page */
export async function listAssessmentResultByPage(
  body: API.QueryAssessmentResultRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageAssessmentResultVo>('/assessmentResult/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /assessmentResult/update */
export async function updateAssessmentResult(
  body: API.UpdateAssessmentResultRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/assessmentResult/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
