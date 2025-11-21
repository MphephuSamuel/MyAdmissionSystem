/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;

/**
 *
 * @author mphep
 */
public class ApplicantRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveApplicant(Applicant applicant){
        entityManager.persist(applicant);
    }
    
    public List<Applicant> getAllApplicants(){
        return entityManager.createQuery("SELECT a from Applicant a").getResultList();
    }
    
    public Applicant findByIdNumber(String idNumber){
        
        try{
             return entityManager.createQuery("SELECT a FROM Applicant a WHERE a.idNumber=:idNumber", Applicant.class)
                .setParameter("idNumber", idNumber)
                .getSingleResult();
        } catch(Exception e){
            return null;
        }
       
    }
    
    public Applicant findByStudentNumber(String studentNumber){
        return entityManager.createQuery("SELECT a FROM Applicant a WHERE a.studentNumber=:studentNumber", Applicant.class)
                .setParameter("studentNumber", studentNumber)
                .getSingleResult();
    }
    
    public Applicant findByApplicantNumber(Long applicantId){
        try{
            return entityManager.find(Applicant.class, applicantId);
        } catch(Exception e){
            return null;
        }
        
    }
}
