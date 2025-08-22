// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /career/add */
export async function addCareer(body: API.CareerAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/career/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /career/delete/${param0} */
export async function deleteCareer(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteCareerParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/career/delete/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /career/get/${param0} */
export async function getCareerById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCareerByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseCareer>(`/career/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /career/list/page */
export async function listCareerByPage(
  body: API.CareerQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageCareer>('/career/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /career/update */
export async function updateCareer(
  body: API.CareerUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/career/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
