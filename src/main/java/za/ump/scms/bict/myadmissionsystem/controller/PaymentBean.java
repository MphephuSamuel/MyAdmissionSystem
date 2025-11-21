package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.primefaces.shaded.json.JSONObject;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;
import za.ump.scms.bict.myadmissionsystem.service.TemporalApplicantService;

@Named
@SessionScoped
public class PaymentBean implements Serializable {

    private double amount = 100.00;
    private boolean alreadyPaid = false;
    @Inject
    private TemporalApplicantBean temporalApplicantBean;
    
    @Inject
    private TemporalApplicantService temporalApplicantService;
   
    
  @PostConstruct
    public void checkIfApplicantPaied() {
        String idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);

        if (applicant != null) {
            String data = applicant.getApplicationData();
            if (data != null && !data.isEmpty()) {
                JSONObject json = new JSONObject(data);
                if (json.has("payment")) {
                    JSONObject payment = json.getJSONObject("payment");
                    String paidAmount = payment.optString("amount");

                    alreadyPaid = paidAmount != null && paidAmount.equals(String.format("%.2f", amount));
                }
            }
        }
    }

    // Sandbox credentials
    private static final String PAYFAST_MERCHANT_ID = "10038028";
    private static final String PAYFAST_MERCHANT_KEY = "xf6ysfnwpzv2k";
    private static final String PAYFAST_URL = "https://sandbox.payfast.co.za/eng/process";

    public void redirectToPayFast() {
        try {
            
            String idNumber = temporalApplicantBean.getIdNumber();

            String returnUrl = "https://admissionsystem.h2owise.co.za/applicant/payment-succesful.xhtml";
            String cancelUrl = "https://admissionsystem.h2owise.co.za/payment_cancel.xhtml";
            String notifyUrl = "https://admissionsystem.h2owise.co.za/resources/payment/notify";

            // Build PayFast redirect URL
            StringBuilder url = new StringBuilder(PAYFAST_URL);
            url.append("?merchant_id=").append(URLEncoder.encode(PAYFAST_MERCHANT_ID, StandardCharsets.UTF_8));
            url.append("&merchant_key=").append(URLEncoder.encode(PAYFAST_MERCHANT_KEY, StandardCharsets.UTF_8));
            url.append("&amount=").append(String.format("%.2f", amount));
            url.append("&item_name=").append(URLEncoder.encode("Application Fee", StandardCharsets.UTF_8));
            url.append("&return_url=").append(URLEncoder.encode(returnUrl, StandardCharsets.UTF_8));
            url.append("&cancel_url=").append(URLEncoder.encode(cancelUrl, StandardCharsets.UTF_8));
            url.append("&notify_url=").append(URLEncoder.encode(notifyUrl, StandardCharsets.UTF_8));
            
            url.append("&custom_str1=").append(URLEncoder.encode(idNumber, StandardCharsets.UTF_8));

            // Redirect the user
            FacesContext.getCurrentInstance().getExternalContext().redirect(url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void handlePaymentPageAccess() {
        if (alreadyPaid) {
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("confirm-info-before-submit.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public String goToConfirmInfo(){
        return "confirm-info-before-submit.xhtml?faces-redirect=true";
    }


    // Getters and setters
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
