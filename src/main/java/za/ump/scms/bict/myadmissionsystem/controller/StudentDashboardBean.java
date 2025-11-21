/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.model.Application;
import za.ump.scms.bict.myadmissionsystem.service.ApplicantService;
import za.ump.scms.bict.myadmissionsystem.service.ApplicationService;

/**
 *
 * @author mphep
 */
@Named
@SessionScoped
public class StudentDashboardBean implements Serializable{
    
    private List<Application> applications;
    
    private Applicant applicant;
    
    @Inject
    private ApplicationService applicationService;

    @Inject
    private ApplicantService applicantService;
    
    @Inject
    private UserBean userBean;
    
    @PostConstruct
    public void init(){
        applicant = applicantService.getApplicantByStudentNumber(userBean.getUsername());
        if(applicant!=null){
            applications = applicationService.getApplicationsForApplicant(applicant.getId());
        }
    }
    
    public List<Application> getApplications() {
        return applications;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
    
    
}
