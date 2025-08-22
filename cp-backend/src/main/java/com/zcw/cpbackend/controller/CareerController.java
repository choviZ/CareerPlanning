package com.zcw.cpbackend.controller;

import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.model.dto.career.CareerAddRequest;
import com.zcw.cpbackend.model.dto.career.CareerQueryRequest;
import com.zcw.cpbackend.model.dto.career.CareerUpdateRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.zcw.cpbackend.model.entity.Career;
import com.zcw.cpbackend.service.CareerService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 职业表 控制层。
 *
 * @author zcw
 */
@RestController
@RequestMapping("/career")
public class CareerController {

    @Autowired
    private CareerService careerService;

    /**
     * 添加职业
     *
     * @param careerAddRequest 职业信息
     * @return 是否成功
     */
    @PostMapping("/add")
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
    public BaseResponse<Page<Career>> listCareerByPage(@RequestBody CareerQueryRequest careerQueryRequest) {
        Page<Career> careerPage = careerService.queryCareer(careerQueryRequest);
        return ResultUtils.success(careerPage);
    }

    /**
     * 根据id查询职业
     *
     * @param id 职业id
     * @return 职业信息
     */
    @GetMapping("/get/{id}")
    public BaseResponse<Career> getCareerById(@PathVariable Long id) {
        Career career = careerService.queryCareerById(id);
        return ResultUtils.success(career);
    }
}
