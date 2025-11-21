package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.model.Application;
import za.ump.scms.bict.myadmissionsystem.model.Document;
import za.ump.scms.bict.myadmissionsystem.model.Mark;
import za.ump.scms.bict.myadmissionsystem.model.ProgrammeLeader;
import za.ump.scms.bict.myadmissionsystem.service.ApplicationService;
import za.ump.scms.bict.myadmissionsystem.service.DecisionService;
import za.ump.scms.bict.myadmissionsystem.service.DocumentService;
import za.ump.scms.bict.myadmissionsystem.service.EmailService;
import za.ump.scms.bict.myadmissionsystem.service.ProgrammeLeaderService;
import za.ump.scms.bict.myadmissionsystem.service.ProgrammeService;

/**
 *
 * @author mphep
 */
@SessionScoped
@Named
public class ProgrammeLeaderController implements Serializable {
    // Required for proper session management
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private Long programmeLeaderId = 1L;
    private List<Application> applications = new ArrayList<>();
    
    private List<Application> allApplications;
    
    private double studentAPS;
    
    @Inject
    private ProgrammeLeaderService programmeLeaderService;
    
    @Inject
    private ProgrammeService programmeService;
    
    @Inject
    private ApplicationService applicationService;
    
    @Inject
    private DecisionService decisionService;
   
    @Inject
    private EmailService emailService;
    
    @Inject
    private DocumentService documentService;
    
    private List<Application> acceptedApplications = new ArrayList<>();
    
    private List<Application> rejectedApplications = new ArrayList<>();
    
    private List<Application> waitlistedApplications = new ArrayList<>();
    
    // This field must be properly serialized as part of the session
    private Set<Long> expandedApplicants = new HashSet<>();

    @PostConstruct
public void loadApplications() {
    // Get the FacesContext for session access
    FacesContext context = FacesContext.getCurrentInstance();
    Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
    
    username = (String) sessionMap.get("username");
    System.out.println(username + " in the controller");

    // Restore expanded applicants state from session if available
    @SuppressWarnings("unchecked")
    Set<Long> savedExpandedSet = (Set<Long>) sessionMap.get("expandedApplicants");
    if (savedExpandedSet != null) {
        expandedApplicants = savedExpandedSet;
        System.out.println("Restored expanded set from session: " + expandedApplicants);
    } else {
        expandedApplicants = new HashSet<>();
    }

    ProgrammeLeader programmeLeader = programmeLeaderService.getProgrammeLeaderByUsername(username);
    
    List<Application> allApplications = applicationService.getApplicationsByProgrammeId(programmeLeader.getProgramme().getId());
    
    allApplications.sort(Comparator.comparingDouble((Application app) -> calculateAPS(app.getApplicant().getMarks())).reversed());
    // Clear and re-populate all lists
    applications.clear();
    acceptedApplications.clear();
    rejectedApplications.clear();
    waitlistedApplications.clear(); // Clear the waitlist as well
    
    for (Application app : allApplications) {
        if ("ADMITTED".equalsIgnoreCase(app.getStatus())) {
            acceptedApplications.add(app);
        }
        else if("REJECTED".equalsIgnoreCase(app.getStatus())){
            rejectedApplications.add(app);
        }
        else if("WAITLIST".equalsIgnoreCase(app.getStatus())) {
            waitlistedApplications.add(app);
        }
        else {
            applications.add(app);
        }
    }
}

    // Getter for accepted applications (needed by the XHTML page)
    public List<Application> getAcceptedApplications() {
        return acceptedApplications;
    }

    
    public List<Application> getWaitlistedApplications() {
    return waitlistedApplications;
}
    
    public List<Application> getRejectedApplications() {
        return rejectedApplications;
    }
        
    public double calculateAPS(List<Mark> marks){
        double aps=0;
        for(Mark mark : marks){
           if(mark.getSubject().getName().equalsIgnoreCase("Life Orientation")){
                aps=aps+mark.getLevel()/2;
            }
            else{
                aps=aps+mark.getLevel();
            }
        }
        return aps;
    }
    
    
    public String admit(Long applicationId) {
        // Retrieve the current logged-in Programme Leader using username from session
         ProgrammeLeader programmeLeader = programmeLeaderService.getProgrammeLeaderByUsername(username);
        Long leaderProgrammeId = programmeLeader.getProgramme().getId();

        Application application = applicationService.getApplicationById(applicationId);

        // Verify programme ownership
        if (!application.getProgramme().getId().equals(leaderProgrammeId)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You cannot admit applicants for a programme you do not manage.",
                    "Unauthorized Action"));
            return null;
        }

        // Check if capacity is full BEFORE admitting
        int currentAdmitted = (int) applicationService.getApplicationsByProgrammeId(leaderProgrammeId)
                .stream()
                .filter(app -> "ADMITTED".equalsIgnoreCase(app.getStatus()))
                .count();

        int maxCapacity = programmeLeader.getProgramme().getMaxCapacity();

        if (currentAdmitted >= maxCapacity) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Admission Limit Reached",
                    "No more students can be admitted for this programme."));
            return null;
        }

        // At this point, it's safe to admit
        applicationService.updateStatus(applicationId, "ADMITTED");
        decisionService.saveDecision(applicationId, "ADMITTED", "Excellent");

        Applicant applicant = application.getApplicant();

            
        String emailSubject = "Admission Offer – University of Mpumalanga";

        String emailMessage = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "  body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
            "  .container { background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }" +
            "  h2 { color: #2c3e50; }" +
            "  p, li { font-size: 16px; color: #333333; line-height: 1.6; }" +
            "  ul { padding-left: 20px; }" +
            "  .highlight { font-weight: bold; color: #004080; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<h2>Congratulations on Your Admission!</h2>" +
            "<p>Dear " + applicant.getTitle() + " " + applicant.getLastName() + ",</p>" +
            "<p>We are pleased to inform you that your application has been <strong>successfully admitted</strong> to the <strong>" + application.getProgramme().getName() + "</strong> at the <strong>University of Mpumalanga</strong>.</p>" +
            "<p>We commend you on your achievement and welcome you to our vibrant academic community.</p>" +
            "<p><strong>Your student details are as follows:</strong></p>" +
            "<ul>" +
            "  <li><span class='highlight'>Student Number:</span> " + applicant.getStudentNumber() + "</li>" +
            "  <li><span class='highlight'>Programme:</span> " + application.getProgramme().getName() + "</li>" +
            "</ul>" +
            "<p>Please keep this information safe as it will be required for registration, accessing campus services, and future communication.</p>" +
            "<p>If you have any questions, feel free to contact our Admissions Office at <a href='mailto:admissions@ump.ac.za'>admissions@ump.ac.za</a>.</p>" +
            "<p>Thank you for choosing the University of Mpumalanga. We look forward to supporting you on your academic journey.</p>" +
            "<p>Sincerely,<br>The Admissions Team<br><strong>University of Mpumalanga</strong></p>" +
            "</div>" +
            "</body>" +
            "</html>";

        // Log applicant email for debugging
        System.out.println("Sending email to: " + applicant.getEmail());

        // Notify on JSF UI
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Application admitted successfully", 
                "An admission confirmation email has been sent to the applicant."));

        // Send email
        emailService.sendEmail(applicant.getEmail(), emailSubject, emailMessage);

        loadApplications();
        refreshAllApplications(); 
        return null;
    }

    
    public String reject(Long applicationId) {
        ProgrammeLeader programmeLeader = programmeLeaderService.getProgrammeLeaderByUsername(username);
        Long leaderProgrammeId = programmeLeader.getProgramme().getId();

        Application application = applicationService.getApplicationById(applicationId);

        if (!application.getProgramme().getId().equals(leaderProgrammeId)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Unauthorized Action",
                    "You cannot reject applicants for a programme you do not manage."));
            return null;
        }
            
        // Update application status and save decision
        applicationService.updateStatus(applicationId, "REJECTED");
        decisionService.saveDecision(applicationId, "REJECTED", "Not Qualified");

        // Retrieve applicant details
        Applicant applicant = application.getApplicant();

        String emailSubject = "Application Outcome – University of Mpumalanga";

        String emailMessage = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "  body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
            "  .container { background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }" +
            "  h2 { color: #b71c1c; }" +
            "  p, li { font-size: 16px; color: #333333; line-height: 1.6; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<h2>Application Outcome</h2>" +
            "<p>Dear " + applicant.getTitle() + " " + applicant.getLastName() + ",</p>" +
            "<p>Thank you for applying to the <strong>University of Mpumalanga</strong> for the <strong>" + application.getProgramme().getName() + "</strong> programme.</p>" +
            "<p>After careful consideration, we regret to inform you that your application has not been successful. Unfortunately, you did not meet the minimum admission criteria required for this programme.</p>" +
            "<p>We understand that this news may be disappointing, and we encourage you not to be discouraged. You are welcome to consider applying for other programmes in the future or explore alternative opportunities for further education or skill development.</p>" +
            "<p>If you have any questions regarding your application or would like further guidance, feel free to contact our Admissions Office at <a href='mailto:admissions@ump.ac.za'>admissions@ump.ac.za</a>.</p>" +
            "<p>We sincerely appreciate your interest in the University of Mpumalanga and wish you every success in your future endeavors.</p>" +
            "<p>Sincerely,<br>The Admissions Team<br><strong>University of Mpumalanga</strong></p>" +
            "</div>" +
            "</body>" +
            "</html>";

        // Log applicant email for debugging
        System.out.println("Sending email to: " + applicant.getEmail());

        // Notify on JSF UI
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Application rejected", 
                "A rejection notification email has been sent to the applicant."));

        // Send email
        emailService.sendEmail(applicant.getEmail(), emailSubject, emailMessage);

        // Refresh lists to move application to rejected list
        loadApplications();
        refreshAllApplications();
        return null;
    }

    
    public String waitList(Long applicationId) {
        ProgrammeLeader programmeLeader = programmeLeaderService.getProgrammeLeaderByUsername(username);
        Long leaderProgrammeId = programmeLeader.getProgramme().getId();

        Application application = applicationService.getApplicationById(applicationId);

        if (!application.getProgramme().getId().equals(leaderProgrammeId)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Unauthorized Action",
                    "You cannot waitlist applicants for a programme you do not manage."));
            return null;
        }
        // Update application status and save decision
        applicationService.updateStatus(applicationId, "WAITLIST");
        decisionService.saveDecision(applicationId, "WAITLIST", "Be On Standby");

        // Retrieve application and applicant details
        Applicant applicant = application.getApplicant();

        String emailSubject = "Application Status – Waiting List Notification";

        String emailMessage = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "  body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
            "  .container { background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }" +
            "  h2 { color: #f57c00; }" +
            "  p, li { font-size: 16px; color: #333333; line-height: 1.6; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<h2>Application Status: Waiting List</h2>" +
            "<p>Dear " + applicant.getTitle() + " " + applicant.getLastName() + ",</p>" +
            "<p>Thank you for applying to the <strong>University of Mpumalanga</strong> for the <strong>" + application.getProgramme().getName() + "</strong> programme.</p>" +
            "<p>We would like to inform you that your application is currently on our <strong>waiting list</strong>. This means that your application met the minimum entry requirements but final decisions are pending based on available space and the number of confirmed offers.</p>" +
            "<p>We appreciate your patience and will notify you immediately should a place become available. In the meantime, we encourage you to regularly check your email for updates from our Admissions Office.</p>" +
            "<p>If you have any questions, feel free to reach out to us at <a href='mailto:admissions@ump.ac.za'>admissions@ump.ac.za</a>.</p>" +
            "<p>Thank you again for considering the University of Mpumalanga. We hope to welcome you soon.</p>" +
            "<p>Sincerely,<br>The Admissions Team<br><strong>University of Mpumalanga</strong></p>" +
            "</div>" +
            "</body>" +
            "</html>";

        // Log applicant email for debugging
        System.out.println("Sending email to: " + applicant.getEmail());

        // Notify on JSF UI
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Application placed on waiting list", 
                "A waiting list notification email has been sent to the applicant."));

        // Send email
        emailService.sendEmail(applicant.getEmail(), emailSubject, emailMessage);

        // Refresh lists to ensure waiting list is updated
        loadApplications();
        refreshAllApplications();
        return null;
    }

    public String revoke(Long applicationId) {
        // Update application status and save decision
        applicationService.updateStatus(applicationId, "Pending");
        decisionService.saveDecision(applicationId, "Pending", "Admission Revoked - Re-evaluation in progress");

        // Retrieve application and applicant details
        Application application = applicationService.getApplicationById(applicationId);
        Applicant applicant = application.getApplicant();

        String emailSubject = "Admission Update – University of Mpumalanga";

        String emailMessage = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "  body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
            "  .container { background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }" +
            "  h2 { color: #d84315; }" +
            "  p, li { font-size: 16px; color: #333333; line-height: 1.6; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<h2>Update on Your Admission Status</h2>" +
            "<p>Dear " + applicant.getTitle() + " " + applicant.getLastName() + ",</p>" +
            "<p>We would like to inform you that your previous admission to the <strong>" + application.getProgramme().getName() + "</strong> at the <strong>University of Mpumalanga</strong> has been <strong>revoked temporarily</strong> for re-evaluation purposes.</p>" +
            "<p>Your application status has been reset to <strong>Pending</strong>, and it is currently under further consideration by our admissions team.</p>" +
            "<p>This decision does not imply rejection but rather allows us to conduct a thorough review. We appreciate your understanding during this process.</p>" +
            "<p>Should we require any additional information from you, we will be in touch. Please monitor your email closely for updates.</p>" +
            "<p>If you have any questions, you may contact our Admissions Office at <a href='mailto:admissions@ump.ac.za'>admissions@ump.ac.za</a>.</p>" +
            "<p>Sincerely,<br>The Admissions Team<br><strong>University of Mpumalanga</strong></p>" +
            "</div>" +
            "</body>" +
            "</html>";

        System.out.println("Sending revocation email to: " + applicant.getEmail());

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Admission revoked",
                "A revocation email has been sent, and the application is now pending."));

        emailService.sendEmail(applicant.getEmail(), emailSubject, emailMessage);

        // Refresh lists so the application reappears in the first table
        loadApplications();
        refreshAllApplications();
        return null;
    }
    
    public String reEvaluate(Long applicationId) {
        // Update application status and save decision
        applicationService.updateStatus(applicationId, "Pending");
        decisionService.saveDecision(applicationId, "Pending", "Application Re-evaluation in progress");

        // Retrieve application and applicant details
        Application application = applicationService.getApplicationById(applicationId);
        Applicant applicant = application.getApplicant();

        String emailSubject = "Application Status Update – Re-evaluation";

        String emailMessage = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "  body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
            "  .container { background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }" +
            "  h2 { color: #0277bd; }" +
            "  p, li { font-size: 16px; color: #333333; line-height: 1.6; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<h2>Application Status: Under Re-evaluation</h2>" +
            "<p>Dear " + applicant.getTitle() + " " + applicant.getLastName() + ",</p>" +
            "<p>We are pleased to inform you that your application to the <strong>" + application.getProgramme().getName() + "</strong> at the <strong>University of Mpumalanga</strong> has been moved from rejected status and is now being <strong>re-evaluated</strong>.</p>" +
            "<p>This means your application is receiving further consideration by our admissions committee. We will review your qualifications and credentials once more to ensure a fair assessment.</p>" +
            "<p>You will be notified of the final decision as soon as the re-evaluation process is complete. No further action is required from you at this time.</p>" +
            "<p>If you have any questions, please contact our Admissions Office at <a href='mailto:admissions@ump.ac.za'>admissions@ump.ac.za</a>.</p>" +
            "<p>Thank you for your interest in the University of Mpumalanga.</p>" +
            "<p>Sincerely,<br>The Admissions Team<br><strong>University of Mpumalanga</strong></p>" +
            "</div>" +
            "</body>" +
            "</html>";

        // Log applicant email for debugging
        System.out.println("Sending re-evaluation email to: " + applicant.getEmail());

        // Notify on JSF UI
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Application moved for re-evaluation", 
                "A re-evaluation notification email has been sent to the applicant."));

        // Send email
        emailService.sendEmail(applicant.getEmail(), emailSubject, emailMessage);

        // Update the lists with a full reload
        loadApplications();
        refreshAllApplications();
        return null;
    }

    public void downloadIdDocument(Long applicationId) {
    try {
        // Retrieve applicant using application ID
        Applicant applicant = applicationService.getApplicantByApplicationId(applicationId);
        if (applicant != null) {
            // Get document using applicant ID
            Document document = documentService.getDocumentByApplicantId(applicant.getId());
            if (document != null && document.getIdDocumentData() != null) {

                // Logging (optional for debugging)
                System.out.println("Downloading ID Document for Applicant ID: " + applicant.getId());
                System.out.println("Filename: " + document.getIdDocumentFilename());
                System.out.println("Size: " + document.getIdDocumentData().length);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = facesContext.getExternalContext();

                // Reset and set headers
                externalContext.responseReset();
                externalContext.setResponseContentType(document.getIdDocumentFileType());
                externalContext.setResponseContentLength(document.getIdDocumentData().length);
                externalContext.setResponseHeader("Content-Disposition", 
                    "attachment; filename=\"" + document.getIdDocumentFilename() + "\"");

                // Write binary data to output stream
                externalContext.getResponseOutputStream().write(document.getIdDocumentData());
                facesContext.responseComplete(); // Important to stop JSF lifecycle here
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Document Not Found",
                        "No ID document available for this applicant."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Applicant Not Found",
                    "Could not find applicant for the provided application ID."));
        }
    } catch (IOException e) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Download Error",
                "An error occurred while downloading the document: " + e.getMessage()));
        e.printStackTrace(); // optional
    }
}

   
   public void downloadG12Transcript(Long applicationId) {
    try {
        // Retrieve applicant using application ID
        Applicant applicant = applicationService.getApplicantByApplicationId(applicationId);
        if (applicant != null) {
            // Get document using applicant ID
            Document document = documentService.getDocumentByApplicantId(applicant.getId());
            if (document != null && document.getG12TranscriptData() != null) {

                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = facesContext.getExternalContext();

                // Set headers for download
                externalContext.responseReset();
                externalContext.setResponseContentType(document.getG12TranscriptFileType());
                externalContext.setResponseContentLength(document.getG12TranscriptData().length);
                externalContext.setResponseHeader("Content-Disposition",
                        "attachment; filename=\"" + document.getG12TranscriptFilename() + "\"");

                // Write file content to response
                externalContext.getResponseOutputStream().write(document.getG12TranscriptData());
                facesContext.responseComplete(); // Prevent JSF lifecycle from continuing
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "No G12 Transcript",
                                "No Grade 12 transcript available for this applicant."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Applicant Not Found",
                            "Could not find applicant for the provided application ID."));
        }
    } catch (IOException e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Download Error",
                        "Failed to download Grade 12 transcript: " + e.getMessage()));
        e.printStackTrace();
    }
}

    
    public String getAdmissionProgress() {
        ProgrammeLeader leader = programmeLeaderService.getProgrammeLeaderByUsername(username);
        int maxCapacity = leader.getProgramme().getMaxCapacity();
        long admittedCount = acceptedApplications.size();
        return admittedCount + " / " + maxCapacity + " students admitted";
    }

    // Calculate percentage for progress bar width
    public int getProgressBarPercentage() {
        ProgrammeLeader leader = programmeLeaderService.getProgrammeLeaderByUsername(username);
        int maxCapacity = leader.getProgramme().getMaxCapacity();
        long admittedCount = acceptedApplications.size();
        
        // Calculate percentage but cap at 100%
        int percentage = (int) (((double) admittedCount / maxCapacity) * 100);
        return Math.min(percentage, 100);
    }

    // Get CSS class based on capacity level
    public String getCapacityLevelClass() {
        int percentage = getProgressBarPercentage();
        
        if (percentage < 50) {
            return "capacity-warning capacity-low";
        } else if (percentage < 90) {
            return "capacity-warning capacity-medium";
        } else {
            return "capacity-warning capacity-high";
        }
    }

    // Get text for capacity level indicator
    public String getCapacityLevelText() {
        int percentage = getProgressBarPercentage();
        
        if (percentage < 50) {
            return "Plenty of spaces";
        } else if (percentage < 90) {
            return "Filling up";
        } else {
            return "Full";
        }
    }
    
    // Updated toggle marks method with proper state management
    public void toggleMarks(Long applicantId) {
        if (expandedApplicants == null) {
            expandedApplicants = new HashSet<>();
        }
        
        if (expandedApplicants.contains(applicantId)) {
            expandedApplicants.remove(applicantId);
        } else {
            expandedApplicants.add(applicantId);
        }
        
        // Force immediate update
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("applicationsForm");
    }
    
    public List<Application> getAllApplications() {
    if (allApplications == null) {
        // Fetch all applications regardless of status
        allApplications = applicationService.getAllApplications();
    }
    return allApplications;
}
    
    public void refreshAllApplications() {
    allApplications = applicationService.getAllApplications();
}
    
    public boolean isExpanded(Long applicantId) {
        return expandedApplicants.contains(applicantId);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getProgrammeLeaderId() {
        return programmeLeaderId;
    }

    public void setProgrammeLeaderId(Long programmeLeaderId) {
        this.programmeLeaderId = programmeLeaderId;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
    
    // Getter and setter for expanded applicants set
    public Set<Long> getExpandedApplicants() {
        return expandedApplicants;
    }
    
    public void setExpandedApplicants(Set<Long> expandedApplicants) {
        this.expandedApplicants = expandedApplicants;
    }
}
