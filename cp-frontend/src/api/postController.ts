// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /post/add */
export async function addPost(body: API.PostAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/post/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /post/delete/${param0} */
export async function deletePost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deletePostParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/post/delete/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /post/edit */
export async function editPost(body: API.PostEditRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/post/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 切换帖子精选状态 POST /post/essence/${param0} */
export async function toggleEssence(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.toggleEssenceParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/post/essence/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 收藏/取消收藏帖子 POST /post/favorite/${param0} */
export async function toggleFavorite(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.toggleFavoriteParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/post/favorite/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取我的收藏帖子 GET /post/favorite/my */
export async function getMyFavoritePosts(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMyFavoritePostsParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePostVO>('/post/favorite/my', {
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

/** 检查是否已收藏 GET /post/favorite/status/${param0} */
export async function hasFavorited(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.hasFavoritedParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/post/favorite/status/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取用户收藏的帖子 GET /post/favorite/user/${param0} */
export async function getUserFavoritePosts(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserFavoritePostsParams,
  options?: { [key: string]: any }
) {
  const { userId: param0, ...queryParams } = params
  return request<API.BaseResponsePagePostVO>(`/post/favorite/user/${param0}`, {
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

/** 此处后端没有提供注释 GET /post/get/${param0} */
export async function getPostById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPostByIdParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponsePostVO>(`/post/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 点赞/取消点赞帖子 POST /post/like/${param0} */
export async function toggleLike(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.toggleLikeParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/post/like/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取帖子点赞数量 GET /post/like/count/${param0} */
export async function getLikeCount(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLikeCountParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseLong>(`/post/like/count/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 检查是否已点赞 GET /post/like/status/${param0} */
export async function hasLiked(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.hasLikedParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/post/like/status/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /post/list/page */
export async function queryPostPage(body: API.PostQueryRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponsePagePostVO>('/post/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /post/my */
export async function getUserPosts(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserPostsParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePostVO>('/post/my', {
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

/** 此处后端没有提供注释 GET /post/user/${param0} */
export async function getUserPostsByUserId(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserPostsByUserIdParams,
  options?: { [key: string]: any }
) {
  const { userId: param0, ...queryParams } = params
  return request<API.BaseResponsePagePostVO>(`/post/user/${param0}`, {
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
