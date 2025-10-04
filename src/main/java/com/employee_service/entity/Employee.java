package com.employee_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "paternal_last_name", nullable = false)
    private String paternalLastName;

    @Column(name = "maternal_last_name")
    private String maternalLastName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String gender;

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(nullable = false)
    private String position;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive;

    // CONSTRUCTORES
    public Employee() {}

    public Employee(Long id, String firstName, String secondName, String paternalLastName,
                    String maternalLastName, Integer age, String gender, Date birthDate,
                    String position, LocalDateTime createdAt, Boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.paternalLastName = paternalLastName;
        this.maternalLastName = maternalLastName;
        this.age = age;
        this.gender = gender;
        this.birthDate = birthDate;
        this.position = position;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

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

    // MÉTODO BUILDER MANUAL
    public static EmployeeBuilder builder() {
        return new EmployeeBuilder();
    }

    // CLASE BUILDER
    public static class EmployeeBuilder {
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

        public EmployeeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EmployeeBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public EmployeeBuilder secondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public EmployeeBuilder paternalLastName(String paternalLastName) {
            this.paternalLastName = paternalLastName;
            return this;
        }

        public EmployeeBuilder maternalLastName(String maternalLastName) {
            this.maternalLastName = maternalLastName;
            return this;
        }

        public EmployeeBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public EmployeeBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public EmployeeBuilder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public EmployeeBuilder position(String position) {
            this.position = position;
            return this;
        }

        public EmployeeBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EmployeeBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Employee build() {
            return new Employee(id, firstName, secondName, paternalLastName, maternalLastName,
                    age, gender, birthDate, position, createdAt, isActive);
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }
}