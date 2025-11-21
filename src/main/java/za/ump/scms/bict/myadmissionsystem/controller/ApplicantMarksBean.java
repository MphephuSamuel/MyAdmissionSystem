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
import za.ump.scms.bict.myadmissionsystem.service.SubjectService;
import za.ump.scms.bict.myadmissionsystem.model.Subject;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;
import za.ump.scms.bict.myadmissionsystem.service.TemporalApplicantService;

@Named
@SessionScoped
public class ApplicantMarksBean implements Serializable {

    private int gradeLevel;
    private int percentage;
    private int level;
    private int examYear;
    private Long subjectId;
    
    // Add flags to track if first mark has been added
    private boolean firstMarkAdded = false;
    private int lockedExamYear;
    private int lockedGradeLevel;

    private List<Subject> allSubjects;

    @Inject
    private SubjectService subjectService;

    @Inject
    private TemporalApplicantService temporalApplicantService;

    @Inject
    private TemporalApplicantBean temporalApplicantBean;

    private List<MarkEntry> markList = new ArrayList<>();

    @PostConstruct
    public void init() {
        allSubjects = subjectService.getAllSubjects();
    }

    public void loadMarks() {
        String idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);

        if (applicant != null) {
            JSONObject json = new JSONObject(applicant.getApplicationData());
            if (json.has("marks")) {
                markList.clear();
                JSONArray marksArray = json.getJSONArray("marks");
                for (Object obj : marksArray) {
                    JSONObject mark = (JSONObject) obj;
                    if (mark.has("subjectId")) {
                        MarkEntry entry = new MarkEntry(
                            mark.getLong("subjectId"),
                            mark.getInt("examYear"),
                            mark.getInt("finalMark"),
                            mark.getInt("subjectLevel"),
                            mark.getInt("gradeLevel")
                        );
                        markList.add(entry);
                    }
                }
                
                // If marks exist, set the first mark added flag and lock values
                if (!markList.isEmpty()) {
                    firstMarkAdded = true;
                    MarkEntry firstEntry = markList.get(0);
                    lockedExamYear = firstEntry.getExamYear();
                    lockedGradeLevel = firstEntry.getGradeLevel();
                    
                    // Pre-set the form fields with locked values
                    examYear = lockedExamYear;
                    gradeLevel = lockedGradeLevel;
                }
            }
        }
    }

    public String saveMarks() {
        String idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);

        JSONObject json = applicant != null
                ? new JSONObject(applicant.getApplicationData())
                : new JSONObject();

        JSONArray markArray = new JSONArray();

        for (MarkEntry entry : markList) {
            JSONObject mark = new JSONObject();
            mark.put("subjectId", entry.getSubjectId());
            mark.put("examYear", entry.getExamYear());
            mark.put("finalMark", entry.getFinalMark());
            mark.put("subjectLevel", entry.getSubjectLevel());
            mark.put("gradeLevel", entry.getGradeLevel());
            markArray.put(mark);
        }

        json.put("marks", markArray);
        String applicationData = json.toString();

        temporalApplicantService.saveTemporalApplicant(idNumber, applicationData);

        return null;
    }

    public String saveAndContinue() {
        // Save the current marks
        saveMarks();

        // Check if there are 7 or more subjects
        if (markList.size() < 7) {
            // If fewer than 7 subjects, show an error message
            FacesContext.getCurrentInstance().addMessage("form:messages", 
                new FacesMessage(FacesMessage.SEVERITY_ERROR,"You need to add at least 7 subjects to continue.", "Error" )
            );
            return null; // Prevent the navigation
        }
        
        // Proceed to the next page if there are 7 or more subjects
        return "programme-selection.xhtml?faces-redirect=true";
    }

    // Replace the addMark() method with this updated version
public void addMark() {
    // Validate all required fields
    boolean hasErrors = false;
    FacesContext context = FacesContext.getCurrentInstance();
    
    // Validate subject
    if (subjectId == null || subjectId == 0L) {
        context.addMessage("marksForm:messages", 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Subject is required.", null));
        hasErrors = true;
    }

    // Validate exam year
    if (examYear <= 0) {
        context.addMessage("marksForm:messages", 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exam Year is required.", null));
        hasErrors = true;
    }

    // Validate final mark
    if (percentage <= 0) {
        context.addMessage("marksForm:messages", 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Final Mark is required.", null));
        hasErrors = true;
    }

    // Validate subject level
    if (level <= 0) {
        context.addMessage("marksForm:messages", 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Subject Level is required.", null));
        hasErrors = true;
    }

    // Validate grade level
    if (gradeLevel <= 0) {
        context.addMessage("marksForm:messages", 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grade Level is required.", null));
        hasErrors = true;
    }

    // If any validation errors, return without adding the mark
    if (hasErrors) {
        return;
    }

    // Continue with the existing validation logic
    // Validate if the subject already exists in the list
    for (MarkEntry existingMark : markList) {
        if (existingMark.getSubjectId().equals(subjectId)) {
            context.addMessage("marksForm:messages", 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "This subject has already been added.", null));
            return;
        }
    }

    // Validate that the level corresponds to the percentage
    int expectedLevel = calculateLevelFromMark(percentage);
    if (level != expectedLevel) {
        context.addMessage("marksForm:messages",
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Subject Level does not match the Final Mark (%). For " + percentage + "%, Level " + expectedLevel + " is expected.", null));
        return;
    }
    
    // Handle the first mark special case
    if (!firstMarkAdded) {
        // This is the first mark - lock the exam year and grade level
        firstMarkAdded = true;
        lockedExamYear = examYear;
        lockedGradeLevel = gradeLevel;
    }

    // Add the mark entry to the list
    MarkEntry entry = new MarkEntry(subjectId, examYear, percentage, level, gradeLevel);
    markList.add(entry);
    saveMarkToJsonFile(entry);

    // Reset form fields, but keep the locked values
    subjectId = 0L;
    percentage = 0;
    level = 0;
    examYear = lockedExamYear;     // Keep the locked exam year
    gradeLevel = lockedGradeLevel; // Keep the locked grade level

    // Display success message
    context.addMessage("marksForm:messages", 
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Mark added successfully.", null));
}

    // Helper method to map percentage to level
    private int calculateLevelFromMark(int mark) {
        if (mark >= 80) return 7;
        if (mark >= 70) return 6;
        if (mark >= 60) return 5;
        if (mark >= 50) return 4;
        if (mark >= 40) return 3;
        if (mark >= 30) return 2;
        return 1;
    }
    
    public List<Integer> getPercentages() {
        List<Integer> percentages = new ArrayList<>();
        for (int i = 0; i <= 100; i += 1) {
            percentages.add(i);
        }
        return percentages;
    }

    public List<Integer> getLevels() {
        return List.of(1, 2, 3, 4, 5, 6, 7);
    }

    // Ajax listener to auto-assign level when final mark changes
    public void onPercentageChange() {
        if (percentage >= 80) {
            level = 7;
        } else if (percentage >= 70) {
            level = 6;
        } else if (percentage >= 60) {
            level = 5;
        } else if (percentage >= 50) {
            level = 4;
        } else if (percentage >= 40) {
            level = 3;
        } else if (percentage >= 30) {
            level = 2;
        } else {
            level = 1;
        }
    }

    private void saveMarkToJsonFile(MarkEntry newMark) {
        String idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);

        if (applicant != null) {
            JSONObject json = new JSONObject(applicant.getApplicationData());
            JSONArray markArray = json.has("marks") ? json.getJSONArray("marks") : new JSONArray();
            
            // Add the new mark to the JSON array
            JSONObject markJson = new JSONObject();
            markJson.put("subjectId", newMark.getSubjectId());
            markJson.put("examYear", newMark.getExamYear());
            markJson.put("finalMark", newMark.getFinalMark());
            markJson.put("subjectLevel", newMark.getSubjectLevel());
            markJson.put("gradeLevel", newMark.getGradeLevel());
            markArray.put(markJson);
            
            // Update the JSON with the new marks array
            json.put("marks", markArray);

            // Save the updated JSON back to the applicant record
            temporalApplicantService.saveTemporalApplicant(idNumber, json.toString());
        }
    }

    public void removeMark(Long subjectId) {
        // Find the mark entry with the given subjectId and remove it
        markList.removeIf(entry -> entry.getSubjectId().equals(subjectId));

        // After removing the mark, save the updated list
        saveMarks();
        
        // If all marks are removed, reset the first mark added flag
        if (markList.isEmpty()) {
            firstMarkAdded = false;
        }
    }

    public List<Subject> getAllSubjects() {
        return allSubjects != null ? allSubjects : new ArrayList<>();
    }

    public List<MarkEntry> getMarkList() {
        return markList;
    }

    public String getSubjectName(Long id) {
        Subject subject = subjectService.getSubjectById(id);
        return subject != null ? subject.getName() : "Unknown";
    }
    
    // Check if the first mark has been added
    public boolean isFirstMarkAdded() {
        return firstMarkAdded;
    }

    // Inner class for marks
    public static class MarkEntry {
        private Long subjectId;
        private int examYear, finalMark, subjectLevel, gradeLevel;

        public MarkEntry(Long subjectId, int examYear, int finalMark, int subjectLevel, int gradeLevel) {
            this.subjectId = subjectId;
            this.examYear = examYear;
            this.finalMark = finalMark;
            this.subjectLevel = subjectLevel;
            this.gradeLevel = gradeLevel;
        }

        public Long getSubjectId() { return subjectId; }
        public int getExamYear() { return examYear; }
        public int getFinalMark() { return finalMark; }
        public int getSubjectLevel() { return subjectLevel; }
        public int getGradeLevel() { return gradeLevel; }
    }

    // Getters/Setters
    public int getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }
    public int getPercentage() { return percentage; }
    public void setPercentage(int percentage) { this.percentage = percentage; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getExamYear() { return examYear; }
    public void setExamYear(int examYear) { this.examYear = examYear; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
}
