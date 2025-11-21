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
import java.util.ArrayList;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.dto.ApplicationAdditionDTO;
import za.ump.scms.bict.myadmissionsystem.model.Application;
import za.ump.scms.bict.myadmissionsystem.model.Programme;
import za.ump.scms.bict.myadmissionsystem.service.ApplicationService;
import za.ump.scms.bict.myadmissionsystem.service.ProgrammeService;

/**
 *
 * @author mphep
 */
@Named
@SessionScoped
public class ApplicationController implements Serializable{
    private Long applicantId = 2L;
    private int choiceNumber=1;
    private Long programmeId;
    private List<ApplicationAdditionDTO> applications = new ArrayList<>();
    private List<Programme> programmes = new ArrayList<>();
    private List<Application> chosenApplications = new ArrayList<>();
    
    
    @Inject
    private ApplicationService applicationService;
    
    @Inject
    private ProgrammeService programmeService;
    
    @PostConstruct
    public void retrieveProgrammesFromDatabase(){
        programmes = programmeService.getAllProgrammes();
    }
    
    public String selectProgrammeChoice(){
        if (choiceNumber<4){
            applications.add(new ApplicationAdditionDTO(choiceNumber, programmeId));
            choiceNumber++;
        }

        return null;
    }
    
    public String saveProgrammeChoices(){
        List<Integer> choices = new ArrayList<>();
        List<Long> programmeIds = new ArrayList<>();
        for(ApplicationAdditionDTO application : applications){
            choices.add(application.getChoiceNumber());
            programmeIds.add(application.getSubjectId());
        }
        applicationService.saveAppllications(applicantId, programmeIds, choices);
        resetChoices();
        return null; 
    }
    
    public void resetChoices(){
        choiceNumber=0;
        applications.clear();
        
    }

    public int getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(int choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public Long getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(Long programmeId) {
        this.programmeId = programmeId;
    }

    public List<Programme> getProgrammes() {
        return programmes;
    }

    public void setProgrammes(List<Programme> programmes) {
        this.programmes = programmes;
    }

    public List<Application> getChosenApplications() {
        return applicationService.getApplicationsForApplicant(applicantId);
    }

    public void setChosenApplications(List<Application> chosenApplications) {
        this.chosenApplications = chosenApplications;
    }
    
    
    
    
}
