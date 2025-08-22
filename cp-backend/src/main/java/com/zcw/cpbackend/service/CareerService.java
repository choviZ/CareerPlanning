package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.career.CareerAddRequest;
import com.zcw.cpbackend.model.dto.career.CareerQueryRequest;
import com.zcw.cpbackend.model.dto.career.CareerUpdateRequest;
import com.zcw.cpbackend.model.entity.Career;

/**
 * 职业表 服务层。
 *
 * @author zcw
 */
public interface CareerService extends IService<Career> {

    /**
     * 添加职业
     *
     * @param careerAddRequest 职业信息
     * @return 是否成功
     */
    boolean addCareer(CareerAddRequest careerAddRequest);

    /**
     * 删除职业
     *
     * @param id 职业id
     * @return 是否成功
     */
    boolean deleteCareer(Long id);

    /**
     * 更新职业
     *
     * @param careerUpdateRequest 职业信息
     * @return 是否成功
     */
    boolean updateCareer(CareerUpdateRequest careerUpdateRequest);

    /**
     * 分页查询职业
     *
     * @param careerQueryRequest 查询条件
     * @return 职业分页
     */
    Page<Career> queryCareer(CareerQueryRequest careerQueryRequest);

    /**
     * 根据id查询职业
     *
     * @param id 职业id
     * @return 职业信息
     */
    Career queryCareerById(Long id);
}
