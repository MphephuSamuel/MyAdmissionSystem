/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Subject;

/**
 *
 * @author mphep
 */
public class SubjectRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveSubject(Subject subject){
        entityManager.persist(subject);
    }
    
    public Subject findBySubjectId(Long subjectId){
        return entityManager.find(Subject.class, subjectId);
    }
    
    public List<Subject> getAllSubjects(){
        return entityManager.createQuery("SELECT s FROM Subject s", Subject.class)
                .getResultList();
    }
    
    public Subject findByName(String name) {
        try {
            return entityManager.createQuery(
                    "SELECT s FROM Subject s WHERE LOWER(s.name) = LOWER(:name)", Subject.class)
                .setParameter("name", name)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
}
}
