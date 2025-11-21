/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.model.Payment;
import za.ump.scms.bict.myadmissionsystem.repository.PaymentRepository;

/**
 *
 * @author mphep
 */
public class PaymentService implements Serializable{
    
    @Inject
    private PaymentRepository paymentRepository;
    
    @Inject
    private ApplicantService applicantService;
    
    @Transactional
    public void savePayment(Long applicantId, double amount){
        Applicant applicant = applicantService.getApplicantByApplicantId(applicantId);
        if(applicant!=null){
            Payment payment = new Payment(applicant, amount);
            paymentRepository.savePayment(payment);
        }
        
    }
}
