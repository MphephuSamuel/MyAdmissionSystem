/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.model.Mark;
import za.ump.scms.bict.myadmissionsystem.model.Subject;
import za.ump.scms.bict.myadmissionsystem.repository.MarkRepository;

/**
 *
 * @author mphep
 */
@Stateless
public class MarkService implements Serializable{
    
    @Inject
    private MarkRepository markRepository;
    
    @Inject
    private ApplicantService applicantService;
    
    @Inject
    private SubjectService subjectService;
    
    @Transactional
    public void saveMark(Long subjectId, Long applicantId, int gradeLevel, int percentage, int level, int exampYear){
        System.out.println("The subject Id got here and applicant is " + subjectId + " "  + applicantId);
        Applicant applicant = applicantService.getApplicantByApplicantId(applicantId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Mark mark = new Mark(subject, applicant, gradeLevel, percentage, level, exampYear);
        markRepository.saveMark(mark);
    }
    
    @Transactional
    public void saveMarks(Long applicantId, List<Long> subjectIds, List<Integer> gradeLevels,
            List<Integer> percentages,  List<Integer> levels, List<Integer> examYears){
        
        System.out.println("The size of subjectIds is " +subjectIds.size());
        for(int i=0;i<subjectIds.size();i++){
            saveMark(subjectIds.get(i), applicantId,(int)  gradeLevels.get(i),(int) levels.get(i),(int) percentages.get(i),(int) examYears.get(i));
        }
    }
    
    @Transactional
    public List<Mark> getAllMarks(){
        return markRepository.getAllMarks();
    }
    
    @Transactional
    public double getApplicantAPS(Long applicantId){
        double aps=0;
        for(Mark mark : getMarksByApplicantId(applicantId)){
            if(mark.getSubject().getId()==1){
                aps=aps+mark.getLevel()/2;
            }
            else{
                aps=aps+mark.getLevel();
            }
        }
        return aps;
    }
    
    @Transactional
    public List<Mark> getMarksByApplicantId(Long applicantId){
        System.out.println("applicantId in the service " + applicantId);
        return markRepository.getMarksForStudent(applicantId);
    }
    
}
