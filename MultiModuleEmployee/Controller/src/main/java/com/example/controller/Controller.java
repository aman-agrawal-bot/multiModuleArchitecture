package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Entity.Employee;
import com.example.Service.Service;
import com.example.commonInfrastructure.ApiResponse;
import com.example.commonInfrastructure.Paths;
import com.example.commonInfrastructure.ResponseEmployee;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = Paths.REQUEST_MAPPING)
@Slf4j
public class Controller {
  @Autowired
  private Service service;

  @PostMapping(value = Paths.SAVE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse save(@RequestBody Employee employeeEmployee) {
    log.info("employee save method called for {} ", employeeEmployee.getId());
    return service.save(employeeEmployee);
  }

//  @GetMapping(value = Paths.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
//  public List<Employee> findAll() {
//    log.info("employee findALl method called");
//    return service.findAll();
//  }

  @GetMapping(value = Paths.FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse findById(@PathVariable("empId") int empId) {
    log.info("employee find method called for {} ", empId);
    return service.findById(empId);
  }

  @GetMapping(value = Paths.FIND_UNDERLINGS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse findUnderlings(@PathVariable("empId") int empId) {
    log.info("employee find Underlings method called for {} ", empId);
    return service.findUnderlings(empId);
  }

  @GetMapping(value = Paths.FIND_SUPERIORS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse findSuperiors(@PathVariable("empId") int empId) {
    log.info("employee find Superiors method called for {} ", empId);
    return service.findSuperiors(empId);
  }

  @GetMapping(value = Paths.FIND_ALL_UNDERLINGS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<List<ResponseEmployee>> findAllUnderlings(@PathVariable("empId") int empId) {
    log.info("employee find All Underlings method called for {} ", empId);
    return service.findAllUnderlings(empId);
  }

  @GetMapping(value = Paths.FIND_ALL_SUPERIORS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse findAllSuperiors(@PathVariable("empId") int empId) {
    log.info("employee find All Superiors method called for {} ", empId);
    return service.findAllSuperiors(empId);
  }

  @GetMapping(value = Paths.DELETE_BY_ID)
  public ApiResponse deleteById(@PathVariable("empId") int empId, @PathVariable("newManagerId") int newManager) {
    log.info("employee delete method called for {} ", empId);
    return service.deleteById(empId, newManager);
  }

  @PostMapping(value = Paths.EDIT_EMPLOYEE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse editEmployee(@RequestBody Employee employee) {
    return service.editEmployee(employee);
  }

  @GetMapping(value = Paths.FIND_ALL_PAGEABLE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<Employee> findAllPage(@PathVariable("pageNo") int pageNo) {
    return service.findAllPageable(pageNo);
  }

}
