// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /result-career/add */
export async function addResultCareerMapping(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addResultCareerMappingParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/result-career/add', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /result-career/best-match */
export async function queryBestCompatibleCareer(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.queryBestCompatibleCareerParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseResultCareerMapping>('/result-career/best-match', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /result-career/delete/${param0} */
export async function deleteResultCareerMapping(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteResultCareerMappingParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/result-career/delete/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /result-career/list/page */
export async function queryMappingByResultCode(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.queryMappingByResultCodeParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResultCareerMapping>('/result-career/list/page', {
    method: 'GET',
    params: {
      // pageNum has a default value: 1
      pageNum: '1',
      // pageSize has a default value: 10
      pageSize: '10',
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /result-career/update */
export async function updateResultCareerMapping(
  body: API.UpdateResultCareerRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/result-career/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
