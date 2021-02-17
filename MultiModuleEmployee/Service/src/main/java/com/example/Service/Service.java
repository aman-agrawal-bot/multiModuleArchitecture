package com.example.Service;

import java.util.List;

import com.example.Entity.Employee;
import com.example.commonInfrastructure.ApiResponse;
import com.example.commonInfrastructure.ResponseEmployee;

public interface Service {
  /**
   * Save an entity in db and check if manager id exist
   *
   * @param employeeEmployee
   * @return response entity
   */
  ApiResponse save(Employee employeeEmployee);
//
//  /**
//   * Return all employees
//   *
//   * @return response entity
//   */
//  List<Employee> findAll();

  /**
   * Find particular employee by id
   *
   * @param empId
   * @return response entity
   */
  ApiResponse findById(int empId);

  /**
   * Find immediate underlings of employee
   *
   * @param empId
   * @return response entity
   */
  ApiResponse findUnderlings(int empId);

  /**
   * Find immediate superiors of employee
   *
   * @param empId
   * @return response entity
   */
  ApiResponse findSuperiors(int empId);

  /**
   * Find all underlings of employee
   *
   * @param empId
   * @return response entity
   */
  ApiResponse<List<ResponseEmployee>> findAllUnderlings(int empId);

  /**
   * Find all superiors of employee
   *
   * @param empId
   * @return response entity
   */
  ApiResponse findAllSuperiors(int empId);

  /**
   * delete an employee and switch underlings to other of same rank or freelance
   *
   * @param empId
   * @return
   */
  ApiResponse deleteById(int empId, int newManagerId);

  /**
   * edit details of employee
   *
   * @param employee
   * @return
   */
  ApiResponse editEmployee(Employee employee);

  /**
   * find all with pageable
   *
   * @return
   */
  org.springframework.data.domain.Page<Employee> findAllPageable(int pageNo);

//  /**
//   * find All superiors Recursively while updating cache
//   *
//   * @param empId
//   * @return
//   */
//  ApiResponse findAllSuperiorsRecursively(int empId);
//
//  /**
//   * find underlings recursively
//   *
//   * @param empId
//   * @return
//   */
//  ApiResponse findAllUnderlingsRecursively(int empId);
}
