package com.employee_service.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;

public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String secondName;
    private String paternalLastName;
    private String maternalLastName;
    private Integer age;
    private String gender;
    private Date birthDate;
    private String position;
    private LocalDateTime createdAt;
    private Boolean isActive;

    // GETTERS Y SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSecondName() { return secondName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }

    public String getPaternalLastName() { return paternalLastName; }
    public void setPaternalLastName(String paternalLastName) { this.paternalLastName = paternalLastName; }

    public String getMaternalLastName() { return maternalLastName; }
    public void setMaternalLastName(String maternalLastName) { this.maternalLastName = maternalLastName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getFullName() {
        return String.format("%s %s %s %s",
                firstName,
                secondName != null ? secondName : "",
                paternalLastName,
                maternalLastName != null ? maternalLastName : "").trim();
    }
}