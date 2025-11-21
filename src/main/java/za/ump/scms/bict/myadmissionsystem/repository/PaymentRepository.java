/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import za.ump.scms.bict.myadmissionsystem.model.Payment;

/**
 *
 * @author mphep
 */
public class PaymentRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void savePayment(Payment payment){
        entityManager.persist(payment);
    }
}
