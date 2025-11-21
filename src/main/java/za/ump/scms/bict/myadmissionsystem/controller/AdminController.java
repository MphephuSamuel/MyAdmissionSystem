/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Admin;
import za.ump.scms.bict.myadmissionsystem.model.Application;
import za.ump.scms.bict.myadmissionsystem.model.Programme;
import za.ump.scms.bict.myadmissionsystem.model.ProgrammeLeader;
import za.ump.scms.bict.myadmissionsystem.service.AdminService;
import za.ump.scms.bict.myadmissionsystem.service.ApplicationService;
import za.ump.scms.bict.myadmissionsystem.service.ProgrammeLeaderService;
import za.ump.scms.bict.myadmissionsystem.service.ProgrammeService;
import za.ump.scms.bict.myadmissionsystem.service.UserService;

/**
 *
 * @author mphep
 */
@SessionScoped
@Named
public class AdminController implements Serializable{
    private String username;
    private String password;
    
    private String programmeLeaderUsername;
    private String programmeLeaderPassword;
    
    private List<Programme>  programmes = new ArrayList<>();
    private Long selectedProgrammeId;
    
    private List<ProgrammeLeader> programmeLeaders = new ArrayList<>();
    
    private List<Application> applications = new ArrayList<>();
    
    
    @Inject
    private AdminService adminService;
    
    @Inject
    private ProgrammeLeaderService programmeLeaderService;
    
    @Inject
    private ProgrammeService  programmeService;
    
    @Inject
    private UserService userService;
    
    @Inject
    private ApplicationService applicationService;
    
    
    
    public String saveProgrammeLeader() {
        boolean usernameExists = programmeLeaders.stream()
            .anyMatch(pl -> pl.getUsername().equalsIgnoreCase(programmeLeaderUsername));

        boolean programmeAlreadyAssigned = programmeLeaders.stream()
            .anyMatch(pl -> pl.getProgramme() != null &&
                            pl.getProgramme().getId().equals(selectedProgrammeId));

        if (usernameExists) {
            FacesContext.getCurrentInstance().addMessage("programmeLeaderUsername",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username already exists.", "Error"));
            return null;
        }

        if (programmeAlreadyAssigned) {
            FacesContext.getCurrentInstance().addMessage("programmeLeaderUsername",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "This programme already has a leader assigned.", "Error"));
            return null;
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, programmeLeaderPassword.toCharArray());
        programmeLeaderService.saveProgrammeLeader(selectedProgrammeId, programmeLeaderUsername);
        userService.userRegister(programmeLeaderUsername, hashedPassword, "programmeLeader");

        programmeLeaderUsername = "";
        programmeLeaderPassword = "";
        programmeLeaders=null;
        loadProgrammes();

        return null;
    }

    
    @PostConstruct
    public void loadProgrammes(){
        programmeLeaders = programmeLeaderService.getAllProgrammeLeaders();
        programmes = programmeService.getAllProgrammes();
        applications = applicationService.getAllApplications();
    }
    
    public String saveAdmin(){
        try{
            Admin admin = new Admin(username, password);
            adminService.saveAdmin(admin);
            return null;
        } catch(Exception e){
            return "Error";
        }
    }

    public String getProgrammeLeaderUsername() {
        return programmeLeaderUsername;
    }

    public void setProgrammeLeaderUsername(String programmeLeaderUsername) {
        this.programmeLeaderUsername = programmeLeaderUsername;
    }

    public String getProgrammeLeaderPassword() {
        return programmeLeaderPassword;
    }

    public void setProgrammeLeaderPassword(String programmeLeaderPassword) {
        this.programmeLeaderPassword = programmeLeaderPassword;
    }

    public List<Programme> getProgrammes() {
        return programmes;
    }

    public void setProgrammes(List<Programme> programmes) {
        this.programmes = programmes;
    }

    public Long getSelectedProgrammeId() {
        return selectedProgrammeId;
    }

    public void setSelectedProgrammeId(Long selectedProgrammeId) {
        this.selectedProgrammeId = selectedProgrammeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ProgrammeLeader> getProgrammeLeaders() {
        return programmeLeaders;
    }

    public void setProgrammeLeaders(List<ProgrammeLeader> programmeLeaders) {
        this.programmeLeaders = programmeLeaders;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
  
}
