/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.ProgrammeLeader;

/**
 *
 * @author mphep
 */
@Stateless
public class ProgrammeLeaderRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveProgrammeLeader(ProgrammeLeader programmeLeader){
        entityManager.persist(programmeLeader);
    }
    
    public ProgrammeLeader getProgrammeLeaderById(Long programmeLeaderId){
        return entityManager.find(ProgrammeLeader.class, programmeLeaderId);
    }
    
    public List<ProgrammeLeader> getAllProgrammeLeaders(){
        return entityManager.createQuery("SELECT p from ProgrammeLeader p", ProgrammeLeader.class)
                .getResultList();
    }
    
    public ProgrammeLeader getProgrammeLeaderUsername(String username){
        System.out.println("username in the repo is " + username);
        return entityManager.createQuery("SELECT p from ProgrammeLeader p WHERE p.username =:username", ProgrammeLeader.class)
                .setParameter("username", username).getSingleResult();         
    }
    
    
}
