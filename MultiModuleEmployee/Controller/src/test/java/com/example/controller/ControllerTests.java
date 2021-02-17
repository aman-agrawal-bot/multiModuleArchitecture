package com.example.controller;

import static com.example.commonInfrastructure.Paths.DELETE_BY_ID;
import static com.example.commonInfrastructure.Paths.EDIT_EMPLOYEE;
import static com.example.commonInfrastructure.Paths.FIND_ALL_PAGEABLE;
import static com.example.commonInfrastructure.Paths.FIND_ALL_SUPERIORS;
import static com.example.commonInfrastructure.Paths.FIND_ALL_UNDERLINGS;
import static com.example.commonInfrastructure.Paths.FIND_BY_ID;
import static com.example.commonInfrastructure.Paths.FIND_SUPERIORS;
import static com.example.commonInfrastructure.Paths.FIND_UNDERLINGS;
import static com.example.commonInfrastructure.Paths.REQUEST_MAPPING;
import static com.example.commonInfrastructure.Paths.SAVE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.Entity.Employee;
import com.example.Service.Service;
import com.example.commonInfrastructure.ApiResponse;
import com.example.commonInfrastructure.ResponseEmployee;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerTests {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock
  private Service service;
  @InjectMocks
  private Controller controller;

  private static final String TEST_DESIGNATION = "manager";
  private static final String TEST_NAME = "aman";
  private static final String NO_ERROR = null;
  private static final String ID_EXIST_ERROR = "No value present";

  private MockMvc mockMvc;
  private List<ResponseEmployee> employeeList = new ArrayList<>();
  private Employee testEmployee = new Employee();
  private ResponseEmployee responseEmployee = new ResponseEmployee();
  private ApiResponse apiResponse = new ApiResponse();
  //  private ApiResponse

  private void setUpSuccess() {
    testEmployee.setDesignation(TEST_DESIGNATION);
    testEmployee.setId(2);
    testEmployee.setManagerId(1);
    testEmployee.setName(TEST_NAME);
    responseEmployee.setName(TEST_NAME);
    responseEmployee.setDesignation(TEST_DESIGNATION);
    apiResponse.setSuccess(true);
    apiResponse.setError(NO_ERROR);
    employeeList.add(responseEmployee);
    apiResponse.setResult(employeeList);
  }

  private void setUpFailure() {
    testEmployee.setDesignation(TEST_DESIGNATION);
    testEmployee.setId(2);
    testEmployee.setManagerId(1);
    testEmployee.setName(TEST_NAME);
    apiResponse.setSuccess(false);
    apiResponse.setError(ID_EXIST_ERROR);
  }

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void save_whenIdExists() throws Exception {
    setUpSuccess();
    when(service.save(ArgumentMatchers.any())).thenReturn(apiResponse);
    mockMvc.perform(post(REQUEST_MAPPING + SAVE).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)))
        .andExpect(jsonPath("$.result[0].name", CoreMatchers.equalTo(TEST_NAME)));
    Mockito.verify(service).save(testEmployee);
    //        andExpect(status().)
  }

  @Test
  public void save_whenNameNull() throws Exception {
    setUpSuccess();
    ApiResponse localApiResponse = new ApiResponse<String>(false, "empty name", null);
    when(service.save(ArgumentMatchers.any())).thenReturn(localApiResponse);
    mockMvc.perform(post(REQUEST_MAPPING + SAVE).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo("empty name")))
        .andExpect(jsonPath("$.result", CoreMatchers.equalTo(null)));
    Mockito.verify(service).save(testEmployee);
    //        andExpect(status().)
  }

  @Test
  public void save_whenDesignationNull() throws Exception {
    setUpSuccess();
    ApiResponse localApiResponse = new ApiResponse<String>(false, "empty designation", null);
    when(service.save(ArgumentMatchers.any())).thenReturn(localApiResponse);
    mockMvc.perform(post(REQUEST_MAPPING + SAVE).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo("empty designation")))
        .andExpect(jsonPath("$.result", CoreMatchers.equalTo(null)));
    Mockito.verify(service).save(testEmployee);
    //        andExpect(status().)
  }

  @Test
  public void save_whenManagerDoesntExist() throws Exception {
    setUpSuccess();
    ApiResponse localApiResponse = new ApiResponse<String>(false, ID_EXIST_ERROR, null);
    when(service.save(ArgumentMatchers.any())).thenReturn(localApiResponse);
    mockMvc.perform(post(REQUEST_MAPPING + SAVE).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(ID_EXIST_ERROR)))
        .andExpect(jsonPath("$.result", CoreMatchers.equalTo(null)));
    Mockito.verify(service).save(testEmployee);
  }

  //  @Test
  //  public void findAll() throws Exception {
  //    setUpSuccess();
  //    when(service.findAll()).thenReturn(apiResponse);
  //    mockMvc.perform(get(REQUEST_MAPPING + FIND_ALL).contentType(MediaType.APPLICATION_JSON)
  //        .content(new ObjectMapper().writeValueAsString(testEmployee))).
  //        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
  //        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)))
  //        .andExpect(jsonPath("$.result[0].name", CoreMatchers.equalTo(TEST_NAME)));
  //    Mockito.verify(service).findAll();
  //  }
  //
  @Test
  public void findById_whenIdExists() throws Exception {
    setUpSuccess();
    when(service.findById(2)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + FIND_BY_ID, 2).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)))
        .andExpect(jsonPath("$.result[0].name", CoreMatchers.equalTo(TEST_NAME)));
    Mockito.verify(service).findById(2);
  }

  @Test
  public void findByIdError_whenNoIdExists() throws Exception {
    setUpFailure();
    when(service.findById(200)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + FIND_BY_ID, 200).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(ID_EXIST_ERROR)));
    //        .andExpect(jsonPath("$.result[0].id",CoreMatchers.equalTo(2)));
    Mockito.verify(service).findById(200);
  }

  //
  @Test
  public void findUnderlings_whenIdExists() throws Exception {
    setUpSuccess();
    when(service.findUnderlings(1)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + FIND_UNDERLINGS, 1).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)))
        .andExpect(jsonPath("$.result[0].name", CoreMatchers.equalTo(TEST_NAME)));
    Mockito.verify(service).findUnderlings(1);
  }

  @Test
  public void findUnderlingError_whenNoIdExists() throws Exception {
    setUpFailure();
    when(service.findUnderlings(200)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + FIND_UNDERLINGS, 200).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(ID_EXIST_ERROR)));
    //        .andExpect(jsonPath("$.result[0].id",CoreMatchers.equalTo(2)));
    Mockito.verify(service).findUnderlings(200);
  }

  @Test
  public void findSuperiors_whenIdExists() throws Exception {
    setUpSuccess();
    when(service.findSuperiors(1)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + FIND_SUPERIORS, 1).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)))
        .andExpect(jsonPath("$.result[0].name", CoreMatchers.equalTo(TEST_NAME)));
    Mockito.verify(service).findSuperiors(1);
  }

  @Test
  public void findSuperiorsError_whenNoIdExists() throws Exception {
    setUpFailure();
    when(service.findSuperiors(200)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + FIND_SUPERIORS, 200).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).andExpect(status().isOk())
        .andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(ID_EXIST_ERROR)));
    //        .andExpect(jsonPath("$.result[0].id",CoreMatchers.equalTo(2)));
    Mockito.verify(service).findSuperiors(200);
  }

    @Test
    public void findAllUnderlings_whenIdExists() throws Exception {
      setUpSuccess();
      when(service.findAllUnderlings(1)).thenReturn(apiResponse);
      mockMvc.perform(get(REQUEST_MAPPING + FIND_ALL_UNDERLINGS, 1).contentType(MediaType.APPLICATION_JSON)
          .content(new ObjectMapper().writeValueAsString(testEmployee))).andExpect(status().isOk())
          .andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
          .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)))
          .andExpect(jsonPath("$.result[0].name", CoreMatchers.equalTo(TEST_NAME)));
      Mockito.verify(service).findAllUnderlings(1);
    }

    @Test
    public void findAllUnderlingsError_whenNoIdExists() throws Exception {
      setUpFailure();
      when(service.findAllUnderlings(200)).thenReturn(apiResponse);
      mockMvc.perform(get(REQUEST_MAPPING + FIND_ALL_UNDERLINGS, 200).contentType(MediaType.APPLICATION_JSON)
          .content(new ObjectMapper().writeValueAsString(testEmployee))).
          andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
          .andExpect(jsonPath("$.error", CoreMatchers.equalTo(ID_EXIST_ERROR)));
      //        .andExpect(jsonPath("$.result[0].id",CoreMatchers.equalTo(2)));
      Mockito.verify(service).findAllUnderlings(200);
    }

  @Test
  public void findAllSuperiors_whenIdExists() throws Exception {
    setUpSuccess();
    when(service.findAllSuperiors(1)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + FIND_ALL_SUPERIORS, 1).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)))
        .andExpect(jsonPath("$.result[0].name", CoreMatchers.equalTo(TEST_NAME)));
    Mockito.verify(service).findAllSuperiors(1);
  }

  @Test
  public void findAllSuperiorsError_whenNoIdExists() throws Exception {
    setUpFailure();
    when(service.findAllSuperiors(200)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + FIND_ALL_SUPERIORS, 200).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(ID_EXIST_ERROR)));
    //        .andExpect(jsonPath("$.result[0].id",CoreMatchers.equalTo(2)));
    Mockito.verify(service).findAllSuperiors(200);
  }

  @Test
  public void deleteByIdTest_whenIdExists() throws Exception {
    setUpSuccess();
    when(service.deleteById(1, 2)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + DELETE_BY_ID, 1, 2).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)));
    Mockito.verify(service).deleteById(1, 2);

  }

  @Test
  public void deleteByIdError_whenNoIdExists() throws Exception {
    setUpFailure();
    when(service.deleteById(200, 2)).thenReturn(apiResponse);
    mockMvc.perform(get(REQUEST_MAPPING + DELETE_BY_ID, 200, 2).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(ID_EXIST_ERROR)));
    //        .andExpect(jsonPath("$.result[0].id",CoreMatchers.equalTo(2)));
    Mockito.verify(service).deleteById(200, 2);
  }

  @Test
  public void edit_whenIdExists() throws Exception {
    setUpSuccess();
    when(service.editEmployee(ArgumentMatchers.any())).thenReturn(apiResponse);
    mockMvc.perform(post(REQUEST_MAPPING + EDIT_EMPLOYEE).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)))
        .andExpect(jsonPath("$.result[0].name", CoreMatchers.equalTo(TEST_NAME)));
    Mockito.verify(service).editEmployee(testEmployee);
    //        andExpect(status().)
  }

  @Test
  public void edit_whenNoIdExists() throws Exception {
    setUpFailure();
    //    setUpSuccess();
    when(service.editEmployee(ArgumentMatchers.any())).thenReturn(apiResponse);
    mockMvc.perform(post(REQUEST_MAPPING + EDIT_EMPLOYEE).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(testEmployee))).
        andExpect(status().isOk()).andExpect(jsonPath("$.success", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(ID_EXIST_ERROR)));
    //        .andExpect(jsonPath("$.result[0].id",CoreMatchers.equalTo(2)));
    Mockito.verify(service).editEmployee(testEmployee);
  }

  @Test
  public void findAllPageable() throws Exception {
    setUpSuccess();
    List<ResponseEmployee> testList = new ArrayList<>();
    testList.add(new ResponseEmployee());
    testList.add(new ResponseEmployee());
    Page<Employee> testPage = new PageImpl(testList);
    when(service.findAllPageable(1)).thenReturn(testPage);
    mockMvc.perform(get(REQUEST_MAPPING + FIND_ALL_PAGEABLE, 1).contentType(MediaType.APPLICATION_JSON)
        //        .content(new ObjectMapper().writeValueAsString(testEmployee))
    ).
        andExpect(status().isOk());
    //TODO check how to check pageable type in unit testing
    //        .andExpect(jsonPath("$.success", CoreMatchers.equalTo(true)))
    //        .andExpect(jsonPath("$.error", CoreMatchers.equalTo(NO_ERROR)))
    //        .andExpect(jsonPath("$.result[0].name", CoreMatchers.equalTo(TEST_NAME)));
    Mockito.verify(service).findAllPageable(1);
  }

  @After
  public void teardown() {
    Mockito.verifyNoMoreInteractions(service);
    log.info("teardown successful");
  }

}

