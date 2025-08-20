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

  type BaseResponseListAssessmentQuestionVo = {
    code?: number
    data?: AssessmentQuestionVo[]
    message?: string
  }

  type deleteQuestionParams = {
    id: number
  }

  type DoAssessmentRequest = {
    testType?: string
    userAnswers?: string[]
  }

  type OptionDTO = {
    key?: string
    value?: string
    dimension?: string
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
}
