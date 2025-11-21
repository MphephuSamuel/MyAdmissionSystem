/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import za.ump.scms.bict.myadmissionsystem.model.Document;

/**
 *
 * @author mphep
 */
public class DocumentRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveDocument(Document document){
        entityManager.merge(document);
    }
    
    public Document findByApplicantId(Long applicantId) {
        try {
            TypedQuery<Document> query = entityManager.createQuery(
                "SELECT d FROM Document d WHERE d.applicant.id = :applicantId", 
                Document.class);
            query.setParameter("applicantId", applicantId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
