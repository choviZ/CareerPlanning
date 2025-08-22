declare namespace API {
  type AddQuestionRequest = {
    id?: number
    testType?: string
    content?: string
    dimension?: string
    options?: string
    sortOrder?: number
    status?: number
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
    userId?: number
    testType?: string
    resultCode?: string
    dimensionScores?: string
    createdAt?: string
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

  type BaseResponseCareer = {
    code?: number
    data?: Career
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

  type BaseResponsePageCareer = {
    code?: number
    data?: PageCareer
    message?: string
  }

  type BaseResponsePageUser = {
    code?: number
    data?: PageUser
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

  type Career = {
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

  type deleteCareerParams = {
    id: number
  }

  type deleteQuestionParams = {
    id: number
  }

  type deleteUserParams = {
    id: number
  }

  type DoAssessmentRequest = {
    testType?: string
    userAnswers?: string[]
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

  type PageCareer = {
    records?: Career[]
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

  type queryQuestionParams = {
    testType: string
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
