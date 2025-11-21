package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;
import org.primefaces.shaded.json.JSONObject;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;
import za.ump.scms.bict.myadmissionsystem.service.ApplicantService;
import za.ump.scms.bict.myadmissionsystem.service.ApplicationPeriodService;
import za.ump.scms.bict.myadmissionsystem.service.TemporalApplicantService;

@Named
@SessionScoped
public class TemporalApplicantBean implements Serializable {

    @Inject
    private ApplicantService applicantService;

    @Inject
    private TemporalApplicantService temporalApplicantService;
    
    @Inject
    private ApplicationPeriodService applicationPeriodService;

    private boolean showIdSection = false;
    private String hasStudentNumber = "Yes";

    private String applicationData;

    private String idNumber;
    private String studentNumber;
    private String pin;
    private String title;
    private Date dateOfBirth;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String phoneCountryCode;
    private String altPhoneCountryCode;
    private String phoneNumber;
    private String alternativePhoneNumber;
    private String disability;
    private String homeAddress;
    private String postalAddress;
    private String homeLanguage;
    private String alternativeLanguage;
    private String marriageStatus;
    private String email;
    private String school;
    
    private boolean applicationOpened;

    @PostConstruct
    public void clearSession() {
        applicationOpened = applicationPeriodService.isApplicationOpen();
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        if (facesContext != null) {
            HttpSession session = (HttpSession) facesContext
                    .getExternalContext().getSession(false);
            if (session != null) {
                session.invalidate(); 
            }
        }
    }
    
    public void loadApplicantSession() {
        TemporalApplicant existingApplicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);
        if (existingApplicant != null) {
            applicationData = existingApplicant.getApplicationData();
            JSONObject json = new JSONObject(applicationData);
            JSONObject personalDetails = json.optJSONObject("personalDetails");

            if (personalDetails != null) {
                title = personalDetails.optString("title", "");
                idNumber = personalDetails.optString("idNumber", "");
                pin = personalDetails.optString("pin", "");
                firstName = personalDetails.optString("firstName", "");
                middleName = personalDetails.optString("middleName", "");
                lastName = personalDetails.optString("lastName", "");
                gender = personalDetails.optString("gender", "");
                phoneNumber = personalDetails.optString("phoneNumber", "");
                alternativePhoneNumber = personalDetails.optString("alternativePhoneNumber", "");
                disability = personalDetails.optString("disability", "");
                homeAddress = personalDetails.optString("homeAddress", "");
                postalAddress = personalDetails.optString("postalAddress", "");
                homeLanguage = personalDetails.optString("homeLanguage", "");
                alternativeLanguage = personalDetails.optString("alternativeLanguage", "");
                marriageStatus = personalDetails.optString("marriageStatus", "");
                school = personalDetails.optString("school", "");
                studentNumber = personalDetails.optString("studentNumber", "");
                email = personalDetails.optString("email", "");
                dateOfBirth = (Date) personalDetails.opt("dateOfBirth");

                phoneCountryCode = personalDetails.optString("phoneCountryCode", "");
                altPhoneCountryCode = personalDetails.optString("altPhoneCountryCode", "");
            }
        } else {
            applicationData = "{}";
            title = "";
            pin = "";
            firstName = "";
            middleName = "";
            lastName = "";
            gender = "";
            phoneNumber = "";
            alternativePhoneNumber = "";
            disability = "";
            homeAddress = "";
            postalAddress = "";
            homeLanguage = "";
            alternativeLanguage = "";
            marriageStatus = "";
            school = "";
            studentNumber = "";
            email = "";
            phoneCountryCode = "";
            altPhoneCountryCode = "";
        }
    }

    public String saveApplicationProgress() {
        JSONObject json = new JSONObject(applicationData);
        JSONObject personalDetails = json.optJSONObject("personalDetails");
        if (personalDetails == null) {
            personalDetails = new JSONObject();
        }

        personalDetails.put("idNumber", idNumber);
        personalDetails.put("title", title);
        personalDetails.put("studentNumber", studentNumber);
        personalDetails.put("pin", pin);
        personalDetails.put("dateOfBirth", dateOfBirth);
        personalDetails.put("firstName", firstName);
        personalDetails.put("middleName", middleName);
        personalDetails.put("lastName", lastName);
        personalDetails.put("gender", gender);
        personalDetails.put("phoneNumber", phoneNumber);
        personalDetails.put("alternativePhoneNumber", alternativePhoneNumber);
        personalDetails.put("disability", disability);
        personalDetails.put("homeAddress", homeAddress);
        personalDetails.put("postalAddress", postalAddress);
        personalDetails.put("homeLanguage", homeLanguage);
        personalDetails.put("alternativeLanguage", alternativeLanguage);
        personalDetails.put("marriageStatus", marriageStatus);
        personalDetails.put("school", school);
        personalDetails.put("email", email);
        personalDetails.put("phoneCountryCode", phoneCountryCode);
        personalDetails.put("altPhoneCountryCode", altPhoneCountryCode);

        json.put("personalDetails", personalDetails);
        applicationData = json.toString();

        temporalApplicantService.saveTemporalApplicant(idNumber, applicationData);

        return null;
    }

    public String continueAction() {
        if (isApplicantExists(idNumber)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Please select Yes and Go to the Login Screen", "Please use your student number and PIN to log in."));
            return null;
        } else {
            loadApplicantSession();
            return "applicant/applicant-form.xhtml?faces-redirect=true";
        }
    }

    private boolean isApplicantExists(String idNumber) {
        return applicantService.isApplicantExistsById(idNumber);
    }

    public String goToNextOfKin() {
        saveApplicationProgress();
        return "next-of-kin.xhtml?faces-redirect=true";
    }

    // Getters and Setters

    public String getApplicationData() {
        return applicationData;
    }

    public void setApplicationData(String applicationData) {
        this.applicationData = applicationData;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getAltPhoneCountryCode() {
        return altPhoneCountryCode;
    }

    public void setAltPhoneCountryCode(String altPhoneCountryCode) {
        this.altPhoneCountryCode = altPhoneCountryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAlternativePhoneNumber() {
        return alternativePhoneNumber;
    }

    public void setAlternativePhoneNumber(String alternativePhoneNumber) {
        this.alternativePhoneNumber = alternativePhoneNumber;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getHomeLanguage() {
        return homeLanguage;
    }

    public void setHomeLanguage(String homeLanguage) {
        this.homeLanguage = homeLanguage;
    }

    public String getAlternativeLanguage() {
        return alternativeLanguage;
    }

    public void setAlternativeLanguage(String alternativeLanguage) {
        this.alternativeLanguage = alternativeLanguage;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public boolean isShowIdSection() {
        return showIdSection;
    }

    public void setShowIdSection(boolean showIdSection) {
        this.showIdSection = showIdSection;
    }

    public String getHasStudentNumber() {
        return hasStudentNumber;
    }

    public void setHasStudentNumber(String hasStudentNumber) {
        this.hasStudentNumber = hasStudentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isApplicationOpened() {
        return applicationOpened;
    }

    public void setApplicationOpened(boolean applicationOpened) {
        this.applicationOpened = applicationOpened;
    }

    public String checkStudentNumber() {
        if ("yes".equals(hasStudentNumber)) {
            showIdSection = false;
            return "login.xhtml?faces-redirect=true";
        } else {
            showIdSection = true;
            return null;
        }
    }
}
