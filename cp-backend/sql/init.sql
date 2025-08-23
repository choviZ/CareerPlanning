INSERT INTO career (name, description, required_skills, job_outlook, average_salary)
VALUES ('软件工程师', '负责设计、开发和维护软件系统，解决技术问题并优化性能。',
        '编程语言（Java/Python/C++）、数据结构、算法、软件开发流程',
        '数字化转型推动需求持续增长，尤其是人工智能和云计算领域。', '15000-35000元/月'),
       ('数据科学家', '通过统计分析、机器学习和数据可视化提取业务洞察，支持决策制定。',
        'Python/R、SQL、机器学习、统计学、数据可视化工具', '大数据时代需求旺盛，各行业均需数据驱动决策能力。',
        '20000-45000元/月'),
       ('电气工程师', '设计、开发和测试电气设备与系统，如电力网络、电子电路和控制系统。',
        '电路设计、CAD工具、电力系统分析、项目管理', '新能源和智能电网发展带来新机会，稳定性较高。', '12000-30000元/月'),
       ('市场营销经理', '制定营销策略，管理品牌推广、市场调研和数字营销活动。',
        '市场分析、SEO/SEM、社交媒体运营、消费者行为研究', '数字化营销趋势扩大需求，需适应快速变化的媒体环境。',
        '10000-25000元/月'),
       ('临床医生', '诊断和治疗疾病，提供医疗咨询并参与公共卫生工作。', '医学知识、临床技能、沟通能力、应急处理',
        '人口老龄化加剧医疗需求，就业稳定性极高。', '18000-40000元/月'),
       ('产品经理', '定义产品愿景、协调开发团队并管理产品生命周期。', '需求分析、原型设计、项目管理、跨部门协作',
        '互联网行业竞争推动需求，需复合型能力。', '20000-40000元/月'),
       ('金融分析师', '评估投资机会、分析财务数据并提供风险评估报告。', '财务建模、Excel、数据分析、行业研究',
        '金融市场复杂化增加专业分析需求，竞争激烈。', '15000-35000元/月'),
       ('机械工程师', '设计机械系统、制造工艺和设备，进行测试与优化。', 'CAD/CAM、力学分析、材料科学、生产流程',
        '制造业升级与自动化推动需求，需持续学习新技术。', '10000-28000元/月'),
       ('用户体验设计师', '研究用户需求，设计产品交互界面和视觉元素以提升满意度。',
        '用户研究、原型设计、UI/UX工具、可用性测试', '产品体验成为核心竞争力，互联网行业需求强劲。', '12000-30000元/月'),
       ('土木工程师', '规划、设计和监督基础设施项目（如桥梁、道路和建筑）。', '结构分析、CAD、工程管理、法规标准',
        '城市化与基建投资保障需求，但受经济周期影响。', '11000-30000元/月');

INSERT INTO assessment_question (test_type, content, dimension, options, sort_order)
VALUES ('MBTI', '在社交场合中，你通常：', 'E/I', '[
  {"key": "A", "value": "主动与他人交谈，容易结识新人", "dimension": "E"},
  {"key": "B", "value": "较少主动开口，偏好与熟人交流", "dimension": "I"},
  {"key": "C", "value": "活跃气氛，积极参与群体互动", "dimension": "E"},
  {"key": "D", "value": "观察他人，等待别人先打招呼", "dimension": "I"}
]', 1),
       ('MBTI', '你更倾向于如何理解世界？', 'S/N', '[
  {"key": "A", "value": "关注具体事实和实际细节", "dimension": "S"},
  {"key": "B", "value": "关注模式和创新可能性", "dimension": "N"},
  {"key": "C", "value": "依赖经验和实际数据", "dimension": "S"},
  {"key": "D", "value": "依赖直觉和理论推断", "dimension": "N"}
]', 2),
       ('MBTI', '做决定时，你更重视：', 'T/F', '[
  {"key": "A", "value": "逻辑分析和客观标准", "dimension": "T"},
  {"key": "B", "value": "价值观和人际关系影响", "dimension": "F"},
  {"key": "C", "value": "效率和结果最大化", "dimension": "T"},
  {"key": "D", "value": "和谐与情感共鸣", "dimension": "F"}
]', 3),
       ('MBTI', '你更喜欢的生活方式是：', 'J/P', '[
  {"key": "A", "value": "有计划、有组织的方式", "dimension": "J"},
  {"key": "B", "value": "灵活、随性调整的方式", "dimension": "P"},
  {"key": "C", "value": "提前制定详细计划", "dimension": "J"},
  {"key": "D", "value": "根据情况临时决策", "dimension": "P"}
]', 4),
       ('MBTI', '当你学习新技能时，更倾向于：', 'S/N', '[
  {"key": "A", "value": "按步骤反复练习直到熟练", "dimension": "S"},
  {"key": "B", "value": "探索其底层原理和可能性", "dimension": "N"},
  {"key": "C", "value": "关注实际应用场景", "dimension": "S"},
  {"key": "D", "value": "联想与其他知识的关联", "dimension": "N"}
]', 5),
       ('MBTI', '面对问题时，你通常：', 'T/F', '[
  {"key": "A", "value": "分析问题原因和逻辑关系", "dimension": "T"},
  {"key": "B", "value": "考虑问题对人们的影响", "dimension": "F"},
  {"key": "C", "value": "寻找最有效的解决方案", "dimension": "T"},
  {"key": "D", "value": "寻求共识和妥协方案", "dimension": "F"}
]', 6),
       ('MBTI', '你更喜欢的工作环境是：', 'J/P', '[
  {"key": "A", "value": "结构清晰、任务明确", "dimension": "J"},
  {"key": "B", "value": "灵活多变、充满挑战", "dimension": "P"},
  {"key": "C", "value": "按部就班、可预测", "dimension": "J"},
  {"key": "D", "value": "自主决策、适应变化", "dimension": "P"}
]', 7),
       ('MBTI', '在聚会中，你通常：', 'E/I', '[
  {"key": "A", "value": "是谈话的中心人物", "dimension": "E"},
  {"key": "B", "value": "与一两个人深入交流", "dimension": "I"},
  {"key": "C", "value": "主动介绍陌生人认识", "dimension": "E"},
  {"key": "D", "value": "待在角落观察人群", "dimension": "I"}
]', 8),
       ('MBTI', '你更欣赏哪种能力？', 'S/N', '[
  {"key": "A", "value": "注重细节和实际执行", "dimension": "S"},
  {"key": "B", "value": "看到大局和未来趋势", "dimension": "N"},
  {"key": "C", "value": "熟练掌握具体技能", "dimension": "S"},
  {"key": "D", "value": "提出创新想法和概念", "dimension": "N"}
]', 9),
       ('MBTI', '评价他人时，你更注重：', 'T/F', '[
  {"key": "A", "value": "他们的能力和效率", "dimension": "T"},
  {"key": "B", "value": "他们的善良和体贴", "dimension": "F"},
  {"key": "C", "value": "他们的逻辑和理性", "dimension": "T"},
  {"key": "D", "value": "他们的热情和同情心", "dimension": "F"}
]', 10),
       ('MBTI', '你如何处理任务截止日期？', 'J/P', '[
  {"key": "A", "value": "提前完成，留出缓冲时间", "dimension": "J"},
  {"key": "B", "value": "最后一刻完成，压力下效率高", "dimension": "P"},
  {"key": "C", "value": "制定时间表并严格执行", "dimension": "J"},
  {"key": "D", "value": "灵活调整，随进度变化", "dimension": "P"}
]', 11),
       ('MBTI', '当你感到疲惫时，更喜欢：', 'E/I', '[
  {"key": "A", "value": "与朋友相聚恢复能量", "dimension": "E"},
  {"key": "B", "value": "独处休息恢复能量", "dimension": "I"},
  {"key": "C", "value": "参加活动转移注意力", "dimension": "E"},
  {"key": "D", "value": "安静阅读或冥想", "dimension": "I"}
]', 12),
       ('MBTI', '你更容易注意到：', 'S/N', '[
  {"key": "A", "value": "具体的事物和细节", "dimension": "S"},
  {"key": "B", "value": "事物的象征和含义", "dimension": "N"},
  {"key": "C", "value": "实际存在的东西", "dimension": "S"},
  {"key": "D", "value": "可能性和潜在变化", "dimension": "N"}
]', 13),
       ('MBTI', '在团队中，你更可能：', 'T/F', '[
  {"key": "A", "value": "指出逻辑漏洞和改进方案", "dimension": "T"},
  {"key": "B", "value": "维护团队和谐和凝聚力", "dimension": "F"},
  {"key": "C", "value": "专注于任务效率和结果", "dimension": "T"},
  {"key": "D", "value": "关注成员感受和需求", "dimension": "F"}
]', 14),
       ('MBTI', '你更喜欢如何安排假期？', 'J/P', '[
  {"key": "A", "value": "提前规划详细行程", "dimension": "J"},
  {"key": "B", "value": "随心所欲，临时决定", "dimension": "P"},
  {"key": "C", "value": "预订好所有住宿交通", "dimension": "J"},
  {"key": "D", "value": "保留灵活性，随时调整", "dimension": "P"}
]', 15),
       ('MBTI', '在新环境中，你通常：', 'E/I', '[
  {"key": "A", "value": "快速适应并主动探索", "dimension": "E"},
  {"key": "B", "value": "需要时间观察和适应", "dimension": "I"},
  {"key": "C", "value": "立即开始社交互动", "dimension": "E"},
  {"key": "D", "value": "先了解规则和结构", "dimension": "I"}
]', 16),
       ('MBTI', '你更相信：', 'S/N', '[
  {"key": "A", "value": "经验和实践验证", "dimension": "S"},
  {"key": "B", "value": "直觉和灵感启发", "dimension": "N"},
  {"key": "C", "value": "具体数据和事实", "dimension": "S"},
  {"key": "D", "value": "理论和概念框架", "dimension": "N"}
]', 17),
       ('MBTI', '当与人意见不同时，你更可能：', 'T/F', '[
  {"key": "A", "value": "坚持自己的逻辑观点", "dimension": "T"},
  {"key": "B", "value": "避免冲突，寻求和谐", "dimension": "F"},
  {"key": "C", "value": "用事实和数据说服对方", "dimension": "T"},
  {"key": "D", "value": "理解对方感受和立场", "dimension": "F"}
]', 18),
       ('MBTI', '你如何处理多任务？', 'J/P', '[
  {"key": "A", "value": "优先排序，逐一完成", "dimension": "J"},
  {"key": "B", "value": "同时进行，灵活切换", "dimension": "P"},
  {"key": "C", "value": "制定计划，按部就班", "dimension": "J"},
  {"key": "D", "value": "随机应变，根据兴趣", "dimension": "P"}
]', 19),
       ('MBTI', '你如何描述自己的社交能量？', 'E/I', '[
  {"key": "A", "value": "与人相处让我充满能量", "dimension": "E"},
  {"key": "B", "value": "独处让我恢复能量", "dimension": "I"},
  {"key": "C", "value": "喜欢大型社交活动", "dimension": "E"},
  {"key": "D", "value": "偏好小型亲密聚会", "dimension": "I"}
]', 20),
       ('MBTI', '你更擅长：', 'S/N', '[
  {"key": "A", "value": "处理具体实际的问题", "dimension": "S"},
  {"key": "B", "value": "构想创新解决方案", "dimension": "N"},
  {"key": "C", "value": "注意细节和实际执行", "dimension": "S"},
  {"key": "D", "value": "看到模式和联系", "dimension": "N"}
]', 21),
       ('MBTI', '你更欣赏哪种品质？', 'T/F', '[
  {"key": "A", "value": "理性客观", "dimension": "T"},
  {"key": "B", "value": " empathy", "dimension": "F"},
  {"key": "C", "value": "高效直接", "dimension": "T"},
  {"key": "D", "value": "温暖关怀", "dimension": "F"}
]', 22),
       ('MBTI', '你更喜欢的工作方式是：', 'J/P', '[
  {"key": "A", "value": "有明确目标和期限", "dimension": "J"},
  {"key": "B", "value": "自由探索不同可能性", "dimension": "P"},
  {"key": "C", "value": "按计划逐步推进", "dimension": "J"},
  {"key": "D", "value": "根据灵感随时调整", "dimension": "P"}
]', 23),
       ('MBTI', '在会议上，你通常：', 'E/I', '[
  {"key": "A", "value": "积极发言，表达观点", "dimension": "E"},
  {"key": "B", "value": "倾听他人，偶尔发言", "dimension": "I"},
  {"key": "C", "value": "享受讨论和辩论", "dimension": "E"},
  {"key": "D", "value": "偏好书面沟通", "dimension": "I"}
]', 24),
       ('MBTI', '你更关注：', 'S/N', '[
  {"key": "A", "value": "当前实际的情况", "dimension": "S"},
  {"key": "B", "value": "未来的可能性", "dimension": "N"},
  {"key": "C", "value": "具体的数据和事实", "dimension": "S"},
  {"key": "D", "value": "潜在的意义和象征", "dimension": "N"}
]', 25),
       ('MBTI', '当朋友遇到问题时，你更可能：', 'T/F', '[
  {"key": "A", "value": "提供解决方案和建议", "dimension": "T"},
  {"key": "B", "value": "提供情感支持和理解", "dimension": "F"},
  {"key": "C", "value": "分析问题根源", "dimension": "T"},
  {"key": "D", "value": "感同身受地倾听", "dimension": "F"}
]', 26),
       ('MBTI', '你如何做决定？', 'J/P', '[
  {"key": "A", "value": "快速决定，坚持选择", "dimension": "J"},
  {"key": "B", "value": "保持开放，收集更多信息", "dimension": "P"},
  {"key": "C", "value": "基于明确的标准", "dimension": "J"},
  {"key": "D", "value": "根据情况灵活调整", "dimension": "P"}
]', 27),
       ('MBTI', '你更喜欢如何度过周末？', 'E/I', '[
  {"key": "A", "value": "与朋友聚会或参加活动", "dimension": "E"},
  {"key": "B", "value": "独自或与家人安静地休息", "dimension": "I"},
  {"key": "C", "value": "尝试新的社交体验", "dimension": "E"},
  {"key": "D", "value": "在家看电影或读书", "dimension": "I"}
]', 28),
       ('MBTI', '你更倾向于相信：', 'S/N', '[
  {"key": "A", "value": "亲眼所见的事实", "dimension": "S"},
  {"key": "B", "value": "直觉和预感", "dimension": "N"},
  {"key": "C", "value": "实际经验和实践", "dimension": "S"},
  {"key": "D", "value": "创新理论和概念", "dimension": "N"}
]', 29),
       ('MBTI', '你更重视：', 'T/F', '[
  {"key": "A", "value": "真理和公正", "dimension": "T"},
  {"key": "B", "value": "同情和善良", "dimension": "F"},
  {"key": "C", "value": "效率和效果", "dimension": "T"},
  {"key": "D", "value": "和谐和包容", "dimension": "F"}
]', 30);


INSERT INTO assessment_result (test_type, result_code, result_name, result_desc)
VALUES ('MBTI', 'ISTJ', '物流师', '务实、注重事实、有责任感，喜欢有序和 organized 的环境。他们可靠且勤奋，遵守规则和传统。'),
       ('MBTI', 'ISFJ', '守护者', '温暖、有同情心、负责任，致力于保护和服务他人。他们注重细节，有很强的记忆力和责任感。'),
       ('MBTI', 'INFJ', '提倡者', '富有洞察力、有原则、有远见，致力于帮助他人实现潜能。他们深沉而复杂，能够理解深层含义。'),
       ('MBTI', 'INTJ', '建筑师', '战略思维、独立、有远见，擅长制定长期计划和系统思考。他们追求知识和能力，注重效率。'),
       ('MBTI', 'ISTP', '鉴赏家', '灵活务实、善于分析、喜欢动手解决问题。他们冷静观察，擅长在危机中保持镇定。'),
       ('MBTI', 'ISFP', '探险家', '温和友善、审美敏锐、喜欢体验当下。他们灵活适应环境，重视个人价值观和自由。'),
       ('MBTI', 'INFP', '调解员', '理想主义、有同情心、好奇，致力于追求真实和意义。他们开放包容，重视个人成长。'),
       ('MBTI', 'INTP', '逻辑学家', '创新思维、好奇、理性，喜欢理论分析和概念思考。他们追求知识和理解，擅长发现模式。'),
       ('MBTI', 'ESTP', '企业家', '精力充沛、务实、喜欢冒险和即时行动。他们擅长应对危机，享受物质享受和社交互动。'),
       ('MBTI', 'ESFP', '表演者', '热情友好、活泼、喜欢娱乐和帮助他人。他们享受当下，擅长创造欢乐氛围。'),
       ('MBTI', 'ENFP', '竞选者', '热情洋溢、创造性、乐观，擅长激励他人和看到可能性。他们充满好奇心，喜欢新体验。'),
       ('MBTI', 'ENTP', '辩论家', '聪明好奇、思维敏捷、喜欢智力挑战和辩论。他们擅长看到多种可能性，厌恶常规。'),
       ('MBTI', 'ESTJ', '总经理', '务实果断、有组织能力、注重效率，喜欢建立秩序和结构。他们重视传统和责任。'),
       ('MBTI', 'ESFJ', '执政官', '友善尽责、合作、注重和谐，喜欢帮助他人和维持传统。他们擅长创建温馨的社交环境。'),
       ('MBTI', 'ENFJ', '主人公', '富有魅力、有同情心、有影响力，擅长激励他人实现潜能。他们注重人际关系和集体和谐。'),
       ('MBTI', 'ENTJ', '指挥官', '果断战略、有领导力、注重效率，擅长组织资源和实现目标。他们重视能力和竞争力。');

INSERT INTO assessment_question (test_type, content, dimension, options, sort_order)
VALUES ('MBTI', '在社交场合中，你通常：', 'E/I', '[
  {"key": "A", "value": "主动与他人交谈，容易结识新人", "dimension": "E"},
  {"key": "B", "value": "较少主动开口，偏好与熟人交流", "dimension": "I"},
  {"key": "C", "value": "活跃气氛，积极参与群体互动", "dimension": "E"},
  {"key": "D", "value": "观察他人，等待别人先打招呼", "dimension": "I"}
]', 1),
       ('MBTI', '你更倾向于如何理解世界？', 'S/N', '[
  {"key": "A", "value": "关注具体事实和实际细节", "dimension": "S"},
  {"key": "B", "value": "关注模式和创新可能性", "dimension": "N"},
  {"key": "C", "value": "依赖经验和实际数据", "dimension": "S"},
  {"key": "D", "value": "依赖直觉和理论推断", "dimension": "N"}
]', 2),
       ('MBTI', '做决定时，你更重视：', 'T/F', '[
  {"key": "A", "value": "逻辑分析和客观标准", "dimension": "T"},
  {"key": "B", "value": "价值观和人际关系影响", "dimension": "F"},
  {"key": "C", "value": "效率和结果最大化", "dimension": "T"},
  {"key": "D", "value": "和谐与情感共鸣", "dimension": "F"}
]', 3),
       ('MBTI', '你更喜欢的生活方式是：', 'J/P', '[
  {"key": "A", "value": "有计划、有组织的方式", "dimension": "J"},
  {"key": "B", "value": "灵活、随性调整的方式", "dimension": "P"},
  {"key": "C", "value": "提前制定详细计划", "dimension": "J"},
  {"key": "D", "value": "根据情况临时决策", "dimension": "P"}
]', 4),
       ('MBTI', '当你学习新技能时，更倾向于：', 'S/N', '[
  {"key": "A", "value": "按步骤反复练习直到熟练", "dimension": "S"},
  {"key": "B", "value": "探索其底层原理和可能性", "dimension": "N"},
  {"key": "C", "value": "关注实际应用场景", "dimension": "S"},
  {"key": "D", "value": "联想与其他知识的关联", "dimension": "N"}
]', 5),
       ('MBTI', '面对问题时，你通常：', 'T/F', '[
  {"key": "A", "value": "分析问题原因和逻辑关系", "dimension": "T"},
  {"key": "B", "value": "考虑问题对人们的影响", "dimension": "F"},
  {"key": "C", "value": "寻找最有效的解决方案", "dimension": "T"},
  {"key": "D", "value": "寻求共识和妥协方案", "dimension": "F"}
]', 6),
       ('MBTI', '你更喜欢的工作环境是：', 'J/P', '[
  {"key": "A", "value": "结构清晰、任务明确", "dimension": "J"},
  {"key": "B", "value": "灵活多变、充满挑战", "dimension": "P"},
  {"key": "C", "value": "按部就班、可预测", "dimension": "J"},
  {"key": "D", "value": "自主决策、适应变化", "dimension": "P"}
]', 7),
       ('MBTI', '在聚会中，你通常：', 'E/I', '[
  {"key": "A", "value": "是谈话的中心人物", "dimension": "E"},
  {"key": "B", "value": "与一两个人深入交流", "dimension": "I"},
  {"key": "C", "value": "主动介绍陌生人认识", "dimension": "E"},
  {"key": "D", "value": "待在角落观察人群", "dimension": "I"}
]', 8),
       ('MBTI', '你更欣赏哪种能力？', 'S/N', '[
  {"key": "A", "value": "注重细节和实际执行", "dimension": "S"},
  {"key": "B", "value": "看到大局和未来趋势", "dimension": "N"},
  {"key": "C", "value": "熟练掌握具体技能", "dimension": "S"},
  {"key": "D", "value": "提出创新想法和概念", "dimension": "N"}
]', 9),
       ('MBTI', '评价他人时，你更注重：', 'T/F', '[
  {"key": "A", "value": "他们的能力和效率", "dimension": "T"},
  {"key": "B", "value": "他们的善良和体贴", "dimension": "F"},
  {"key": "C", "value": "他们的逻辑和理性", "dimension": "T"},
  {"key": "D", "value": "他们的热情和同情心", "dimension": "F"}
]', 10),
       ('MBTI', '你如何处理任务截止日期？', 'J/P', '[
  {"key": "A", "value": "提前完成，留出缓冲时间", "dimension": "J"},
  {"key": "B", "value": "最后一刻完成，压力下效率高", "dimension": "P"},
  {"key": "C", "value": "制定时间表并严格执行", "dimension": "J"},
  {"key": "D", "value": "灵活调整，随进度变化", "dimension": "P"}
]', 11),
       ('MBTI', '当你感到疲惫时，更喜欢：', 'E/I', '[
  {"key": "A", "value": "与朋友相聚恢复能量", "dimension": "E"},
  {"key": "B", "value": "独处休息恢复能量", "dimension": "I"},
  {"key": "C", "value": "参加活动转移注意力", "dimension": "E"},
  {"key": "D", "value": "安静阅读或冥想", "dimension": "I"}
]', 12),
       ('MBTI', '你更容易注意到：', 'S/N', '[
  {"key": "A", "value": "具体的事物和细节", "dimension": "S"},
  {"key": "B", "value": "事物的象征和含义", "dimension": "N"},
  {"key": "C", "value": "实际存在的东西", "dimension": "S"},
  {"key": "D", "value": "可能性和潜在变化", "dimension": "N"}
]', 13),
       ('MBTI', '在团队中，你更可能：', 'T/F', '[
  {"key": "A", "value": "指出逻辑漏洞和改进方案", "dimension": "T"},
  {"key": "B", "value": "维护团队和谐和凝聚力", "dimension": "F"},
  {"key": "C", "value": "专注于任务效率和结果", "dimension": "T"},
  {"key": "D", "value": "关注成员感受和需求", "dimension": "F"}
]', 14),
       ('MBTI', '你更喜欢如何安排假期？', 'J/P', '[
  {"key": "A", "value": "提前规划详细行程", "dimension": "J"},
  {"key": "B", "value": "随心所欲，临时决定", "dimension": "P"},
  {"key": "C", "value": "预订好所有住宿交通", "dimension": "J"},
  {"key": "D", "value": "保留灵活性，随时调整", "dimension": "P"}
]', 15),
       ('MBTI', '在新环境中，你通常：', 'E/I', '[
  {"key": "A", "value": "快速适应并主动探索", "dimension": "E"},
  {"key": "B", "value": "需要时间观察和适应", "dimension": "I"},
  {"key": "C", "value": "立即开始社交互动", "dimension": "E"},
  {"key": "D", "value": "先了解规则和结构", "dimension": "I"}
]', 16),
       ('MBTI', '你更相信：', 'S/N', '[
  {"key": "A", "value": "经验和实践验证", "dimension": "S"},
  {"key": "B", "value": "直觉和灵感启发", "dimension": "N"},
  {"key": "C", "value": "具体数据和事实", "dimension": "S"},
  {"key": "D", "value": "理论和概念框架", "dimension": "N"}
]', 17),
       ('MBTI', '当与人意见不同时，你更可能：', 'T/F', '[
  {"key": "A", "value": "坚持自己的逻辑观点", "dimension": "T"},
  {"key": "B", "value": "避免冲突，寻求和谐", "dimension": "F"},
  {"key": "C", "value": "用事实和数据说服对方", "dimension": "T"},
  {"key": "D", "value": "理解对方感受和立场", "dimension": "F"}
]', 18),
       ('MBTI', '你如何处理多任务？', 'J/P', '[
  {"key": "A", "value": "优先排序，逐一完成", "dimension": "J"},
  {"key": "B", "value": "同时进行，灵活切换", "dimension": "P"},
  {"key": "C", "value": "制定计划，按部就班", "dimension": "J"},
  {"key": "D", "value": "随机应变，根据兴趣", "dimension": "P"}
]', 19),
       ('MBTI', '你如何描述自己的社交能量？', 'E/I', '[
  {"key": "A", "value": "与人相处让我充满能量", "dimension": "E"},
  {"key": "B", "value": "独处让我恢复能量", "dimension": "I"},
  {"key": "C", "value": "喜欢大型社交活动", "dimension": "E"},
  {"key": "D", "value": "偏好小型亲密聚会", "dimension": "I"}
]', 20),
       ('MBTI', '你更擅长：', 'S/N', '[
  {"key": "A", "value": "处理具体实际的问题", "dimension": "S"},
  {"key": "B", "value": "构想创新解决方案", "dimension": "N"},
  {"key": "C", "value": "注意细节和实际执行", "dimension": "S"},
  {"key": "D", "value": "看到模式和联系", "dimension": "N"}
]', 21),
       ('MBTI', '你更欣赏哪种品质？', 'T/F', '[
  {"key": "A", "value": "理性客观", "dimension": "T"},
  {"key": "B", "value": " empathy", "dimension": "F"},
  {"key": "C", "value": "高效直接", "dimension": "T"},
  {"key": "D", "value": "温暖关怀", "dimension": "F"}
]', 22),
       ('MBTI', '你更喜欢的工作方式是：', 'J/P', '[
  {"key": "A", "value": "有明确目标和期限", "dimension": "J"},
  {"key": "B", "value": "自由探索不同可能性", "dimension": "P"},
  {"key": "C", "value": "按计划逐步推进", "dimension": "J"},
  {"key": "D", "value": "根据灵感随时调整", "dimension": "P"}
]', 23),
       ('MBTI', '在会议上，你通常：', 'E/I', '[
  {"key": "A", "value": "积极发言，表达观点", "dimension": "E"},
  {"key": "B", "value": "倾听他人，偶尔发言", "dimension": "I"},
  {"key": "C", "value": "享受讨论和辩论", "dimension": "E"},
  {"key": "D", "value": "偏好书面沟通", "dimension": "I"}
]', 24),
       ('MBTI', '你更关注：', 'S/N', '[
  {"key": "A", "value": "当前实际的情况", "dimension": "S"},
  {"key": "B", "value": "未来的可能性", "dimension": "N"},
  {"key": "C", "value": "具体的数据和事实", "dimension": "S"},
  {"key": "D", "value": "潜在的意义和象征", "dimension": "N"}
]', 25),
       ('MBTI', '当朋友遇到问题时，你更可能：', 'T/F', '[
  {"key": "A", "value": "提供解决方案和建议", "dimension": "T"},
  {"key": "B", "value": "提供情感支持和理解", "dimension": "F"},
  {"key": "C", "value": "分析问题根源", "dimension": "T"},
  {"key": "D", "value": "感同身受地倾听", "dimension": "F"}
]', 26),
       ('MBTI', '你如何做决定？', 'J/P', '[
  {"key": "A", "value": "快速决定，坚持选择", "dimension": "J"},
  {"key": "B", "value": "保持开放，收集更多信息", "dimension": "P"},
  {"key": "C", "value": "基于明确的标准", "dimension": "J"},
  {"key": "D", "value": "根据情况灵活调整", "dimension": "P"}
]', 27),
       ('MBTI', '你更喜欢如何度过周末？', 'E/I', '[
  {"key": "A", "value": "与朋友聚会或参加活动", "dimension": "E"},
  {"key": "B", "value": "独自或与家人安静地休息", "dimension": "I"},
  {"key": "C", "value": "尝试新的社交体验", "dimension": "E"},
  {"key": "D", "value": "在家看电影或读书", "dimension": "I"}
]', 28),
       ('MBTI', '你更倾向于相信：', 'S/N', '[
  {"key": "A", "value": "亲眼所见的事实", "dimension": "S"},
  {"key": "B", "value": "直觉和预感", "dimension": "N"},
  {"key": "C", "value": "实际经验和实践", "dimension": "S"},
  {"key": "D", "value": "创新理论和概念", "dimension": "N"}
]', 29),
       ('MBTI', '你更重视：', 'T/F', '[
  {"key": "A", "value": "真理和公正", "dimension": "T"},
  {"key": "B", "value": "同情和善良", "dimension": "F"},
  {"key": "C", "value": "效率和效果", "dimension": "T"},
  {"key": "D", "value": "和谐和包容", "dimension": "F"}
]', 30);