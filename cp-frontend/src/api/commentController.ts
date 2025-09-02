// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /comment/add */
export async function addComment(body: API.CommentAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/comment/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /comment/delete */
export async function deleteComment(
  body: API.CommentDeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/comment/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /comment/hasLiked/${param0} */
export async function hasLikedComment(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.hasLikedCommentParams,
  options?: { [key: string]: any }
) {
  const { commentId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/comment/hasLiked/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /comment/like/${param0} */
export async function likeComment(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.likeCommentParams,
  options?: { [key: string]: any }
) {
  const { commentId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/comment/like/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /comment/list/page */
export async function listCommentByPage(
  body: API.CommentQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageCommentVO>('/comment/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /comment/post/${param0} */
export async function getPostComments(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPostCommentsParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseListCommentVO>(`/comment/post/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /comment/replies/${param0} */
export async function getCommentReplies(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCommentRepliesParams,
  options?: { [key: string]: any }
) {
  const { commentId: param0, ...queryParams } = params
  return request<API.BaseResponseListCommentVO>(`/comment/replies/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
