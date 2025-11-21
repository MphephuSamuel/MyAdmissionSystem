/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import za.ump.scms.bict.myadmissionsystem.model.Admin;

/**
 *
 * @author mphep
 */
public class AdminRepository {
    
    @PersistenceContext(unitName = "MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    @Transactional
    public void saveAdmin(Admin admin){
        entityManager.persist(admin);
    }
}
