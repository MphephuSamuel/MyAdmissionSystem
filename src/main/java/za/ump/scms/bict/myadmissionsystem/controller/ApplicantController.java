/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Date;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.service.ApplicantService;

/**
 *
 * @author mphep
 */
@SessionScoped
@Named
public class ApplicantController implements Serializable{
    
    @Inject
    private ApplicantService applicantService;

    private String idNumber;
    private String studentNumber;
    private String pin;
    private String title;
    private Date dateOfBirth;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String phoneNumber;
    private String alternativePhoneNumber;
    private String disability;
    private String homeAddress;
    private String postalAddress;
    private String homeLanguage;
    private String alternativeLanguage;  
    private String marriageStatus;
    private String email;
    private String school;
    
    public String saveApplicant(){
        try{
            applicantService.saveApplicant(idNumber, studentNumber, pin, title, dateOfBirth, firstName, middleName,
                    lastName, gender, phoneNumber, alternativePhoneNumber, disability, homeAddress, postalAddress, 
                    homeLanguage, alternativeLanguage, marriageStatus, email, school);
            return null;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }


public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAlternativePhoneNumber() {
        return alternativePhoneNumber;
    }

    public void setAlternativePhoneNumber(String alternativePhoneNumber) {
        this.alternativePhoneNumber = alternativePhoneNumber;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getHomeLanguage() {
        return homeLanguage;
    }

    public void setHomeLanguage(String homeLanguage) {
        this.homeLanguage = homeLanguage;
    }

    public String getAlternativeLanguage() {
        return alternativeLanguage;
    }

    public void setAlternativeLanguage(String alternativeLanguage) {
        this.alternativeLanguage = alternativeLanguage;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
    
    
    
    
}
