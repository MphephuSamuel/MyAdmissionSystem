/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Mark;

/**
 *
 * @author mphep
 */
public class MarkRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveMark(Mark mark){
        entityManager.persist(mark);
    }
    
    
    public List<Mark> getAllMarks(){
        return entityManager.createQuery("SELECT m Mark m", Mark.class)
                .getResultList();
    }
    
    
    public List<Mark> getMarksForStudent(Long applicantId) {
        System.out.println("The repo reached with " +applicantId);
        return entityManager.createQuery(
                "SELECT m FROM Mark m WHERE m.applicant.id = :applicantId", Mark.class)
                .setParameter("applicantId", applicantId)
                .getResultList();
    }  
}
    