/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.Date;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.repository.AdminRepository;
import za.ump.scms.bict.myadmissionsystem.repository.ApplicantRepository;

/**
 *
 * @author mphep
 */
@Stateless
public class ApplicantService {
    
    @Inject
    private ApplicantRepository applicantRepository;
    
    
    
    public void saveApplicant(String idNumber, String studentNumber, String pin, String title, Date dateOfBirth, 
            String firstName, String middleName, String lastName, String gender, String phoneNumber, 
            String alternativePhoneNumber, String disability, String homeAddress, String postalAddress, 
            String homeLanguage, String alternativeLanguage, String marriageStatus, String email, String school){
        
        Applicant applicant = new Applicant(idNumber, studentNumber, pin, title, dateOfBirth, firstName, 
                    middleName, lastName, gender,phoneNumber, alternativePhoneNumber, disability, homeAddress, 
                    postalAddress, homeLanguage, alternativeLanguage, marriageStatus, email, school);
        
        applicantRepository.saveApplicant(applicant);
        applicantRepository.saveApplicant(applicant);
    }
    
    public List<Applicant> getAllApplicants(){
        return applicantRepository.getAllApplicants();
    }
    
    public Applicant getApplicantByIdNumber(String idNumber){
        return applicantRepository.findByIdNumber(idNumber);
    }
    
    public Applicant getApplicantByStudentNumber(String studentNumber){
        return applicantRepository.findByStudentNumber(studentNumber);
    }
    
    public Applicant getApplicantByApplicantId(Long applicantId){
        return applicantRepository.findByApplicantNumber(applicantId);
    }
    
    public boolean isApplicantExistsById(String idNumber) {
        return applicantRepository.findByIdNumber(idNumber) != null;
    }
}
