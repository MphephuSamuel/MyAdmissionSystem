/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;

/**
 *
 * @author mphep
 */
@Stateless
public class TemporalApplicantRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveTemporalApplicant(TemporalApplicant temporalApplicant){
        entityManager.merge(temporalApplicant); 
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public TemporalApplicant getTemporalApplicantByIdNumber(String idNumber) {
    try {
        return entityManager.createQuery("SELECT t FROM TemporalApplicant t WHERE t.idNumber=:idNumber", TemporalApplicant.class)
                .setParameter("idNumber", idNumber)
                .getSingleResult();
    } catch (NoResultException e) {
        System.out.println("No applicant with id number " + idNumber);
        return null; // Returning null as no result was found
    }
}
    
    public void deleteTemporalApplicantByIdNumber(String idNumber) {
        TemporalApplicant temp = getTemporalApplicantByIdNumber(idNumber);
        if (temp != null) {
            entityManager.remove(entityManager.contains(temp) ? temp : entityManager.merge(temp));
        }
    }

}
