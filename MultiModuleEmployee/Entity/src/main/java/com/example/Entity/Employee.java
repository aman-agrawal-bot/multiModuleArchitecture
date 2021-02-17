package com.example.Entity;

import static com.example.Entity.TableNames.TABLE_NAME_EMPLOYEE;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TABLE_NAME_EMPLOYEE)
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;
  @Column(name = "designation")
  private String designation;
  @Column(name = "manager_id_my")
  private int managerId;
  @JsonIgnore
  @JsonBackReference
//  @ManyToOne(cascade = CascadeType.ALL,targetEntity = Employee.class)
  @ManyToOne(cascade = CascadeType.ALL,targetEntity = Employee.class)
  @JoinColumn(name="manager_id")
  private Employee manager;
  @JsonManagedReference
  @JsonIgnore
  @OneToMany(mappedBy = "manager",cascade = CascadeType.ALL)
  private List<Employee> peopleUnderMe;
}
/*

public void setStudentName(String studentName) {
this.studentName = studentName;
}
@OneToMany(cascade = CascadeType.ALL)
@JoinTable(name = "STUDENT_PHONE", joinColumns = { @JoinColumn(name = "STUDENT_ID") }, inverseJoinColumns = { @JoinColumn(name = "PHONE_ID") })
public Set<Phone> getStudentPhoneNumbers() {
return this.studentPhoneNumbers;

}
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TABLE_NAME_EMPLOYEE)
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;
  @Column(name = "designation")
  private String designation;
  @ManyToOne(cascade = CascadeType.ALL)
  private Employee managerId;
}

*/

