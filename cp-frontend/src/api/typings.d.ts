declare namespace API {
  type AddAssessmentResultRequest = {
    testType?: string
    resultCode?: string
    resultName?: string
    resultDesc?: string
  }

  type AddQuestionRequest = {
    id?: number
    testType?: string
    content?: string
    dimension?: string
    status?: number
    options?: OptionDTO
    sortOrder?: number
  }

  type addResultCareerMappingParams = {
    assessmentResultId: number
    careerId: number
    compatibilityScore: number
  }

  type adminDeleteResumeParams = {
    id: number
  }

  type adminGetResumeByIdParams = {
    id: number
  }

  type AssessmentQuestionVo = {
    id?: number
    testType?: string
    content?: string
    dimension?: string
    options?: OptionDTO[]
    sortOrder?: number
    status?: number
    createdAt?: string
    updatedAt?: string
  }

  type AssessmentResultVo = {
    id?: number
    testType?: string
    resultCode?: string
    resultName?: string
    resultDesc?: string
    createdAt?: string
    updatedAt?: string
    isDeleted?: number
  }

  type BaseResponseAssessmentResultVo = {
    code?: number
    data?: AssessmentResultVo
    message?: string
  }

  type BaseResponseBoolean = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseCareerVo = {
    code?: number
    data?: CareerVo
    message?: string
  }

  type BaseResponseLearningResourceVO = {
    code?: number
    data?: LearningResourceVO
    message?: string
  }

  type BaseResponseListAssessmentQuestionVo = {
    code?: number
    data?: AssessmentQuestionVo[]
    message?: string
  }

  type BaseResponseListCommentVO = {
    code?: number
    data?: CommentVO[]
    message?: string
  }

  type BaseResponseListResumeTemplateVo = {
    code?: number
    data?: ResumeTemplateVo[]
    message?: string
  }

  type BaseResponseLong = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponsePageAssessmentResultVo = {
    code?: number
    data?: PageAssessmentResultVo
    message?: string
  }

  type BaseResponsePageCareerVo = {
    code?: number
    data?: PageCareerVo
    message?: string
  }

  type BaseResponsePageCommentVO = {
    code?: number
    data?: PageCommentVO
    message?: string
  }

  type BaseResponsePageLearningResourceVO = {
    code?: number
    data?: PageLearningResourceVO
    message?: string
  }

  type BaseResponsePagePostVO = {
    code?: number
    data?: PagePostVO
    message?: string
  }

  type BaseResponsePageResultCareerMapping = {
    code?: number
    data?: PageResultCareerMapping
    message?: string
  }

  type BaseResponsePageResumeTemplateVo = {
    code?: number
    data?: PageResumeTemplateVo
    message?: string
  }

  type BaseResponsePageResumeVo = {
    code?: number
    data?: PageResumeVo
    message?: string
  }

  type BaseResponsePageUser = {
    code?: number
    data?: PageUser
    message?: string
  }

  type BaseResponsePostVO = {
    code?: number
    data?: PostVO
    message?: string
  }

  type BaseResponseResultCareerMapping = {
    code?: number
    data?: ResultCareerMapping
    message?: string
  }

  type BaseResponseResumeTemplateVo = {
    code?: number
    data?: ResumeTemplateVo
    message?: string
  }

  type BaseResponseResumeVo = {
    code?: number
    data?: ResumeVo
    message?: string
  }

  type BaseResponseString = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseUserAssessmentVo = {
    code?: number
    data?: UserAssessmentVo
    message?: string
  }

  type BaseResponseUserLoginVO = {
    code?: number
    data?: UserLoginVO
    message?: string
  }

  type BaseResponseUserVO = {
    code?: number
    data?: UserVO
    message?: string
  }

  type BasicInfo = {
    name?: string
    phone?: string
    email?: string
    avatar?: string
    birthDate?: string
    gender?: number
    address?: string
    jobIntention?: string
    selfIntroduction?: string
  }

  type CareerAddRequest = {
    name?: string
    description?: string
    requiredSkills?: string
    jobOutlook?: string
    averageSalary?: string
  }

  type CareerQueryRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    name?: string
    description?: string
    requiredSkills?: string
    jobOutlook?: string
    averageSalary?: string
  }

  type CareerUpdateRequest = {
    id?: number
    name?: string
    description?: string
    requiredSkills?: string
    jobOutlook?: string
    averageSalary?: string
  }

  type CareerVo = {
    id?: number
    name?: string
    description?: string
    requiredSkills?: string
    jobOutlook?: string
    averageSalary?: string
    createdAt?: string
    updatedAt?: string
    isDeleted?: boolean
  }

  type CommentAddRequest = {
    postId?: number
    parentId?: number
    replyToUserId?: number
    content?: string
  }

  type CommentDeleteRequest = {
    id?: number
  }

  type CommentQueryRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    postId?: number
    parentId?: number
    rootId?: number
    userId?: number
    content?: string
    level?: number
  }

  type CommentVO = {
    id?: number
    postId?: number
    userId?: number
    user?: UserVO
    parentId?: number
    rootId?: number
    replyToUserId?: number
    replyToUser?: UserVO
    content?: string
    likeCount?: number
    replyCount?: number
    level?: number
    path?: string
    createdAt?: string
    hasLiked?: boolean
  }

  type copyResumeParams = {
    id: number
  }

  type deleteAssessmentResultParams = {
    id: number
  }

  type deleteCareerParams = {
    id: number
  }

  type deleteLearningResourceParams = {
    resourceId: number
  }

  type deletePostParams = {
    postId: number
  }

  type deleteQuestionParams = {
    id: number
  }

  type deleteResultCareerMappingParams = {
    id: number
  }

  type deleteResumeParams = {
    id: number
  }

  type deleteUserParams = {
    id: number
  }

  type DoAssessmentRequest = {
    testType?: string
    userAnswers?: string[]
  }

  type Education = {
    schoolName?: string
    major?: string
    degree?: string
    startDate?: string
    endDate?: string
    isCurrent?: boolean
    description?: string
    sortOrder?: number
  }

  type generateShareCodeParams = {
    id: number
  }

  type getAssessmentResultByIdParams = {
    id: number
  }

  type getCareerByIdParams = {
    id: number
  }

  type getCommentRepliesParams = {
    commentId: number
  }

  type getLearningResourceByIdParams = {
    resourceId: number
  }

  type getLearningResourcesByCategoryParams = {
    category: string
    current?: number
    pageSize?: number
  }

  type getLikeCountParams = {
    postId: number
  }

  type getMyFavoritePostsParams = {
    current?: number
    pageSize?: number
  }

  type getPostByIdParams = {
    postId: number
  }

  type getPostCommentsParams = {
    postId: number
  }

  type getResumeByIdParams = {
    id: number
  }

  type getResumeByShareCodeParams = {
    shareCode: string
  }

  type getUserByIdParams = {
    id: number
  }

  type getUserFavoritePostsParams = {
    userId: number
    current?: number
    pageSize?: number
  }

  type getUserPostsByUserIdParams = {
    userId: number
    current?: number
    pageSize?: number
  }

  type getUserPostsParams = {
    current?: number
    pageSize?: number
  }

  type hasFavoritedParams = {
    postId: number
  }

  type hasLikedCommentParams = {
    commentId: number
  }

  type hasLikedParams = {
    postId: number
  }

  type LearningResourceAddRequest = {
    title?: string
    content?: string
    summary?: string
    resourceType?: number
    contentType?: number
    category?: string
    coverImage?: string
    isTop?: number
  }

  type LearningResourceEditRequest = {
    id?: number
    title?: string
    content?: string
    summary?: string
    resourceType?: number
    contentType?: number
    category?: string
    coverImage?: string
    isTop?: number
  }

  type LearningResourceQueryRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    title?: string
    summary?: string
    resourceType?: number
    contentType?: number
    category?: string
    authorId?: number
    isTop?: number
    keyword?: string
  }

  type LearningResourceVO = {
    id?: number
    title?: string
    content?: string
    summary?: string
    resourceType?: number
    resourceTypeName?: string
    contentType?: number
    contentTypeName?: string
    category?: string
    coverImage?: string
    authorId?: number
    author?: UserVO
    viewCount?: number
    isTop?: number
    createdAt?: string
    updatedAt?: string
  }

  type likeCommentParams = {
    commentId: number
  }

  type OptionDTO = {
    key?: string
    value?: string
    dimension?: string
  }

  type PageAssessmentResultVo = {
    records?: AssessmentResultVo[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageCareerVo = {
    records?: CareerVo[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageCommentVO = {
    records?: CommentVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageLearningResourceVO = {
    records?: LearningResourceVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PagePostVO = {
    records?: PostVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageResultCareerMapping = {
    records?: ResultCareerMapping[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageResumeTemplateVo = {
    records?: ResumeTemplateVo[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageResumeVo = {
    records?: ResumeVo[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageUser = {
    records?: User[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PostAddRequest = {
    title: string
    content: string
    tags?: string
  }

  type PostEditRequest = {
    id: number
    title: string
    content: string
    tags?: string
  }

  type PostQueryRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    title?: string
    content?: string
    tags?: string
    userId?: number
    isEssence?: number
    status?: number
  }

  type PostVO = {
    id?: number
    userId?: number
    user?: UserVO
    title?: string
    content?: string
    tagList?: string[]
    viewCount?: number
    likeCount?: number
    commentCount?: number
    isEssence?: number
    status?: number
    createdAt?: string
    updatedAt?: string
    lastCommentAt?: string
    hasLiked?: boolean
    hasFavorited?: boolean
  }

  type ProjectExperience = {
    projectName?: string
    role?: string
    startDate?: string
    endDate?: string
    projectUrl?: string
    technologies?: string[]
    description?: string
    achievements?: string
    sortOrder?: number
  }

  type publishResumeParams = {
    id: number
  }

  type QueryAssessmentResultRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    testType?: string
    resultCode?: string
    resultName?: string
    resultDesc?: string
  }

  type queryBestCompatibleCareerParams = {
    testType?: string
    resultCode?: string
  }

  type queryMappingByResultCodeParams = {
    testType?: string
    resultCode?: string
    pageNum?: number
    pageSize?: number
  }

  type queryQuestionParams = {
    testType: string
  }

  type queryResumeTemplateByIdParams = {
    id: number
  }

  type ResultCareerMapping = {
    id?: number
    resultId?: number
    testType?: string
    resultCode?: string
    careerId?: number
    careerName?: string
    description?: string
    compatibilityScore?: number
    createdAt?: string
    isDeleted?: number
  }

  type ResumeAddRequest = {
    title?: string
    templateId?: number
    content?: ResumeContent
    status?: number
    isPublic?: number
  }

  type ResumeContent = {
    basicInfo?: BasicInfo
    education?: Education[]
    workExperience?: WorkExperience[]
    projectExperience?: ProjectExperience[]
    skills?: Skill[]
  }

  type ResumeQueryRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    title?: string
    name?: string
    jobIntention?: string
    status?: number
    isPublic?: number
    templateId?: number
    userId?: number
  }

  type ResumeTemplateAddRequest = {
    templateName?: string
    templateDesc?: string
    previewUrl?: string
    templateConfig?: Record<string, any>
    defaultContent?: ResumeContent
    templateType?: number
    sortOrder?: number
    isActive?: number
  }

  type ResumeTemplateQueryRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    templateName?: string
    templateType?: number
    isActive?: number
  }

  type ResumeTemplateUpdateRequest = {
    id?: number
    templateName?: string
    templateDesc?: string
    previewUrl?: string
    templateConfig?: Record<string, any>
    defaultContent?: ResumeContent
    templateType?: number
    isFree?: number
    price?: number
    sortOrder?: number
    status?: number
  }

  type ResumeTemplateVo = {
    id?: number
    templateName?: string
    templateDesc?: string
    previewUrl?: string
    templateConfig?: Record<string, any>
    defaultContent?: ResumeContent
    templateType?: number
    sortOrder?: number
    isActive?: number
    createdAt?: string
    updatedAt?: string
  }

  type ResumeUpdateRequest = {
    id?: number
    title?: string
    templateId?: number
    content?: ResumeContent
    status?: number
    isPublic?: number
    versionRemark?: string
  }

  type ResumeVo = {
    id?: number
    userId?: number
    title?: string
    templateId?: number
    content?: ResumeContent
    name?: string
    jobIntention?: string
    status?: number
    shareCode?: string
    isPublic?: number
    createdAt?: string
    updatedAt?: string
  }

  type searchLearningResourcesParams = {
    keyword: string
    current?: number
    pageSize?: number
  }

  type Skill = {
    skillName?: string
    skillLevel?: number
    category?: string
    sortOrder?: number
  }

  type toggleEssenceParams = {
    postId: number
  }

  type toggleFavoriteParams = {
    postId: number
  }

  type toggleLikeParams = {
    postId: number
  }

  type toggleTopStatusParams = {
    resourceId: number
  }

  type UpdateAssessmentResultRequest = {
    id?: number
    testType?: string
    resultCode?: string
    resultName?: string
    resultDesc?: string
  }

  type UpdateQuestionRequest = {
    id?: number
    testType?: string
    content?: string
    dimension?: string
    options?: string
    sortOrder?: number
    status?: number
  }

  type UpdateResultCareerRequest = {
    id?: number
    assessmentResultId?: number
    careerId?: number
    compatibilityScore?: number
  }

  type updateTemplateStatusParams = {
    id: number
    status: number
  }

  type User = {
    id?: number
    userAccount?: string
    userPassword?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    editTime?: string
    createAt?: string
    updateAt?: string
    isDeleted?: number
  }

  type UserAddRequest = {
    userAccount?: string
    userPassword?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserAssessmentVo = {
    id?: number
    testType?: string
    resultCode?: string
    resultName?: string
    resultDesc?: string
    resultIcon?: string
    choices?: string
    dimensionScores?: string
    userId?: number
    careerId?: number
    careerName?: string
    careerDescription?: string
    createdAt?: string
    isDeleted?: number
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserLoginVO = {
    id?: number
    token?: string
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserQueryRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    id?: number
    userAccount?: string
    userName?: string
    userRole?: string
  }

  type UserRegisterRequest = {
    userAccount?: string
    userPassword?: string
    checkPassword?: string
  }

  type UserUpdateRequest = {
    id?: number
    userAccount?: string
    userPassword?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserVO = {
    id?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type WorkExperience = {
    companyName?: string
    position?: string
    startDate?: string
    endDate?: string
    isCurrent?: boolean
    jobDescription?: string
    achievements?: string
    sortOrder?: number
  }
}
