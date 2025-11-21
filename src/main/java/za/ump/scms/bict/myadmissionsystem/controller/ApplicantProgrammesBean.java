/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import za.ump.scms.bict.myadmissionsystem.dto.ApplicationAdditionDTO;
import za.ump.scms.bict.myadmissionsystem.model.Programme;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;
import za.ump.scms.bict.myadmissionsystem.service.ProgrammeService;
import za.ump.scms.bict.myadmissionsystem.service.TemporalApplicantService;

/**
 *
 * @author mphep
 */
@SessionScoped
@Named
public class ApplicantProgrammesBean implements Serializable{
    
    private List<Programme> programmes = new ArrayList<>();
    private int choiceNumber=1;
    private Long programmeId;
    private List<ApplicationAdditionDTO> applications = new ArrayList<>();
     
     @Inject
    private TemporalApplicantService temporalApplicantService;

    @Inject
    private TemporalApplicantBean temporalApplicantBean;
    
    @Inject
    private ProgrammeService programmeService;
    
    
    
    @PostConstruct
    public void retrieveProgrammesFromDatabase(){
        programmes = programmeService.getAllProgrammes();
        loadApplications();
    }
    
    public String selectProgrammeChoice() {
    if (programmeId == null) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, 
                             "No Programme Selected", 
                             "Please select a programme before adding."));
        return null;
    }

    // Check for duplicates
    for (ApplicationAdditionDTO application : applications) {
        if (application.getSubjectId().equals(programmeId)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                                 "Duplicate Programme",
                                 "You have already selected this programme."));
            return null;
        }
    }

    if (choiceNumber < 4) {
        System.out.println("now adding this application " + choiceNumber + " " + programmeId);
        applications.add(new ApplicationAdditionDTO(choiceNumber, programmeId));
        choiceNumber++;
    } else {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, 
                             "Limit Reached", 
                             "You can only select up to 3 programme choices."));
    }
    return null;
}

    
    public String saveApplications() {
        String idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);

        JSONObject json = applicant != null
                ? new JSONObject(applicant.getApplicationData())
                : new JSONObject();

        JSONArray appArray = new JSONArray();

        for (ApplicationAdditionDTO entry : applications) {
            JSONObject application = new JSONObject();
            application.put("choiceNumber", entry.getChoiceNumber());
            application.put("programmeId", entry.getSubjectId());
            appArray.put(application);
        }

        json.put("applications", appArray);
        String applicationData = json.toString();

        temporalApplicantService.saveTemporalApplicant(idNumber, applicationData);
        System.out.println("Applications saved successfully");

        return "uploadDocuments.xhtml?faces-redirect=true";
    }
    
    public String getSubjectName(Long id) {
        
        Programme programme = programmeService.getProgrammeById(id);
        
        return programme != null ? programme.getName() : "Unknown";
    }
    
    public void loadApplications() {
        String idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);

        if (applicant != null) {
            JSONObject json = new JSONObject(applicant.getApplicationData());
            if (json.has("applications")) {
                applications.clear(); // Clear any existing to avoid duplication
                for (Object obj : json.getJSONArray("applications")) {
                    JSONObject app = (JSONObject) obj;
                    ApplicationAdditionDTO entry = new ApplicationAdditionDTO(
                            app.getInt("choiceNumber"),
                            app.getLong("programmeId")
                    );
                    applications.add(entry);
                }
                // Set the choiceNumber to the next value
                choiceNumber = applications.size() + 1;
            }
        }
    }


    public List<Programme> getProgrammes() {
        return programmes;
    }

    public void setProgrammes(List<Programme> programmes) {
        this.programmes = programmes;
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

    public List<ApplicationAdditionDTO> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationAdditionDTO> applications) {
        this.applications = applications;
    }
    
    
}



    