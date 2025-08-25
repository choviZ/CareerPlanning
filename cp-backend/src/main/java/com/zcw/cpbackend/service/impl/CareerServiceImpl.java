package com.zcw.cpbackend.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.BusinessException;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.model.dto.career.CareerAddRequest;
import com.zcw.cpbackend.model.dto.career.CareerQueryRequest;
import com.zcw.cpbackend.model.dto.career.CareerUpdateRequest;
import com.zcw.cpbackend.model.entity.Career;
import com.zcw.cpbackend.mapper.CareerMapper;
import com.zcw.cpbackend.model.vo.CareerVo;
import com.zcw.cpbackend.service.CareerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 职业表 服务层实现。
 *
 * @author zcw
 */
@Service
public class CareerServiceImpl extends ServiceImpl<CareerMapper, Career>  implements CareerService{

    @Override
    public boolean addCareer(CareerAddRequest careerAddRequest) {
        if (careerAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        String name = careerAddRequest.getName();
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职业名称不能为空");
        }
        
        // 创建职业对象
        Career career = new Career();
        career.setName(name);
        career.setDescription(careerAddRequest.getDescription());
        career.setRequiredSkills(careerAddRequest.getRequiredSkills());
        career.setJobOutlook(careerAddRequest.getJobOutlook());
        career.setAverageSalary(careerAddRequest.getAverageSalary());
        career.setCreatedAt(LocalDateTime.now());
        career.setUpdatedAt(LocalDateTime.now());
        
        // 保存到数据库
        return save(career);
    }

    @Override
    public boolean deleteCareer(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 检查职业是否存在
        Career career = getById(id);
        if (career == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        
        // 从数据库中删除
        return removeById(id);
    }

    @Override
    public boolean updateCareer(CareerUpdateRequest careerUpdateRequest) {
        if (careerUpdateRequest == null || careerUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Long id = careerUpdateRequest.getId();
        // 检查职业是否存在
        Career oldCareer = getById(id);
        if (oldCareer == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        
        // 更新职业信息
        Career career = new Career();
        career.setId(id);
        career.setName(careerUpdateRequest.getName());
        career.setDescription(careerUpdateRequest.getDescription());
        career.setRequiredSkills(careerUpdateRequest.getRequiredSkills());
        career.setJobOutlook(careerUpdateRequest.getJobOutlook());
        career.setAverageSalary(careerUpdateRequest.getAverageSalary());
        career.setUpdatedAt(LocalDateTime.now());
        
        // 更新数据库
        return updateById(career);
    }

    @Override
    public Page<CareerVo> queryCareer(CareerQueryRequest careerQueryRequest) {
        if (careerQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 构建查询条件
        QueryWrapper queryWrapper = QueryWrapper.create();
        String name = careerQueryRequest.getName();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Career::getName, name);
        }
        
        String description = careerQueryRequest.getDescription();
        if (StringUtils.isNotBlank(description)) {
            queryWrapper.like(Career::getDescription, description);
        }
        
        String requiredSkills = careerQueryRequest.getRequiredSkills();
        if (StringUtils.isNotBlank(requiredSkills)) {
            queryWrapper.like(Career::getRequiredSkills, requiredSkills);
        }
        
        String jobOutlook = careerQueryRequest.getJobOutlook();
        if (StringUtils.isNotBlank(jobOutlook)) {
            queryWrapper.like(Career::getJobOutlook, jobOutlook);
        }
        
        String averageSalary = careerQueryRequest.getAverageSalary();
        if (StringUtils.isNotBlank(averageSalary)) {
            queryWrapper.like(Career::getAverageSalary, averageSalary);
        }
        
        // 默认按创建时间降序排序
        queryWrapper.orderBy(Career::getCreatedAt, false);
        
        // 分页查询
        long current = careerQueryRequest.getCurrent();
        long pageSize = careerQueryRequest.getPageSize();
        Page<Career> careerPage = page(new Page<>(current, pageSize), queryWrapper);
        // 转换为 CareerVo
        Page<CareerVo> careerVoPage = new Page<>(careerPage.getPageNumber(), careerPage.getPageSize(), careerPage.getTotalRow());
        List<CareerVo> careerVos = careerPage.getRecords().stream().map(career -> {
            CareerVo careerVo = new CareerVo();
            careerVo.setId(career.getId());
            careerVo.setName(career.getName());
            careerVo.setDescription(career.getDescription());
            careerVo.setRequiredSkills(career.getRequiredSkills());
            careerVo.setJobOutlook(career.getJobOutlook());
            careerVo.setAverageSalary(career.getAverageSalary());
            careerVo.setCreatedAt(career.getCreatedAt());
            careerVo.setUpdatedAt(career.getUpdatedAt());
            return careerVo;
        }).collect(Collectors.toList());

        careerVoPage.setRecords(careerVos);
        return careerVoPage;
    }

    @Override
    public CareerVo queryCareerById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        return CareerVo.objToVo(getById(id));
    }
}
