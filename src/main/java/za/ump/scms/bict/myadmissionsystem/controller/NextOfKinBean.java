package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.shaded.json.JSONObject;
import za.ump.scms.bict.myadmissionsystem.service.TemporalApplicantService;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;

@Named
@SessionScoped
public class NextOfKinBean implements Serializable {

    private String applicationData;

    private String title;
    private String name;
    private String surname;
    private String email;
    private String phoneCode;       // New field for phone code
    private String phoneNumber;
    private String homeAddress;
    private String postalAddress;

    @Inject
    private TemporalApplicantBean temporalApplicantBean;

    @Inject
    private TemporalApplicantService temporalApplicantService;

    // Load next-of-kin details from applicationData
    public void loadNextOfKin() {
        title = "";
        name = "";
        surname = "";
        email = "";
        phoneCode = "";       // Reset phone code
        phoneNumber = "";
        homeAddress = "";
        postalAddress = "";

        String idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);
        if (applicant != null) {
            JSONObject json = new JSONObject(applicant.getApplicationData());
            JSONObject nextOfKin = json.optJSONObject("nextOfKin");
            if (nextOfKin != null) {
                this.title = nextOfKin.optString("title", "");
                this.name = nextOfKin.optString("name", "");
                this.surname = nextOfKin.optString("surname", "");
                this.email = nextOfKin.optString("email", "");
                this.homeAddress = nextOfKin.optString("homeAddress", "");
                this.postalAddress = nextOfKin.optString("postalAddress", "");

                JSONObject phoneJson = nextOfKin.optJSONObject("phoneNumber");
                if (phoneJson != null) {
                    this.phoneCode = phoneJson.optString("code", "");
                    this.phoneNumber = phoneJson.optString("number", "");
                }
            }
        }
    }

    // Save next-of-kin details to applicationData
    public String saveApplicant() {
        String idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);
        JSONObject json = applicant != null
                ? new JSONObject(applicant.getApplicationData())
                : new JSONObject();

        // Store phone number using mapping (code + number)
        Map<String, String> phoneMap = new HashMap<>();
        phoneMap.put("code", phoneCode);
        phoneMap.put("number", phoneNumber);
        JSONObject phoneJson = new JSONObject(phoneMap);

        JSONObject nextOfKin = new JSONObject();
        nextOfKin.put("title", title);
        nextOfKin.put("name", name);
        nextOfKin.put("surname", surname);
        nextOfKin.put("email", email);
        nextOfKin.put("phoneNumber", phoneJson);  // nested phone object
        nextOfKin.put("homeAddress", homeAddress);
        nextOfKin.put("postalAddress", postalAddress);

        json.put("nextOfKin", nextOfKin);
        applicationData = json.toString();

        temporalApplicantService.saveTemporalApplicant(idNumber, applicationData);

        return null; // Stay on same page or return to summary
    }

    public String goToMarksSelection() {
        saveApplicant();
        return "marks-selection.xhtml?faces-redirect=true";
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneCode() { return phoneCode; }
    public void setPhoneCode(String phoneCode) { this.phoneCode = phoneCode; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getHomeAddress() { return homeAddress; }
    public void setHomeAddress(String homeAddress) { this.homeAddress = homeAddress; }

    public String getPostalAddress() { return postalAddress; }
    public void setPostalAddress(String postalAddress) { this.postalAddress = postalAddress; }
}
