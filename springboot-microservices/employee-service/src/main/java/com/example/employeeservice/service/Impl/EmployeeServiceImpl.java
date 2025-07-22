package com.example.employeeservice.service.Impl;

import com.example.employeeservice.dto.APIResponseDto;
import com.example.employeeservice.dto.DepartmentDto;
import com.example.employeeservice.dto.EmployeeDto;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.mapper.EmployeeMapper;
import com.example.employeeservice.repository.EmployeeRespository;
import com.example.employeeservice.service.APIClient;
import  com.example.employeeservice.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cglib.core.Block;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl
            .class);

    private EmployeeRespository employeeRespository;

   // private RestTemplate restTemplate;
   private WebClient webClient;
 private APIClient apiClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        // Convert DTO to JPA entity
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        // Save to database (assumes employeeRepository is injected)
        Employee savedEmployee = employeeRespository.save(employee);

        // Convert saved entity back to DTO
        EmployeeDto savedEmployeeDto = EmployeeMapper.maptoEmployeeDto(savedEmployee);

        return savedEmployeeDto;
    }

    // @CircuitBreaker(name ="${spring.application.name}",fallbackMethod ="getDefaultDepartment")
 @Retry(name ="${spring.application.name}",fallbackMethod ="getDefaultDepartment")

 @Override
    public APIResponseDto getEmployeeById(Long employeeId) {

      Employee employee = employeeRespository.findById(employeeId).get();

//      ResponseEntity<DepartmentDto> responseEntity =restTemplate.getForEntity("http://localhost:8080/api/departments/" + employee.getDepartmentCode(),DepartmentDto.class);
//
//      DepartmentDto departmentDto = responseEntity.getBody();

     LOGGER.info("inside getEmployeeById() method");
        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

        departmentDto = apiClient.getDepartment(employee.getDepartmentCode());

        EmployeeDto employeeDto = EmployeeMapper.maptoEmployeeDto(employee);


        APIResponseDto apiResponseDto=new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);



        return apiResponseDto;
    }
    public APIResponseDto getDefaultDepartment(Long employeeId,Exception exception){
        Employee employee = employeeRespository.findById(employeeId).get();

        LOGGER.info("inside getDefaultDepartment() method");

DepartmentDto departmentDto = new DepartmentDto();
departmentDto.setDepartmentName("R&D Department");
departmentDto.setDepartmentCode("RD001");
departmentDto.setDepartmentCode("Research and Development Department");



EmployeeDto employeeDto = EmployeeMapper.maptoEmployeeDto(employee);



        APIResponseDto apiResponseDto=new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);
        return apiResponseDto;
    }
}
