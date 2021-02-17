package com.example.commonInfrastructure;

import java.util.List;

import com.example.Entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class ApiResponse <T>{
  private boolean success;
  private String error;
  private T result;
}
