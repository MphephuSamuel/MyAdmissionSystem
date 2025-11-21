package za.ump.scms.bict.myadmissionsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.ApplicationPeriod;

public class ApplicationPeriodRepository {

    @PersistenceContext(unitName = "MyAdmissionSystemPU")
    private EntityManager entityManager;

    public ApplicationPeriod getCurrentPeriod() {
        List<ApplicationPeriod> results = entityManager.createQuery("SELECT a FROM ApplicationPeriod a", ApplicationPeriod.class)
                                                        .setMaxResults(1)
                                                        .getResultList();

        if (results.isEmpty()) {
          
            LocalDate today = LocalDate.now();
            Date opening = Date.from(today.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date closing = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            ApplicationPeriod defaultPeriod = new ApplicationPeriod();
            defaultPeriod.setOpeningDate(opening);
            defaultPeriod.setClosingDate(closing);
            return defaultPeriod;
        }

        return results.get(0);
    }

    public void save(ApplicationPeriod period) {
        entityManager.persist(period);
    }

    public void update(ApplicationPeriod period) {
        entityManager.merge(period);
    }
}
