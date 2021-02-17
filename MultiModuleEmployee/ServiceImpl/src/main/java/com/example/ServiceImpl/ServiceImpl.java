package com.example.ServiceImpl;

import static com.example.commonInfrastructure.StringConstants.SUPERIOR_CACHE;
import static com.example.commonInfrastructure.StringConstants.UNDERLING_CACHE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.Entity.Employee;
import com.example.Repository.Repository;
import com.example.Service.Service;
import com.example.commonInfrastructure.ApiResponse;
import com.example.commonInfrastructure.ResponseEmployee;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

  @Autowired
  private Repository repository;

  @Autowired
  private CacheManager cacheManager;

  @Override
  @Caching(evict = {@CacheEvict(SUPERIOR_CACHE), @CacheEvict(UNDERLING_CACHE)})
  @Transactional
  public ApiResponse save(Employee employee) {
    try {
      ResponseEmployee responseEmployee;
      if (employee.getName() == null || employee.getName().isEmpty())
        throw new Exception("empty name");
      if (employee.getDesignation() == null || employee.getDesignation().isEmpty())
        throw new Exception("empty designation");
      if (employee.getManagerId() == 0) {
        responseEmployee = new ResponseEmployee(employee.getName(), employee.getDesignation(), "none");
      } else {
        employee.setManager(repository.findById(employee.getManagerId()).get());
        employee.getManager().getPeopleUnderMe().add(employee);
        responseEmployee =
            new ResponseEmployee(employee.getName(), employee.getDesignation(), employee.getManager().getName());
      }
      repository.save(employee);
      return new ApiResponse<ResponseEmployee>(true, null, responseEmployee);
    } catch (Exception e) {
      log.error("invalid user save attempt {}", employee.getId());
      return new ApiResponse<ResponseEmployee>(false, e.getMessage(), null);
    }
  }

  @Override
  @Caching(evict = {@CacheEvict(SUPERIOR_CACHE), @CacheEvict(UNDERLING_CACHE)})
  @Transactional
  public ApiResponse editEmployee(Employee employee) {
    try {
      if (employee.getName() == null || employee.getName().isEmpty())
        throw new Exception("empty name");
      if (employee.getDesignation() == null || employee.getDesignation().isEmpty())
        throw new Exception("empty designation");
      Employee oldEmployee = repository.findById(employee.getId()).get();
      Employee oldManager = oldEmployee.getManager();
      Employee newManager;
      if (employee.getManagerId() != 0) {
        newManager = repository.findById(employee.getManagerId()).get();
      } else {
        newManager = null;
      }
      if (!Objects.isNull(newManager) && oldManager.getId() != newManager.getId()) {
        oldManager.getPeopleUnderMe().remove(oldEmployee);
        employee.setManager(newManager);
        employee.setPeopleUnderMe(oldEmployee.getPeopleUnderMe());
        newManager.getPeopleUnderMe().add(employee);
        smartCacheClearing(true, true, false, true, true, employee);
      } else {
        employee.setManager(newManager);
        employee.setPeopleUnderMe(oldEmployee.getPeopleUnderMe());
        //        smartCacheClearing(true, true, false, false, false, employee);
      }
      repository.save(employee);
      return new ApiResponse<String>(true, null, "Update successful");
    } catch (Exception e) {
      log.error("invalid user save attempt {}", employee.getId());
      return new ApiResponse<String>(false, e.getMessage(), null);
    }
  }

  private void smartCacheClearing(boolean idUnder, boolean idAbove, boolean managerUnder, boolean managerAbove,
      boolean useOldManager, Employee employee) {
    //    int managerId = employee.getManagerId();
    //    try {
    //      if (useOldManager) {
    //        managerId = repository.findById(employee.getId()).get().getManagerId();
    //      }
    //      if (idAbove) {
    //        for (Employee e : findAllSuperiors(employee.getId()).getResult()) {
    //          cacheManager.getCache(UNDERLING_CACHE).evict(e.getId());
    //        }
    //      }
    //      if (idUnder) {
    //        for (Employee e : findAllUnderlings(employee.getId()).getResult()) {
    //          cacheManager.getCache(SUPERIOR_CACHE).evict(e.getId());
    //        }
    //      }
    //      if (managerAbove) {
    //        for (Employee e : findAllSuperiors(managerId).getResult()) {
    //          cacheManager.getCache(UNDERLING_CACHE).evict(e.getId());
    //        }
    //      }
    //      if (managerUnder) {
    //        for (Employee e : findAllUnderlings(managerId).getResult()) {
    //          cacheManager.getCache(SUPERIOR_CACHE).evict(e.getId());
    //        }
    //      }
    //    } catch (Exception e) {
    //
    //    }
  }

  //  @Override
  //  public List<Employee> findAll() {
  //    List<Employee> employeeList = new ArrayList<>();
  //    for (Employee e : repository.findAll()) {
  //      e.setManager(null);
  //      e.setPeopleUnderMe(null);
  //      employeeList.add(e);
  //    }
  //    return employeeList;
  //  }

  @Override
  public ApiResponse findById(int empId) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      List<ResponseEmployee> employeeList = new ArrayList<>();
      Employee employee = repository.findById(empId).get();
      if (Objects.isNull(employee.getManager())) {
        return new ApiResponse<ResponseEmployee>(true, null,
            new ResponseEmployee(employee.getName(), employee.getDesignation(), null));
      }
      return new ApiResponse<ResponseEmployee>(true, null,
          new ResponseEmployee(employee.getName(), employee.getDesignation(), employee.getManager().getName()));
    } catch (Exception e) {
      log.error("invalid user find attempt {}", empId);
      return new ApiResponse<ResponseEmployee>(false, e.getMessage(), null);
    }
  }

  @Override
  public ApiResponse findUnderlings(int empId) {
    try {
      Employee employee = repository.findById(empId).get();
      List<ResponseEmployee> responseEmployeeList = new ArrayList<>();
      for (Employee e : employee.getPeopleUnderMe()) {
        ResponseEmployee responseEmployee = new ResponseEmployee();
        responseEmployee.setDesignation(e.getDesignation());
        responseEmployee.setName(e.getName());
        responseEmployee.setManagerName(e.getManager().getName());
        responseEmployeeList.add(responseEmployee);
      }
      return new ApiResponse<List<ResponseEmployee>>(true, null, responseEmployeeList);
    } catch (Exception e) {
      log.error("invalid user underlings find attempt {}", empId);
      return new ApiResponse<String>(false, e.getMessage(), null);
    }
  }

  @Override
  public ApiResponse findSuperiors(int empId) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      Employee employee = repository.findById(empId).get();
      if (Objects.isNull(employee.getManager())) {
        return new ApiResponse<ResponseEmployee>(true, null, null);
      }
      return new ApiResponse<Employee>(true, null, employee.getManager());
    } catch (Exception e) {
      log.error("invalid user superiors find attempt {}", empId);
      return new ApiResponse<String>(false, e.getMessage(), null);
    }
  }

  @Override
  @Cacheable(value = UNDERLING_CACHE,key = "#empId")
  public ApiResponse<List<ResponseEmployee>> findAllUnderlings(int empId) {
    List<ResponseEmployee> responseEmployeeList = new ArrayList<>();
    try {
      responseEmployeeList = makeUnderlingList(repository.findById(empId).get(), responseEmployeeList);
      return new ApiResponse<List<ResponseEmployee>>(true, null, responseEmployeeList);
    } catch (Exception e) {
      return new ApiResponse<List<ResponseEmployee>>(false, e.getMessage(), null);
    }
  }

  private List<ResponseEmployee> makeUnderlingList(Employee employee, List<ResponseEmployee> responseEmployees) {
    if (employee.getPeopleUnderMe().size() == 0)
      return null;
    for (Employee e : employee.getPeopleUnderMe()) {
      ResponseEmployee responseEmployee =
          new ResponseEmployee(e.getName(), e.getDesignation(), e.getManager().getName());
      responseEmployees.add(responseEmployee);
      makeUnderlingList(e, responseEmployees);
    }
    return responseEmployees;
  }

  @Override
  @Cacheable(value = SUPERIOR_CACHE,key = "#empId")
  public ApiResponse findAllSuperiors(int empId) {
    try {
      List<ResponseEmployee> employeeList = new ArrayList<>();
      Employee employee = repository.findById(empId).get();
      while (!Objects.isNull(employee)) {
        ResponseEmployee responseEmployee = new ResponseEmployee();
        responseEmployee.setName(employee.getName());
        responseEmployee.setDesignation(employee.getDesignation());
        if (Objects.isNull(employee.getManager())) {
          responseEmployee.setManagerName("none");
        } else {
          responseEmployee.setManagerName(employee.getManager().getName());
        }
        employee = employee.getManager();
        employeeList.add(responseEmployee);
      }
      return new ApiResponse<List<ResponseEmployee>>(true, null, employeeList);
    } catch (Exception e) {
      log.error("invalid user findAllSuperiors attempt {}", empId);
      return new ApiResponse<ResponseEmployee>(false, e.getMessage(), null);
    }
  }

  @Override
  @Transactional
  @Caching(evict = {@CacheEvict(SUPERIOR_CACHE), @CacheEvict(UNDERLING_CACHE)})
  public ApiResponse deleteById(int empId, int newManagerId) {
    try {
      Employee toDelete = repository.findById(empId).get();
      Employee newManager = null;
      if (newManagerId != 0) {
        newManager = repository.findById(newManagerId).get();
      }
      Employee oldManager = toDelete.getManager();
      if (!Objects.isNull(oldManager)) {
        oldManager.getPeopleUnderMe().remove(toDelete);
      }
      if (!Objects.isNull(newManager)) {
        for (Employee e : toDelete.getPeopleUnderMe()) {
          e.setManager(newManager);
          e.setManagerId(newManagerId);
          newManager.getPeopleUnderMe().add(e);
        }
      } else {
        for (Employee e : toDelete.getPeopleUnderMe()) {
          e.setManager(newManager);
          e.setManagerId(0);
        }
      }
      toDelete.setPeopleUnderMe(null);
      toDelete.setManager(null);
      repository.deleteById(empId);
      return new ApiResponse<String>(true, null, "employee delete successful");
    } catch (Exception e) {
      log.error("invalid user delete attempt {}", empId);
      return new ApiResponse<String>(false, e.getMessage(), null);
    }
  }

  @Override
  public Page<Employee> findAllPageable(int pageNo) {
    Pageable pageable = PageRequest.of(pageNo, 5);
    return repository.findAll(pageable);
  }
}

