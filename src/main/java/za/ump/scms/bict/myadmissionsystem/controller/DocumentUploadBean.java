package za.ump.scms.bict.myadmissionsystem.controller;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import za.ump.scms.bict.myadmissionsystem.service.TemporalApplicantService;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;

@Named
@SessionScoped
public class DocumentUploadBean implements Serializable {

    private Part idDocument;
    private Part g12Transcript;
    private String idNumber;
    private String existingIdDocName;
    private String existingG12TranscriptName;

    @EJB
    private TemporalApplicantService temporalApplicantService;
    
    @Inject
    private TemporalApplicantBean temporalApplicantBean;

    public String upload() {
    try {
        idNumber = temporalApplicantBean.getIdNumber();

        byte[] idDocBytes = null;
        String idDocName = null;
        String idDocType = null;

        byte[] g12Bytes = null;
        String g12Name = null;
        String g12Type = null;

        // Check if the ID Document has been uploaded and update it
        if (idDocument != null) {
            try (InputStream input = idDocument.getInputStream()) {
                idDocBytes = input.readAllBytes();
                idDocName = idDocument.getSubmittedFileName();
                idDocType = idDocument.getContentType();
            }
        }

        // Check if the G12 Transcript has been uploaded and update it
        if (g12Transcript != null) {
            try (InputStream input = g12Transcript.getInputStream()) {
                g12Bytes = input.readAllBytes();
                g12Name = g12Transcript.getSubmittedFileName();
                g12Type = g12Transcript.getContentType();
            }
        }

        // Now only update the fields that have been uploaded
        temporalApplicantService.updateApplicantDocuments(idNumber,
                idDocBytes, idDocName, idDocType,
                g12Bytes, g12Name, g12Type);

        return "proceed-to-payment.xhtml?faces-redirect=true";

    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

    
    public void loadExistingDocuments() {
        idNumber = temporalApplicantBean.getIdNumber();
        TemporalApplicant applicant = temporalApplicantService.findTemporalApplicantByIdNumber(idNumber);

        if (applicant != null) {
            existingIdDocName = applicant.getIdDocumentFilename();
            existingG12TranscriptName = applicant.getG12TranscriptFilename();
        }
    }
    
    public void removeIdDocument() {
        // Ensure only the ID document is being cleared
        if (existingIdDocName != null) {
            temporalApplicantService.removeIdDocument(idNumber);
            existingIdDocName = null;  // Explicitly reset the state
            idDocument = null;         // Explicitly reset the file part
        }
        // Ensure G12 Transcript state is unaffected
        existingG12TranscriptName = existingG12TranscriptName;
    }


    public void removeG12Transcript() {
        // Ensure only the G12 Transcript is being cleared
        if (existingG12TranscriptName != null) {
            temporalApplicantService.removeG12Transcript(idNumber);
            existingG12TranscriptName = null; // Explicitly reset the state
            g12Transcript = null;             // Explicitly reset the file part
        }
        // Ensure ID Document state is unaffected
        existingIdDocName = existingIdDocName;
    }


    // Getters and Setters

    public Part getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(Part idDocument) {
        this.idDocument = idDocument;
    }

    public Part getG12Transcript() {
        return g12Transcript;
    }

    public void setG12Transcript(Part g12Transcript) {
        this.g12Transcript = g12Transcript;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    
    public String getExistingIdDocName() {
    return existingIdDocName;
    }

    public void setExistingIdDocName(String existingIdDocName) {
        this.existingIdDocName = existingIdDocName;
    }

    public String getExistingG12TranscriptName() {
        return existingG12TranscriptName;
    }

    public void setExistingG12TranscriptName(String existingG12TranscriptName) {
        this.existingG12TranscriptName = existingG12TranscriptName;
    }
}
