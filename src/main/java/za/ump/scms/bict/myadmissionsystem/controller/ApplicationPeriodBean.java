package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Date;
import za.ump.scms.bict.myadmissionsystem.model.ApplicationPeriod;
import za.ump.scms.bict.myadmissionsystem.service.ApplicationPeriodService;

@Named
@SessionScoped
public class ApplicationPeriodBean implements Serializable {

    private Date openingDate;
    private Date closingDate;

    @Inject
    private ApplicationPeriodService periodService;

    @PostConstruct
    public void init() {
        loadCurrentPeriod();
    }
    
    public void loadCurrentPeriod() {
        ApplicationPeriod current = periodService.getCurrentPeriod();
        this.openingDate = current.getOpeningDate();
        this.closingDate = current.getClosingDate();
    }

    public void updatePeriod() {
        ApplicationPeriod current = periodService.getCurrentPeriod();
        periodService.updateApplicationPeriod(current, openingDate, closingDate);
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }
}
