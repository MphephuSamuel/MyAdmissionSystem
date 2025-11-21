/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import za.ump.scms.bict.myadmissionsystem.model.Users;

/**
 *
 * @author mphep
 */
public class UserRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveUser(Users user){
        entityManager.persist(user);
    }
    
    public Users findByUsername(String username){
        try {
            return entityManager.createQuery("SELECT a FROM Users a WHERE a.username=:username", Users.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
