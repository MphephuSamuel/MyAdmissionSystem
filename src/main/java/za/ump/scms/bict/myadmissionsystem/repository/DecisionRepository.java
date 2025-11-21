/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import za.ump.scms.bict.myadmissionsystem.model.Decision;

/**
 *
 * @author mphep
 */
@Stateless
public class DecisionRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveDecision(Decision decision){
        entityManager.persist(decision);
    }
}
