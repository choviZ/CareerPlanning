// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /assessment/addQuestion */
export async function addQuestion(body: API.AddQuestionRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/assessment/addQuestion', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /assessment/del/${param0} */
export async function deleteQuestion(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteQuestionParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/assessment/del/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /assessment/do */
export async function doAssessment(
  body: API.DoAssessmentRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseAssessmentResultVo>('/assessment/do', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /assessment/list */
export async function queryQuestion(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.queryQuestionParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListAssessmentQuestionVo>('/assessment/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /assessment/updateQuestion */
export async function updateQuestion(
  body: API.UpdateQuestionRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/assessment/updateQuestion', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
