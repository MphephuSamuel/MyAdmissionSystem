/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.model.Application;
import za.ump.scms.bict.myadmissionsystem.model.Programme;
import za.ump.scms.bict.myadmissionsystem.repository.ApplicationRepository;

/**
 *
 * @author mphep
 */

public class ApplicationService implements Serializable{
    
    @Inject
    private ApplicationRepository applicationRepository;
    
    @Inject
    private ApplicantService applicantService;
    
    @Inject
    private ProgrammeService programmeService;
    
    @Transactional
    public void saveApplication(Long applicantId, Long programmeId, int choiceNumber){
        Applicant applicant  = applicantService.getApplicantByApplicantId(applicantId);
        Programme programme = programmeService.getProgrammeById(programmeId);
        Date date=new Date();
        String applicationStatus="Pending";
        Application application = new Application(applicant, programme, choiceNumber, date, applicationStatus);
        applicationRepository.saveApplication(application);
    }
    
    @Transactional
    public void saveAppllications(Long applicantId, List<Long> programmeIds, List<Integer> choiceNumbers){
        for(int i = 0;i<programmeIds.size();i++){
            saveApplication(applicantId, programmeIds.get(i),choiceNumbers.get(i));
        }
    }
    
    @Transactional
    public List<Application> getAllApplications(){
        return applicationRepository.getAllApplications();
    }
    
    @Transactional
    public Application getApplicationById(Long applicationId){
        return applicationRepository.getApplicationById(applicationId);
    }
    
    @Transactional
    public List<Application> getApplicationsForApplicant(Long applicantId){
        return applicationRepository.getApplicationsForApplicant(applicantId);
    }
    
    @Transactional
    public List<Application> getApplicationsByProgrammeId(Long programmeId){
        return applicationRepository.getApplicationsForProgramme(programmeId);
    }
    
    @Transactional
    public void updateStatus(Long applicationId, String applicationStatus){
        Application application = applicationRepository.getApplicationById(applicationId);
        if(application!=null){
            application.setApplicationStatus(applicationStatus);
            applicationRepository.saveApplication(application);
        }
    }
    
    @Transactional
    public Applicant getApplicantByApplicationId(Long applicationId) {
        return applicationRepository.getApplicantByApplicationId(applicationId);
    }
}
