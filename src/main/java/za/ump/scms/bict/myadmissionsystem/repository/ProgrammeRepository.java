/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Programme;

/**
 *
 * @author mphep
 */
public class ProgrammeRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveProgramme(Programme programme){
        entityManager.persist(programme);
    }
    
    public List<Programme> getAllProgrammes(){
        return entityManager.createQuery("SELECT p FROM Programme p", Programme.class)
                .getResultList();
    }
    
    public Programme getProgrammeById(Long programmeId){
        return entityManager.find(Programme.class, programmeId);
    }
    
        public Programme findByName(String name) {
    try {
        return entityManager.createQuery(
                "SELECT p FROM Programme p WHERE LOWER(p.name) = LOWER(:name)", Programme.class)
                .setParameter("name", name)
                .getSingleResult();
    } catch (NoResultException e) {
        return null;
    }
}
}
