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

  type BaseResponseListAssessmentQuestionVo = {
    code?: number
    data?: AssessmentQuestionVo[]
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

  type BaseResponsePageResultCareerMapping = {
    code?: number
    data?: PageResultCareerMapping
    message?: string
  }

  type BaseResponsePageUser = {
    code?: number
    data?: PageUser
    message?: string
  }

  type BaseResponseResultCareerMapping = {
    code?: number
    data?: ResultCareerMapping
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

  type deleteAssessmentResultParams = {
    id: number
  }

  type deleteCareerParams = {
    id: number
  }

  type deleteQuestionParams = {
    id: number
  }

  type deleteResultCareerMappingParams = {
    id: number
  }

  type deleteUserParams = {
    id: number
  }

  type DoAssessmentRequest = {
    testType?: string
    userAnswers?: string[]
  }

  type getAssessmentResultByIdParams = {
    id: number
  }

  type getCareerByIdParams = {
    id: number
  }

  type getUserByIdParams = {
    id: number
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

  type PageResultCareerMapping = {
    records?: ResultCareerMapping[]
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
}
