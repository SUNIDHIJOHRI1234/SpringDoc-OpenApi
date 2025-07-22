package com.example.departmentservice.service.Impl;

import com.example.departmentservice.mapper.DepartmentMapper;
import lombok.AllArgsConstructor;
import com.example.departmentservice.dto.DepartmentDto;
import com.example.departmentservice.entity.Department;
import com.example.departmentservice.repository.DepartmentRepository;
import com.example.departmentservice.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {
        // Convert DTO to JPA entity
        Department department = DepartmentMapper.mapToDepartment(departmentDto);

        // Save to database (assumes departmentRepository is injected)
        Department savedDepartment = departmentRepository.save(department);

        // Convert saved entity back to DTO
        DepartmentDto savedDepartmentDto = DepartmentMapper.mapToDepartmentDto(savedDepartment);

        return savedDepartmentDto;
    }




    @Override
    public DepartmentDto getDepartmentByCode(String departmentcode) {

        Department department = departmentRepository.findByDepartmentCode(departmentcode);

        DepartmentDto departmentDto= DepartmentMapper.mapToDepartmentDto(department);


        return departmentDto;
    }
}
