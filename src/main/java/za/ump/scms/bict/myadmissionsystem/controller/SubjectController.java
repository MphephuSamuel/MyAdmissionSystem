/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Subject;
import za.ump.scms.bict.myadmissionsystem.service.SubjectService;

/**
 *
 * @author mphep
 */
@Named
@RequestScoped
public class SubjectController {
    
    private String subjectName;
    
    @Inject
    private SubjectService subjectService;
    
    public String saveSubject() {
    try {
        // Check if subject name is empty or blank
        if (subjectName == null || subjectName.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Subject name cannot be empty.", null));
            return null;
        }
        
        // Check if subject already exists (case-insensitive match recommended)
        Subject existing = subjectService.findByName(subjectName.trim());
        if (existing != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Subject already exists.", null));
            return null;
        }
        
        // Save new subject
        Subject subject = new Subject(subjectName.trim());
        subjectService.saveSubject(subject);
        subjectName = "";
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Subject saved successfully.", null));
        return null;
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred: " + e.getMessage(), null));
        return null;
    }
}
    
    public List<Subject> getAllSubjects(){
        try{
            return subjectService.getAllSubjects();
        } catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }     
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    
}
