/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import za.ump.scms.bict.myadmissionsystem.model.Application;
import za.ump.scms.bict.myadmissionsystem.model.Decision;
import za.ump.scms.bict.myadmissionsystem.repository.DecisionRepository;

/**
 *
 * @author mphep
 */
@Named
@SessionScoped
public class DecisionService implements Serializable{
    
    @Inject
    private DecisionRepository decisionRepository;
    
    @Inject
    private ApplicationService applicationService;
    
    @Transactional
    public void  saveDecision(Long applicationId, String decision, String decisionNote){
        Application application = applicationService.getApplicationById(applicationId);
        Decision theDecision = new Decision(application, decision, new Date(), decisionNote);
        decisionRepository.saveDecision(theDecision);
    }
    
}
