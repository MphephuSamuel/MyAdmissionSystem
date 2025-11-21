/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import za.ump.scms.bict.myadmissionsystem.model.StudentNumberState;

/**
 *
 * @author mphep
 */
@Stateless
public class StudentNumberStateRepository {

    @PersistenceContext(unitName="MyAdmissionSystemPU")
    private EntityManager em;

    public StudentNumberState getOrCreateState() {
        StudentNumberState state = em.find(StudentNumberState.class, 1L);
        if (state == null) {
            state = new StudentNumberState();
            em.persist(state);
        }
        return state;
    }

    public void update(StudentNumberState state) {
        em.merge(state);
    }
}