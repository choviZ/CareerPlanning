package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.constance.UserRoleConstance;
import com.zcw.cpbackend.model.dto.career.CareerAddRequest;
import com.zcw.cpbackend.model.dto.career.CareerQueryRequest;
import com.zcw.cpbackend.model.dto.career.CareerUpdateRequest;
import com.zcw.cpbackend.model.vo.CareerVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import com.zcw.cpbackend.service.CareerService;

/**
 * 职业表 控制层。
 *
 * @author zcw
 */
@RestController
@RequestMapping("/career")
public class CareerController {

    @Resource
    private CareerService careerService;

    /**
     * 添加职业
     *
     * @param careerAddRequest 职业信息
     * @return 是否成功
     */
    @PostMapping("/add")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> addCareer(@RequestBody CareerAddRequest careerAddRequest) {
        boolean result = careerService.addCareer(careerAddRequest);
        return ResultUtils.success(result);
    }

    /**
     * 删除职业
     *
     * @param id 职业id
     * @return 是否成功
     */
    @GetMapping("/delete/{id}")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> deleteCareer(@PathVariable Long id) {
        boolean result = careerService.deleteCareer(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新职业
     *
     * @param careerUpdateRequest 职业信息
     * @return 是否成功
     */
    @PostMapping("/update")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> updateCareer(@RequestBody CareerUpdateRequest careerUpdateRequest) {
        boolean result = careerService.updateCareer(careerUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询职业
     *
     * @param careerQueryRequest 查询条件
     * @return 职业分页
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<CareerVo>> listCareerByPage(@RequestBody CareerQueryRequest careerQueryRequest) {
        Page<CareerVo> careerPage = careerService.queryCareer(careerQueryRequest);
        return ResultUtils.success(careerPage);
    }

    /**
     * 根据id查询职业
     *
     * @param id 职业id
     * @return 职业信息
     */
    @GetMapping("/get/{id}")
    public BaseResponse<CareerVo> getCareerById(@PathVariable Long id) {
        CareerVo career = careerService.queryCareerById(id);
        return ResultUtils.success(career);
    }
}
