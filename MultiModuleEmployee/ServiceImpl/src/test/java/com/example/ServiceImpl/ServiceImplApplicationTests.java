package com.example.ServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.Entity.Employee;
import com.example.Repository.Repository;
import com.example.commonInfrastructure.ApiResponse;
import com.example.commonInfrastructure.ResponseEmployee;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceImplApplicationTests {

  @Mock
  private Repository repository;

  @InjectMocks
  private ServiceImpl serviceImpl;

  private MockMvc mockMvc;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  private static final String TEST_DESIGNATION = "manager";
  private static final String TEST_NAME = "aman";
  private static final String NO_ERROR = null;
  private static final String ID_EXIST_ERROR = "No value present";

  private List<Employee> employeeList = new ArrayList<>();
  private Employee testEmployee1 = new Employee();
  private Employee testEmployee2 = new Employee();
  private ApiResponse apiResponse = new ApiResponse();
  private List<Employee> emptyList = new ArrayList<>();
  private Exception exception;

  @Before
  public void setUpSuccess() {
    testEmployee1.setDesignation(TEST_DESIGNATION);
    testEmployee1.setId(2);
    testEmployee1.setManagerId(1);
    testEmployee1.setName(TEST_NAME);
    testEmployee1.setPeopleUnderMe(new ArrayList<>());
    testEmployee1.setManager(testEmployee2);
    testEmployee2.setDesignation(TEST_DESIGNATION);
    testEmployee2.setId(1);
    testEmployee2.setManagerId(0);
    testEmployee2.setPeopleUnderMe(new ArrayList<>());
    testEmployee2.getPeopleUnderMe().add(testEmployee1);
    testEmployee2.setName(TEST_NAME);
    apiResponse.setSuccess(true);
    apiResponse.setError(NO_ERROR);
    employeeList.add(testEmployee1);
    exception = new IndexOutOfBoundsException(ID_EXIST_ERROR);
  }


  @Test
  public void save_noError() {
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    when(repository.save(ArgumentMatchers.any())).thenReturn(null);
    Employee localtestentity = new Employee();
    localtestentity.setManagerId(1);
    ApiResponse result = serviceImpl.save(testEmployee1);
    Mockito.verify(repository).findById(1);
    Mockito.verify(repository).save(testEmployee1);
    assertEquals(result.getError(), NO_ERROR);
  }

  @Test
  public void save_whenId0() {
    when(repository.save(testEmployee2)).thenReturn(null);
    ApiResponse<ResponseEmployee> result = serviceImpl.save(testEmployee2);
    Mockito.verify(repository).save(testEmployee2);
    assertEquals(result.getResult().getName(), TEST_NAME);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getError(), NO_ERROR);
  }

  @Test
  public void saveFail_NoManagerId() {
    when(repository.findById(1)).thenThrow(exception);
    ApiResponse result = serviceImpl.save(testEmployee1);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    Mockito.verify(repository).findById(1);
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void saveFail_NoName() {
    Employee localEmployee = new Employee();
    localEmployee.setId(10);
    localEmployee.setDesignation("asd");
    ApiResponse result = serviceImpl.save(localEmployee);
    assertEquals(result.getError(), "empty name");
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void saveFail_NoDesignation() {
    Employee localEmployee = new Employee();
    localEmployee.setId(10);
    localEmployee.setName("asd");
    ApiResponse result = serviceImpl.save(localEmployee);
    assertEquals(result.getError(), "empty designation");
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void findById_whenIdPresent() {
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    ApiResponse result = serviceImpl.findById(1);
    Mockito.verify(repository).findById(1);
    //Mockito.verify(repository).existsById(2);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
  }

  @Test
  public void findById_whenIdPresentNoManager() {
    when(repository.findById(2)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    ApiResponse result = serviceImpl.findById(2);
    Mockito.verify(repository).findById(2);
    //Mockito.verify(repository).existsById(2);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
  }

  @Test
  public void findByIdFail_whenIdNotPresent() {
    when(repository.findById(1)).thenThrow(exception);
    ApiResponse result = serviceImpl.findById(1);
    Mockito.verify(repository).findById(1);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void findSuperiors_whenIdPresent() {
    when(repository.findById(2)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    ApiResponse<Employee> result = serviceImpl.findSuperiors(2);
    Mockito.verify(repository).findById(2);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getResult().getName(), testEmployee1.getName());
  }

  @Test
  public void findSuperiors_whenIdPresentNoManager() {
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    ApiResponse<Employee> result = serviceImpl.findSuperiors(1);
    Mockito.verify(repository).findById(1);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getResult(), null);
  }

  @Test
  public void findSuperiorsFail_whenIdNotPresent() {
    when(repository.findById(1)).thenThrow(exception);
    ApiResponse result = serviceImpl.findSuperiors(1);
    Mockito.verify(repository).findById(1);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void findAllSuperiors_whenIdPresent() {
    when(repository.findById(2)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    ApiResponse<List<ResponseEmployee>> result = serviceImpl.findAllSuperiors(2);
    Mockito.verify(repository).findById(2);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.getResult().get(0).getName(), TEST_NAME);
  }

  @Test
  public void findAllSuperiorsFail_whenIdNotPresent() {
    when(repository.findById(1)).thenThrow(exception);
    ApiResponse result = serviceImpl.findAllSuperiors(1);
    Mockito.verify(repository).findById(1);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void deleteSuccess() {
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    when(repository.findById(2)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    ApiResponse<String> result = serviceImpl.deleteById(2, 1);
    Mockito.verify(repository).findById(1);
    Mockito.verify(repository).findById(2);
    Mockito.verify(repository).deleteById(2);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.getResult(), "employee delete successful");
  }

  @Test
  public void deleteSuccess2() {
    //    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    ApiResponse<String> result = serviceImpl.deleteById(1, 0);
    //    Mockito.verify(repository).findById(1);
    Mockito.verify(repository).findById(1);
    Mockito.verify(repository).deleteById(1);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.getResult(), "employee delete successful");
  }

  @Test
  public void deleteSuccess3() {
    when(repository.findById(2)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    ApiResponse<String> result = serviceImpl.deleteById(1, 2);
    Mockito.verify(repository).findById(2);
    Mockito.verify(repository).findById(1);
    Mockito.verify(repository).deleteById(1);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.getResult(), "employee delete successful");
  }

  @Test
  public void deleteFail_whenToDeleteDoesntExist() {
    when(repository.findById(2)).thenThrow(exception);
    ApiResponse<String> result = serviceImpl.deleteById(2, 1);
    Mockito.verify(repository).findById(2);
    assertEquals(result.isSuccess(), false);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    assertEquals(result.getResult(), null);
  }

  @Test
  public void deleteFail_whenNewManagerDoesntExist() {
    when(repository.findById(1)).thenThrow(exception);
    when(repository.findById(2)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    ApiResponse<String> result = serviceImpl.deleteById(2, 1);
    Mockito.verify(repository).findById(1);
    Mockito.verify(repository).findById(2);
    assertEquals(result.isSuccess(), false);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    assertEquals(result.getResult(), null);
  }

  @Test
  public void editEmployee_whenIdPresent() {
    Employee localEmployee = new Employee();
    localEmployee.setName("n3");
    localEmployee.setManager(testEmployee1);
    localEmployee.setManagerId(2);
    localEmployee.setId(3);
    localEmployee.setDesignation("d3");
    localEmployee.setPeopleUnderMe(new ArrayList<>());
    testEmployee1.getPeopleUnderMe().add(localEmployee);
    when(repository.findById(3)).thenReturn(java.util.Optional.ofNullable(localEmployee));
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    Employee localEmployeeParameter = new Employee();
    localEmployeeParameter.setName("n3");
    localEmployeeParameter.setManagerId(1);
    localEmployeeParameter.setId(3);
    localEmployeeParameter.setDesignation("d3");
    localEmployeeParameter.setPeopleUnderMe(new ArrayList<>());
    ApiResponse<String> result = serviceImpl.editEmployee(localEmployeeParameter);
    Mockito.verify(repository).findById(3);
    Mockito.verify(repository).findById(1);
    Mockito.verify(repository).save(ArgumentMatchers.any());
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getResult(), "Update successful");
  }

  @Test
  public void editEmployee_whenIdPresent2() {
    Employee localEmployee = new Employee();
    localEmployee.setName("n3");
    localEmployee.setManager(testEmployee1);
    localEmployee.setManagerId(2);
    localEmployee.setId(3);
    localEmployee.setDesignation("d3");
    localEmployee.setPeopleUnderMe(new ArrayList<>());
    testEmployee1.getPeopleUnderMe().add(localEmployee);
    when(repository.findById(3)).thenReturn(java.util.Optional.ofNullable(localEmployee));
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    Employee localEmployeeParameter = new Employee();
    localEmployeeParameter.setName("n3");
    localEmployeeParameter.setManagerId(0);
    localEmployeeParameter.setId(3);
    localEmployeeParameter.setDesignation("d3");
    localEmployeeParameter.setPeopleUnderMe(new ArrayList<>());
    ApiResponse<String> result = serviceImpl.editEmployee(localEmployeeParameter);
    Mockito.verify(repository).findById(3);
    //    Mockito.verify(repository).findById(1);
    Mockito.verify(repository).save(ArgumentMatchers.any());
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getResult(), "Update successful");
  }

  @Test
  public void editEmployeeFail_whenIdNotPresent() {
    when(repository.findById(2)).thenThrow(exception);
    ApiResponse result = serviceImpl.editEmployee(testEmployee1);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    Mockito.verify(repository).findById(2);
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void editEmployeeFail2_whenManagerIdNotPresent() {
    when(repository.findById(2)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    when(repository.findById(1)).thenThrow(exception);
    ApiResponse result = serviceImpl.editEmployee(testEmployee1);
    Mockito.verify(repository).findById(1);
    Mockito.verify(repository).findById(2);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void editFail_NoName() {
    Employee localEmployee = new Employee();
    localEmployee.setId(10);
    localEmployee.setDesignation("asd");
    ApiResponse result = serviceImpl.editEmployee(localEmployee);
    assertEquals(result.getError(), "empty name");
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void editFail_NoDesignation() {
    Employee localEmployee = new Employee();
    localEmployee.setId(10);
    localEmployee.setName("asd");
    ApiResponse result = serviceImpl.editEmployee(localEmployee);
    assertEquals(result.getError(), "empty designation");
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void findAllPageable() {
    org.springframework.data.domain.Pageable firstPageWithTwoElements = PageRequest.of(0, 2);
    List<Employee> testList = new ArrayList<>();
    testList.add(new Employee());
    testList.add(new Employee());
    Page<Employee> testPage = new PageImpl(testList);
    when(repository.findAll(ArgumentMatchers.any(org.springframework.data.domain.Pageable.class))).thenReturn(testPage);
    Long result = serviceImpl.findAllPageable(1).getTotalElements();
    Mockito.verify(repository).findAll(ArgumentMatchers.any(org.springframework.data.domain.Pageable.class));
    assertEquals(result.intValue(), 2);
  }

  @Test
  public void findUnderlingSuccess() {
    when(repository.findById(2)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    ApiResponse<List<ResponseEmployee>> result = serviceImpl.findUnderlings(2);
    Mockito.verify(repository).findById(2);
    assertEquals(result.getResult().size(), 0);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
  }

  @Test
  public void findUnderlingSuccess2() {
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    ApiResponse<List<ResponseEmployee>> result = serviceImpl.findUnderlings(1);
    Mockito.verify(repository).findById(1);
    assertEquals(result.getResult().size(), 1);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
  }

  @Test
  public void findUnderlings_whenIdNotPresent() {
    when(repository.findById(2)).thenThrow(exception);
    ApiResponse result = serviceImpl.findUnderlings(2);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    Mockito.verify(repository).findById(2);
    assertEquals(result.isSuccess(), false);
  }

  @Test
  public void findAllUnderlingSuccess() {
    when(repository.findById(2)).thenReturn(java.util.Optional.ofNullable(testEmployee1));
    ApiResponse<List<ResponseEmployee>> result = serviceImpl.findAllUnderlings(2);
    Mockito.verify(repository).findById(2);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
  }

  //  @Test
  //  public void findAllUnderlingSuccess2() {
  //    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
  //    ApiResponse<List<ResponseEmployee>> result = serviceImpl.findAllUnderlings(1);
  //    Mockito.verify(repository).findById(1);
  //    assertEquals(result.getError(), NO_ERROR);
  //    assertEquals(result.isSuccess(), true);
  //  }


  @Test
  public void findAllUnderlingSuccess2() {
    when(repository.findById(1)).thenReturn(java.util.Optional.ofNullable(testEmployee2));
    ApiResponse<List<ResponseEmployee>> result = serviceImpl.findAllUnderlings(1);
    Mockito.verify(repository).findById(1);
    assertEquals(result.getError(), NO_ERROR);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.getResult().get(0).getName(), TEST_NAME);
  }

  @Test
  public void findAllUnderlings_whenIdNotPresent() {
    when(repository.findById(2)).thenThrow(exception);
    ApiResponse result = serviceImpl.findAllUnderlings(2);
    assertEquals(result.getError(), ID_EXIST_ERROR);
    Mockito.verify(repository).findById(2);
    assertEquals(result.isSuccess(), false);
  }

  @After
  public void teardown() {
    Mockito.verifyNoMoreInteractions(repository);
    log.info("teardown successful");
  }

}
