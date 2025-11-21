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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import za.ump.scms.bict.myadmissionsystem.model.Mark;
import za.ump.scms.bict.myadmissionsystem.model.Subject;
import za.ump.scms.bict.myadmissionsystem.service.MarkService;
import za.ump.scms.bict.myadmissionsystem.service.SubjectService;

/**
 *
 * @author mphep
 */
@Named
@SessionScoped
public class MarkController implements Serializable{
    
    @Inject
    private MarkService markService;
    
    @Inject
    private SubjectService subjectService;
    
    private Long applicantId=1L;
    private List<Long> subjectIds = new ArrayList<>();
    private List<Integer> gradeLevels = new ArrayList<>();
    private List<Integer> percentages = new ArrayList<>();
    private List<Integer> levels= new ArrayList<>();
    private List<Integer> examYears = new ArrayList<>();
    private List<Mark> applicantMarks= new ArrayList<>();
    
    private int gradeLevel;
    private int percentage;
    private int level;
    private int examYear;
    private Long subjectId;

    public void addMark(){
        
        System.out.println(applicantId + " " + subjectId +  " "  + gradeLevel + " " + percentage + " "  + level + " " + examYear);
        
        subjectIds.add(subjectId);
        gradeLevels.add(gradeLevel);
        percentages.add(percentage);
        levels.add(level);
        examYears.add(examYear);
        
        gradeLevel =0 ;
        percentage=0;
        level=0;
        examYear=0;
        subjectId=0L;
        
         gradeLevel =0 ;
        percentage=0;
        level=0;
        examYear=0;
        subjectId=0L;   
    }
    
    public String saveMarks(){
        try{
            markService.saveMarks(applicantId, subjectIds, gradeLevels,  levels, percentages, examYears);
            return null;
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
        subjectIds.clear();
        gradeLevels.clear();
        percentages.clear();
        levels.clear();
        examYears.clear();
        
        retrieveApplicantMarksFromDatabase();
        return null;
    }
    
    public List<Map<String, Object>> getSelectedMarks() {
        List<Map<String, Object>> selectedMarks = new ArrayList<>();
        for (int i = 0; i < subjectIds.size(); i++) {
            Map<String, Object> markDetails = new HashMap<>();
            Subject subject = subjectService.getSubjectById(subjectIds.get(i));
            markDetails.put("subjectName", subject.getName());
            markDetails.put("gradeLevel", gradeLevels.get(i));
            markDetails.put("percentage", percentages.get(i));
            markDetails.put("level", levels.get(i));
            markDetails.put("examYear", examYears.get(i));
            selectedMarks.add(markDetails);
        }
        return selectedMarks;
    }
    
    
    public void retrieveApplicantMarksFromDatabase(){
        applicantMarks=markService.getMarksByApplicantId(applicantId);
    }
    
    public List<Mark> getApplicantMarks(){
        retrieveApplicantMarksFromDatabase();
        System.out.println("Applicant Number in the controller " + applicantId);
        try{
            return applicantMarks;
        } catch(Exception e){
            System.out.print(e.getMessage());
            return null;
        }
        
    }
    
    public double getApplicantAPS(){
        double aps=0;
        for(Mark mark : applicantMarks){
            if(mark.getSubject().getId()==1){
                aps=aps+mark.getLevel()/2;
            }
            else{
                aps=aps+mark.getLevel();
            }
        }
        return aps;
    }

    
    public List<Subject> getAllSubjects(){
        return subjectService.getAllSubjects();
    }
    
    
    public List<Mark> getAllSubjectsByApplicantId(){
        return markService.getMarksByApplicantId(applicantId);
    }
    
    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public List<Long> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<Long> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public List<Integer> getGradeLevels() {
        return gradeLevels;
    }

    public void setGradeLevels(List<Integer> gradeLevels) {
        this.gradeLevels = gradeLevels;
    }

    public List<Integer> getPercentages() {
        return percentages;
    }

    public void setPercentages(List<Integer> percentages) {
        this.percentages = percentages;
    }

    public List<Integer> getLevels() {
        return levels;
    }

    public void setLevels(List<Integer> levels) {
        this.levels = levels;
    }

    public List<Integer> getExamYears() {
        return examYears;
    }

    public void setExamYears(List<Integer> examYears) {
        this.examYears = examYears;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExamYear() {
        return examYear;
    }

    public void setExamYear(int examYear) {
        this.examYear = examYear;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
   
}
