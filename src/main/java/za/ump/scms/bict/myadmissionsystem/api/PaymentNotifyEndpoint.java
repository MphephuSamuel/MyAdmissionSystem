package za.ump.scms.bict.myadmissionsystem.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import org.primefaces.shaded.json.JSONObject;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;
import za.ump.scms.bict.myadmissionsystem.service.TemporalApplicantService;

@Path("/payment/notify")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class PaymentNotifyEndpoint {

    @Context
    private HttpServletRequest request;
    
    
    @Inject
    private TemporalApplicantService temporalApplicantService;

    @POST
    public Response paymentNotify(
        @FormParam("pf_payment_id") String pfPaymentId,
        @FormParam("payment_status") String paymentStatus,
        @FormParam("amount_gross") String amountGross,
        @FormParam("item_name") String itemName,
        @FormParam("custom_str1") String idNumber
    ) {
        // Debug: See all parameters PayFast sent
        System.out.println("=== Received Raw PayFast Params ===");
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            System.out.println(entry.getKey() + " = " + String.join(", ", entry.getValue()));
        }

       
        // Log specific fields
        System.out.println("✔ Notification received from PayFast:");
        System.out.println("pf_payment_id: " + pfPaymentId);
        System.out.println("payment_status: " + paymentStatus);
        System.out.println("amount_gross: " + amountGross);
        System.out.println("item_name: " + itemName);

        // Process payment based on status
        if ("COMPLETE".equalsIgnoreCase(paymentStatus)) {
            
            System.out.println("💰 Payment completed.");
            TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);
            if (applicant != null) {
                String appData = applicant.getApplicationData();
                JSONObject json = appData != null && !appData.isEmpty()
                        ? new JSONObject(appData)
                        : new JSONObject();

                JSONObject paymentInfo = new JSONObject();
                paymentInfo.put("amount", amountGross);

                json.put("payment", paymentInfo);
                String updatedData = json.toString();

                temporalApplicantService.saveTemporalApplicant(idNumber, updatedData);
            }
        
        } else {
            System.out.println("❌ Payment not successful or failed.");
        }
        return Response.ok().build();
    }
}
