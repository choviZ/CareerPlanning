// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /analyse/resourceOverview */
export async function getResourceOverview(options?: { [key: string]: any }) {
  return request<API.BaseResponseResourceOverviewDTO>('/analyse/resourceOverview', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /analyse/typeDistribution */
export async function getTypeDistribution(options?: { [key: string]: any }) {
  return request<API.BaseResponseListResourceTypeDistributionDTO>('/analyse/typeDistribution', {
    method: 'GET',
    ...(options || {}),
  })
}
