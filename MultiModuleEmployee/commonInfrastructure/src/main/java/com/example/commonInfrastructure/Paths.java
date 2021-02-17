package com.example.commonInfrastructure;

public interface Paths {
  String REQUEST_MAPPING = "/api/Employee";
  String SAVE = "/save";
  String FIND_BY_ID = "/findById/{empId}";
  String FIND_UNDERLINGS = "/findUnderlings/{empId}";
  String FIND_SUPERIORS = "/findSuperiors/{empId}";
  String FIND_ALL_UNDERLINGS = "/findAllUnderlings/{empId}";
  String FIND_ALL_SUPERIORS = "/findAllSuperiors/{empId}";
  String EDIT_EMPLOYEE = "/editEmployee";
  String FIND_ALL = "/findAll";
  String DELETE_BY_ID = "/delete/{empId}/{newManagerId}";
  String FIND_ALL_PAGEABLE = "/findAllPageable/{pageNo}";
}
