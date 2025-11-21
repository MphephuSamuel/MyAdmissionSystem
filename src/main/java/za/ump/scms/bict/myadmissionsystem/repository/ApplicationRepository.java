/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.model.Application;
import za.ump.scms.bict.myadmissionsystem.model.Programme;

/**
 *
 * @author mphep
 */
public class ApplicationRepository {
    
    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager entityManager;
    
    public void saveApplication(Application application){
        entityManager.merge(application);
    }
    
    public List<Application> getAllApplications() {
    List<Application> apps = entityManager.createQuery(
        "SELECT DISTINCT a FROM Application a " +
        "JOIN FETCH a.applicant ap " +
        "LEFT JOIN FETCH ap.marks " +
        "JOIN FETCH a.programme", Application.class)
        .setHint("jakarta.persistence.cache.storeMode", "REFRESH") // Optional: ensures cache bypass
        .getResultList();

    // Optional: Force refresh of marks if still stale
    for (Application app : apps) {
        entityManager.refresh(app.getApplicant());
    }

    return apps;
}

    
    public List<Application> getApplicationsForApplicant(Long applicantId){
        return entityManager.createQuery("SELECT a FROM Application a WHERE a.applicant.id=:applicantId", Application.class)
                .setParameter("applicantId", applicantId)
                .getResultList();
    }
    
    public List<Application> getApplicationsForProgramme(Long programmeId) {
    List<Application> applications = entityManager.createQuery(
        "SELECT DISTINCT a FROM Application a " +
        "JOIN FETCH a.applicant ap " +
        "LEFT JOIN FETCH ap.marks " +
        "JOIN FETCH a.programme " +
        "WHERE a.programme.id = :programmeId", Application.class)
        .setParameter("programmeId", programmeId)
        .setHint("jakarta.persistence.cache.storeMode", "REFRESH") // Bypass 2nd level cache
        .getResultList();

    // Optional: force refresh to avoid stale data
    for (Application app : applications) {
        entityManager.refresh(app.getApplicant()); // ensures fresh marks
    }

    return applications;
}

    
    public Application getApplicationById(Long applicationId) {
    return entityManager.createQuery(
        "SELECT a FROM Application a " +
        "JOIN FETCH a.applicant ap " +
        "LEFT JOIN FETCH ap.marks " +
        "JOIN FETCH a.programme " +
        "WHERE a.id = :applicationId", Application.class)
        .setParameter("applicationId", applicationId)
        .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
        .getSingleResult();
}

    
    public Applicant getApplicantByApplicationId(Long applicationId) {
    Applicant applicant = entityManager.createQuery(
        "SELECT ap FROM Application a " +
        "JOIN a.applicant ap " +
        "LEFT JOIN FETCH ap.marks " +
        "WHERE a.id = :applicationId", Applicant.class)
        .setParameter("applicationId", applicationId)
        .setHint("jakarta.persistence.cache.storeMode", "REFRESH") // force DB read
        .getSingleResult();

    return applicant;
}

}
