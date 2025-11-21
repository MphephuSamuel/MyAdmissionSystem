package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import za.ump.scms.bict.myadmissionsystem.dto.SubjectRequirementDTO;
import za.ump.scms.bict.myadmissionsystem.model.Programme;
import za.ump.scms.bict.myadmissionsystem.model.RequiredSubject;
import za.ump.scms.bict.myadmissionsystem.model.Subject;
import za.ump.scms.bict.myadmissionsystem.service.ProgrammeService;
import za.ump.scms.bict.myadmissionsystem.service.SubjectService;

@Named
@SessionScoped
public class ProgrammeController implements Serializable{
    
    private Long selectedSubjectId;
    private int selectedSubjectMinLevel;
    private Map<Long, Integer> selectedRequirements = new HashMap<>();
    private final List<SubjectRequirementDTO> requiredSubjectsAndLevels= new ArrayList<>();
    private String name;
    private int minAps;
    private int maxCapacity;
    
    @Inject
    private SubjectService subjectService;
    
    @Inject
    private ProgrammeService programmeService;
    
    @Inject
    private AdminController adminController;
    
    private boolean subjectsAdded = false;
    
    // Add this method to ProgrammeController.java
    public boolean isMaxSubjectsReached() {
        return requiredSubjectsAndLevels.size() >= 4;
    }

public void addSubject() {
    // Check if max subjects already reached
    if (isMaxSubjectsReached()) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Maximum subjects reached", "You can only add up to 4 required subjects."));
        return;
    }
    
    // Check if subject is already added
    if (selectedSubjectId == null || selectedSubjectMinLevel == 0) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Invalid selection", "Please select both a subject and a minimum level."));
        return;
    }
    
    // Check if subject already exists in the list
    for (SubjectRequirementDTO req : requiredSubjectsAndLevels) {
        if (req.getSubjectId().equals(selectedSubjectId)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Duplicate subject", "This subject has already been added to the programme."));
            return;
        }
    }
    
    System.out.println("Selected subject Id : " + selectedSubjectId + ", selectedSubject level " + selectedSubjectMinLevel);
    Subject subject = subjectService.getSubjectById(selectedSubjectId);
    requiredSubjectsAndLevels.add(new SubjectRequirementDTO(subject.getId(), subject.getName(), selectedSubjectMinLevel));
    selectedRequirements.put(selectedSubjectId, selectedSubjectMinLevel);
    selectedSubjectMinLevel = 0;
    
    // Set flag to true after adding first subject
    subjectsAdded = true;
}

public String saveProgramme() {
    Programme existing = programmeService.findProgrammeByName(name);
    if (existing != null) {
        FacesContext context = FacesContext.getCurrentInstance();
    context.getExternalContext().getFlash().setKeepMessages(true); // Keeps message across redirect (optional)
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
            "Programme already exists", "A programme with this name already exists."));
    return null;
    }

    // Create and save new programme
    Programme programme = new Programme(name, minAps, maxCapacity);
    List<RequiredSubject> requiredSubjects = new ArrayList<>();
    for (Map.Entry<Long, Integer> entry : selectedRequirements.entrySet()) {
        Subject subject = subjectService.getSubjectById(entry.getKey());
        requiredSubjects.add(new RequiredSubject(programme, subject, entry.getValue()));
    }

    System.out.println("requirements are " + requiredSubjects.size());
    programme.setRequiredSubjects(requiredSubjects);
    programmeService.saveProgramme(programme);

    // Clear form
    name = "";
    minAps = 0;
    selectedSubjectId = null;
    selectedSubjectMinLevel = 0;
    selectedRequirements.clear();
    requiredSubjectsAndLevels.clear();
    adminController.loadProgrammes();   
    
    return null;
}

    public boolean isSubjectsAdded() {
        return !requiredSubjectsAndLevels.isEmpty(); 
    }
    
    public List<Programme> getAllProgrammes(){
        return programmeService.getAllProgrammes();
    }
    
    public List<Subject> getAllSubjects(){
        return subjectService.getAllSubjects();
    }
    
    public List<SubjectRequirementDTO> getAllSubjectRequirementsAndLevel(){
        return requiredSubjectsAndLevels;
    }

    public Long getSelectedSubjectId() {
        return selectedSubjectId;
    }

    public void setSelectedSubjectId(Long selectedSubjectId) {
        this.selectedSubjectId = selectedSubjectId;
    }

    public int getSelectedSubjectMinLevel() {
        return selectedSubjectMinLevel;
    }

    public void setSelectedSubjectMinLevel(int selectedSubjectMinLevel) {
        this.selectedSubjectMinLevel = selectedSubjectMinLevel;
    }

    public Map<Long, Integer> getSelectedRequirements() {
        return selectedRequirements;
    }

    public void setSelectedRequirements(Map<Long, Integer> selectedRequirements) {
        this.selectedRequirements = selectedRequirements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinAps() {
        return minAps;
    }

    public void setMinAps(int minAps) {
        this.minAps = minAps;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

}
