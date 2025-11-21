/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.model.Application;
import za.ump.scms.bict.myadmissionsystem.model.Mark;
import za.ump.scms.bict.myadmissionsystem.model.NextOfKin;
import za.ump.scms.bict.myadmissionsystem.model.Payment;
import za.ump.scms.bict.myadmissionsystem.model.Programme;
import za.ump.scms.bict.myadmissionsystem.model.Subject;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;
import za.ump.scms.bict.myadmissionsystem.service.ApplicantService;
import za.ump.scms.bict.myadmissionsystem.service.ApplicationService;
import za.ump.scms.bict.myadmissionsystem.service.EmailService;
import za.ump.scms.bict.myadmissionsystem.service.MarkService;
import za.ump.scms.bict.myadmissionsystem.service.PaymentService;
import za.ump.scms.bict.myadmissionsystem.service.ProgrammeService;
import za.ump.scms.bict.myadmissionsystem.service.StudentNumberService;
import za.ump.scms.bict.myadmissionsystem.service.SubjectService;
import za.ump.scms.bict.myadmissionsystem.service.TemporalApplicantService;
import at.favre.lib.crypto.bcrypt.BCrypt; 
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import za.ump.scms.bict.myadmissionsystem.model.Document;
import za.ump.scms.bict.myadmissionsystem.service.DocumentService;
import za.ump.scms.bict.myadmissionsystem.service.UserService;

/**
 *
 * @author mphep
 */
@Named
@SessionScoped
public class ConfirmBean implements Serializable{
    
    @Inject
    private TemporalApplicantBean temporalApplicantBean;
    
    @Inject
    private MarkService markService;
    
    @Inject
    private TemporalApplicantService temporalApplicantService;
    
    @Inject
    private SubjectService subjectService;
    
    @Inject
    private ProgrammeService programmeService;
    
    @Inject
    private ApplicantService applicantService;
    
    @Inject
    private PaymentService paymentService;
    
    @Inject
    private ApplicationService applicationService;
    
    @Inject
    private StudentNumberService studentNumberService;
    
    @Inject
    private EmailService emailService;
    
    @Inject
    private UserBean userBean;
    
    @Inject
    private UserService userService;
    
    @Inject
    private DocumentService documentService;

    private Applicant applicant;
    private NextOfKin nextOfKin;
    private Payment payment;
    private List<Mark> marks = new ArrayList<>();
    private List<Application> applications = new ArrayList<>();
    
    private String studentNumber;
    private String pin;

    @PostConstruct
    public void init() {
        loadApplicationDataFromDatabase();
    }

    public void loadApplicationDataFromDatabase() {
        String idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);

        if (applicant != null && applicant.getApplicationData() != null) {
            JSONObject json = new JSONObject(applicant.getApplicationData());

            // Load Personal Details
            if (json.has("personalDetails")) {
                JSONObject pd = json.getJSONObject("personalDetails");
                this.applicant = new Applicant();
                this.applicant.setFirstName(pd.optString("firstName"));
                this.applicant.setMiddleName(pd.optString("middleName"));
                this.applicant.setLastName(pd.optString("lastName"));
                this.applicant.setIdNumber(pd.optString("idNumber"));
            // Concatenate country code with phone number
            String phoneCode = pd.optString("phoneCountryCode", "").trim();
            String phoneNumber = pd.optString("phoneNumber", "").trim();
            this.applicant.setPhoneNumber("(" + phoneCode + ") " + phoneNumber);
            
            // Concatenate alt country code with alt phone number
            String altPhoneCode = pd.optString("altPhoneCountryCode", "").trim();
            String altPhoneNumber = pd.optString("alternativePhoneNumber", "").trim();
            this.applicant.setAlternativePhoneNumber("(" + altPhoneCode + ") " + altPhoneNumber);

                this.applicant.setPostalAddress(pd.optString("postalAddress"));
                this.applicant.setHomeAddress(pd.optString("homeAddress"));
                this.applicant.setTitle(pd.optString("title"));
                this.applicant.setGender(pd.optString("gender"));
                this.applicant.setMarriageStatus(pd.optString("marriageStatus"));
                this.applicant.setDisability(pd.optString("disability"));
                this.applicant.setHomeLanguage(pd.optString("homeLanguage"));
                this.applicant.setAlternativeLanguage(pd.optString("alternativeLanguage"));
                this.applicant.setStudentNumber(pd.optString("studentNumber"));
                this.applicant.setPin(pd.optString("pin"));
                this.applicant.setEmail(pd.optString("email"));
                this.applicant.setSchool(pd.optString("school"));
            }

                // Load Next of Kin
                if (json.has("nextOfKin")) {
                    JSONObject nok = json.getJSONObject("nextOfKin");
                    nextOfKin = new NextOfKin();
                    nextOfKin.setName(nok.optString("name"));
                    nextOfKin.setSurname(nok.optString("surname"));

                    // Extract nested phoneNumber object
                    JSONObject phoneJson = nok.optJSONObject("phoneNumber");
                    if (phoneJson != null) {
                        String nokPhoneCode = phoneJson.optString("code", "");
                        String nokPhone = phoneJson.optString("number", "");
                        nextOfKin.setPhoneNumber("(" + nokPhoneCode + ") " + nokPhone);
                    } else {
                        nextOfKin.setPhoneNumber(""); // fallback if not found
                    }

                    nextOfKin.setEmail(nok.optString("email"));
                }

            // Load Payment
            if (json.has("payment")) {
            JSONObject pay = json.getJSONObject("payment");
            payment = new Payment();
            payment.setAmount(pay.optDouble("amount", 0.0));
        }

            // Load Marks
            if (json.has("marks")) {
                marks.clear(); // Clear the current list to avoid duplicates
                JSONArray marksArray = json.getJSONArray("marks");

                for (int i = 0; i < marksArray.length(); i++) {
                    JSONObject markObj = marksArray.getJSONObject(i);

                    // Fetch subject from database using subjectId
                    Long subjectId = markObj.optLong("subjectId");
                    Subject subject = subjectService.getSubjectById(subjectId);

                    if (subject != null) {
                        Mark mark = new Mark();
                        mark.setSubject(subject);
                        mark.setGradeLevel(markObj.optInt("gradeLevel"));
                        mark.setPercentage(markObj.optInt("finalMark"));
                        mark.setLevel(markObj.optInt("subjectLevel"));
                        mark.setExamYear(markObj.optInt("examYear"));

                        marks.add(mark);
                    } else {
                        System.err.println("Subject with ID " + subjectId + " not found.");
                    }
                }
            }


           if (json.has("applications")) {
                applications.clear();
                JSONArray appArray = json.getJSONArray("applications");
                for (int i = 0; i < appArray.length(); i++) {
                    JSONObject app = appArray.getJSONObject(i);
                    Application application = new Application();


                    Long programmeId = app.optLong("programmeId");
                    if (programmeId != 0) {
                        Programme programme = new Programme();
                        programme.setId(programmeId);
                        application.setProgramme(programme);
                    }

                    application.setChoiceNumber(app.optInt("choiceNumber"));
                    applications.add(application);
                }
            }
        }
    }

        public TemporalApplicantBean getTemporalApplicantBean() {
            return temporalApplicantBean;
        }

        public void setTemporalApplicantBean(TemporalApplicantBean temporalApplicantBean) {
            this.temporalApplicantBean = temporalApplicantBean;
        }

        public Applicant getApplicant() {
            return applicant;
        }

        public void setApplicant(Applicant applicant) {
            this.applicant = applicant;
        }

        public NextOfKin getNextOfKin() {
            return nextOfKin;
        }

        public void setNextOfKin(NextOfKin nextOfKin) {
            this.nextOfKin = nextOfKin;
        }

        public Payment getPayment() {
            return payment;
        }

        public void setPayment(Payment payment) {
            this.payment = payment;
        }

        public List<Mark> getMarks() {
            return marks;
        }

        public void setMarks(List<Mark> marks) {
            this.marks = marks;
        }

        public List<Application> getApplications() {
            return applications;
        }

        public void setApplications(List<Application> applications) {
            this.applications = applications;
        }

        public String getSubjectName(Long subjectId) {
            Subject subject = subjectService.getSubjectById(subjectId);
            return subject != null ? subject.getName() : "Unknown Subject";
        }

        public String getProgrammeName(Long programmeId) {
            Programme programme = programmeService.getProgrammeById(programmeId);
            return programme != null ? programme.getName() : "Unknown Programme";
        }
        
        public String goBack(){
            return null;
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
        
        
        
        public String submitApplication(){
            studentNumber = studentNumberService.generateStudentNumber();
            pin = studentNumberService.generateFiveDigitPin();
            applicantService.saveApplicant(applicant.getIdNumber(), studentNumber, pin,
                    applicant.getTitle(), applicant.getDateOfBIrth(), applicant.getFirstName(), applicant.getMiddleName(), 
                    applicant.getLastName(), applicant.getGender(), applicant.getPhoneNumber(), applicant.getAlternativePhoneNumber(), 
                    applicant.getDisability(), applicant.getHomeAddress(), applicant.getPostalAddress(), 
                    applicant.getHomeLanguage(), applicant.getAlternativeLanguage(), applicant.getMarriageStatus(), applicant.getEmail(), applicant.getSchool());
            
            String hashedPassword = BCrypt.withDefaults().hashToString(12, pin.toCharArray());
            userService.userRegister(studentNumber, hashedPassword, "student");
            
            Applicant theApplicant = applicantService.getApplicantByIdNumber(applicant.getIdNumber());
            for(int i =0;i<marks.size(); i++){
                markService.saveMark(marks.get(i).getSubject().getId(),applicantService.getApplicantByIdNumber(applicant.getIdNumber()).getId(), marks.get(i).getGradeLevel(), marks.get(i).getPercentage(), marks.get(i).getLevel(), marks.get(i).getExamYear());
            }
            System.out.println();
            
            paymentService.savePayment(theApplicant.getId(), payment.getAmount());
            System.out.println("now saving the programme");
            for(int i=0;i<applications.size();i++){
                applicationService.saveApplication(theApplicant.getId(), applications.get(i).getProgramme().getId(), applications.get(i).getChoiceNumber());
            }
            
            TemporalApplicant tempApplicant = temporalApplicantService.findTemporalApplicantByIdNumber(applicant.getIdNumber());
            
            documentService.saveDocument(theApplicant.getId(), tempApplicant.getIdDocumentData(), tempApplicant.getIdDocumentFilename(), tempApplicant.getIdDocumentFileType(), 
                   tempApplicant.getG12TranscriptData(), tempApplicant.getG12TranscriptFilename(), tempApplicant.getG12TranscriptFileType());
            
            System.out.println("done saving everything");
            String emailSubject = "Application Submitted";
            String emailMessage = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "  body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
            "  .container { background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }" +
            "  h2 { color: #004080; }" +
            "  p { line-height: 1.6; color: #333333; }" +
            "  .highlight { font-weight: bold; color: #004080; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<h2>Application Submitted Successfully</h2>" +
            "<p>Dear " + applicant.getTitle() + " " + applicant.getLastName() + ",</p>" +
            "<p>Congratulations! Your application has been successfully submitted to the <strong>University of Mpumalanga</strong>. Your application is now under review, and we will notify you once the next steps have been determined.</p>" +
            "<p><strong>Please find your important details below:</strong></p>" +
            "<ul>" +
            "  <li><span class='highlight'>Student Number:</span> " + studentNumber + "</li>" +
            "  <li><span class='highlight'>PIN:</span> " + pin + "</li>" +
            "</ul>" +
            "<p>Kindly keep these details secure as they will be required for accessing your admission status and for any future correspondence.</p>" +
            "<p>If you have any questions, please contact our Admissions Office at <a href='mailto:admissions@ump.ac.za'>admissions@ump.ac.za</a> or call our support line.</p>" +
            "<p>Thank you for choosing the University of Mpumalanga. We look forward to assisting you through the admission process.</p>" +
            "<p>Sincerely,<br>The Admissions Team<br><strong>University of Mpumalanga</strong></p>" +
            "</div>" +
            "</body>" +
            "</html>";
            System.out.println(applicant.getEmail());
            System.out.println(theApplicant.getEmail());
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                         "Application submitted successfully", 
                         "A confirmation email will be sent shortly."));
            
            emailService.sendEmail(theApplicant.getEmail(), emailSubject, emailMessage);
            
            temporalApplicantService.deleteTemporalApplicantByIdNumber(theApplicant.getIdNumber());
            return "successPage?faces-redirect=true";
        }
        
        public String goToLogin() {
            return  "/login.xhtml?faces-redirect=true";
        }
      
}
