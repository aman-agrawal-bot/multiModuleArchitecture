package com.example.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.Entity.Employee;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Employee, Integer> {

  List<Employee> findByManagerId(int managerId);

  List<Employee> findByName(String name);

  @Query(value = "select e.id from Employee e where e.designation = ?1")
  List<Integer> findIdByDesignation(String designation, int toFind);

  @Transactional
  @Modifying
  @Query(value = "update Employee e set e.name = ?1 where e.id = ?2")
  void updateNameById(String nawName,int id);
  //  @Query(value = "select B.* from multi_module_employee A,multi_module_employee B where A.id = B.manager_id and A.id= ?1", nativeQuery = true)
  //  List<Employee> findUnderlings(int empId);

  //  @Query(value = "select A.* from multi_module_employee A,multi_module_employee B where A.id = B.manager_id and B.id= ?1", nativeQuery = true)
  //  List<Employee> findSuperiors(int empId);


  //  @Query(value = "select id from multi_module_employee where designation= ?1 and id!= ?2", nativeQuery = true)
  //  List<Integer> findIdByDesignation(String designation, int toFind);

  //  @Transactional
  //  @Modifying
  //  @Query(value = "update Employee e set e.managerId = ?2 where e.managerId = ?1")
  //  void changeManager(int oldManagerId, int newManagerId);
  //

  //
  //  @Query(value = "select A from Employee A,Employee B where A.id = B.managerId and B.id = ?1")
  //  List<Employee> findSuperiors(int empId);
  //
  //  @Query(value = "select B from Employee A,Employee B where A.id = B.managerId and A.id = ?1")
  //  List<Employee> findUnderlings(int empId);

  //  @Query(value = "select e from Employee e")
  //  List<Employee> findAll();
  /*
  for pagination
  @Query(value = "SELECT u FROM User u ORDER BY id")
Page<User> findAllUsersWithPagination(Pageable pageable);
   */

}
