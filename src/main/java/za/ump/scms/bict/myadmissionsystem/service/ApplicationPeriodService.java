package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import za.ump.scms.bict.myadmissionsystem.model.ApplicationPeriod;
import za.ump.scms.bict.myadmissionsystem.repository.ApplicationPeriodRepository;

@Stateless
public class ApplicationPeriodService {

    @Inject
    private ApplicationPeriodRepository applicationRepositoryRepository;

    public boolean isApplicationOpen() {
        ApplicationPeriod period = applicationRepositoryRepository.getCurrentPeriod();
        LocalDate today = LocalDate.now();

        LocalDate opening = period.getOpeningDate().toInstant()
                                   .atZone(ZoneId.systemDefault())
                                   .toLocalDate();

        LocalDate closing = period.getClosingDate().toInstant()
                                   .atZone(ZoneId.systemDefault())
                                   .toLocalDate();

        System.out.println(opening);
        System.out.println(closing);
        System.out.println(!today.isBefore(opening) && !today.isAfter(closing));
        return !today.isBefore(opening) && !today.isAfter(closing);
    }


    public void updateApplicationPeriod(ApplicationPeriod existingPeriod, Date newOpening, Date newClosing) {
        existingPeriod.setOpeningDate(newOpening);
        existingPeriod.setClosingDate(newClosing);
        applicationRepositoryRepository.update(existingPeriod);
    }

    public ApplicationPeriod getCurrentPeriod() {
        return applicationRepositoryRepository.getCurrentPeriod();
    }
}
