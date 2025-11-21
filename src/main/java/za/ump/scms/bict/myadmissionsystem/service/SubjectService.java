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
import za.ump.scms.bict.myadmissionsystem.model.Subject;
import za.ump.scms.bict.myadmissionsystem.repository.SubjectRepository;

/**
 *
 * @author mphep
 */
@Stateless
public class SubjectService implements Serializable{
    
    @Inject
    private SubjectRepository subjectRepository;
    
    @Transactional
    public void saveSubject(Subject subject){
        subjectRepository.saveSubject(subject);
    }
    
    @Transactional
    public List<Subject> getAllSubjects(){
        return subjectRepository.getAllSubjects();
    }
    
    @Transactional
    public Subject getSubjectById(Long subjectId){
        return subjectRepository.findBySubjectId(subjectId);
    }
    
    public Subject findByName(String name){
        return subjectRepository.findByName(name);
    }
}
