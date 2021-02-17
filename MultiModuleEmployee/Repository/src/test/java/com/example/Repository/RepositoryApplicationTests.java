package com.example.Repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.Entity.Employee;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RepositoryApplicaionTestsConfiguration.class)
class RepositoryApplicationTests {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private Repository repository;

  private Employee employee;
  private Employee employee2;
  private Employee manager;

  static final String TEST_NAME = "aman";
  static final String TEST_DESIGNATION = "employee";
  static final String TEST_NAME_MANAGER = "nama";
  static final String TEST_DESIGNATION_MANAGER = "manager";


  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Before
  void setUp(){
    employee=new Employee();
    employee=new Employee(0,TEST_NAME,TEST_DESIGNATION,0,null,null);
    employee2=new Employee(0,TEST_NAME,TEST_DESIGNATION,0,null,null);
    manager=new Employee(0,TEST_NAME_MANAGER,TEST_DESIGNATION_MANAGER,0,null,new ArrayList<>());
////    employee.setManager(manager);
//    manager.getPeopleUnderMe().add(employee);
    entityManager.persist(manager);
    entityManager.flush();
    employee.setManagerId(manager.getId());
    employee2.setManagerId(manager.getId());
    entityManager.persist(employee);
    entityManager.flush();
    entityManager.persist(employee2);
    entityManager.flush();
//    employee.setManager(manager);
  }

  @Test
  void findById(){
    setUp();
    Employee result=repository.findById(employee.getId()).get();
    log.info("save test for employee {}",result.toString());
    assertEquals(employee,result);
  }

  @Test
  void findById_whenNoId(){
    setUp();
    try {
      Employee result = repository.findById(100).get();
      fail();
    }
    catch (NoSuchElementException e){
      assertEquals(e.getMessage(),"No value present");
    }

  }

  @Test
  void findByNameTest(){
    setUp();
    List<Employee> result=repository.findByName(employee.getName());
    log.info("save test for employee {}",result.toString());
    assertEquals(employee,result.get(0));
  }

  @Test
  void findByNameTest_noNamePresent(){
    setUp();
    List<Employee> result=repository.findByName("fail");
//    log.info("save test for employee {}",result.toString());
    assertEquals(0,result.size());
  }

  @Test
  void findAll(){
    setUp();
    List<Employee> result=repository.findAll();
    for (Employee e: result){
      log.info("the find all returned {}",e.toString());
    }
    assertEquals(result.size(),3);
  }
  @Test
  void save(){
    setUp();
    Employee result=repository.save(employee);
    assertEquals(result,employee);
  }

  @Test
  void findIdByDesignation(){
    setUp();
    List<Integer> result=repository.findIdByDesignation(employee.getDesignation(),employee.getId());
    assertEquals(result.size(),2);
  }
  @Test
  void findIdByDesignation_NoDesignationPresent(){
    setUp();
    List<Integer> result=repository.findIdByDesignation("fail",employee.getId());
    assertEquals(result.size(),0);
  }

  @Test
  void findEmployeeByManager(){
    setUp();
    List<Employee> result=repository.findByManagerId(employee.getManagerId());
    assertEquals(result.size(),2);
  }
  @Test
  void findEmployeeByManager_NoManagerPresent(){
    setUp();
    List<Employee> result=repository.findByManagerId(1000);
    assertEquals(result.size(),0);
  }

  @Test
  void editName(){
    setUp();
    System.out.println(employee.getId());
    Employee priorChange=repository.findById(employee.getId()).get();
    assertEquals(priorChange.getName(),TEST_NAME);
    repository.updateNameById("newName",employee.getId());
    entityManager.clear();
    Employee result=repository.findById(employee.getId()).get();
    for(Employee e:repository.findAll())
      System.out.println(e.toString());
    assertEquals(result.getName(),"newName");
  }


}
