/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDate;
import za.ump.scms.bict.myadmissionsystem.model.TemporalApplicant;
import za.ump.scms.bict.myadmissionsystem.repository.TemporalApplicantRepository;

/**
 *
 * @author mphep
 */
@Stateless
public class TemporalApplicantService implements Serializable {

    @Inject
    private TemporalApplicantRepository temporalApplicantRepository;

    public void saveTemporalApplicant(String idNumber, String applicationData) {
        TemporalApplicant existing = temporalApplicantRepository.getTemporalApplicantByIdNumber(idNumber);

        if (existing != null) {
            // Update existing
            existing.setApplicationData(applicationData);
            existing.setLastUpdated(LocalDate.now());
            temporalApplicantRepository.saveTemporalApplicant(existing);
        } else {
            // Save new
            TemporalApplicant temporalApplicant = new TemporalApplicant(idNumber, applicationData, LocalDate.now());
            temporalApplicantRepository.saveTemporalApplicant(temporalApplicant);
        }
    }

    public TemporalApplicant findTemporalApplicantByIdNumber(String idNumber) {
        return temporalApplicantRepository.getTemporalApplicantByIdNumber(idNumber);
    }

    public void deleteTemporalApplicantByIdNumber(String idNumber) {
        temporalApplicantRepository.deleteTemporalApplicantByIdNumber(idNumber);
    }

    public void updateApplicantDocuments(String idNumber, byte[] idDocData, String idDocFileName, String idDocType,
                                     byte[] g12Data, String g12FileName, String g12Type) {
    TemporalApplicant applicant = temporalApplicantRepository.getTemporalApplicantByIdNumber(idNumber);
    if (applicant != null) {
        System.out.println("Found applicant: " + applicant.getIdNumber());

        // Update ID document only if it's not null
        if (idDocData != null && idDocFileName != null && idDocType != null) {
            applicant.setIdDocumentData(idDocData);
            applicant.setIdDocumentFilename(idDocFileName);
            applicant.setIdDocumentFileType(idDocType);
        }

        // Update G12 transcript only if it's not null
        if (g12Data != null && g12FileName != null && g12Type != null) {
            applicant.setG12TranscriptData(g12Data);
            applicant.setG12TranscriptFilename(g12FileName);
            applicant.setG12TranscriptFileType(g12Type);
        }

        // Update the last updated timestamp
        applicant.setLastUpdated(LocalDate.now());

        // Save the applicant (only the fields that were updated)
        temporalApplicantRepository.saveTemporalApplicant(applicant);

        System.out.println("Updated applicant documents successfully.");
    } else {
        System.out.println("Applicant not found for ID number: " + idNumber);
    }
}

    
    public void removeIdDocument(String idNumber) {
    TemporalApplicant applicant = temporalApplicantRepository.getTemporalApplicantByIdNumber(idNumber);
    if (applicant != null) {
        applicant.setIdDocumentData(null);  // Ensure only the ID document data is nullified
        applicant.setIdDocumentFilename(null);  // Ensure only the filename is cleared
        applicant.setIdDocumentFileType(null);  // Ensure only the file type is cleared
        applicant.setLastUpdated(LocalDate.now());  // Update the last updated timestamp
        temporalApplicantRepository.saveTemporalApplicant(applicant);  // Save the changes
    }
}


    public void removeG12Transcript(String idNumber) {
    TemporalApplicant applicant = temporalApplicantRepository.getTemporalApplicantByIdNumber(idNumber);
    if (applicant != null) {
        applicant.setG12TranscriptData(null);  // Ensure only the G12 transcript data is nullified
        applicant.setG12TranscriptFilename(null);  // Ensure only the filename is cleared
        applicant.setG12TranscriptFileType(null);  // Ensure only the file type is cleared
        applicant.setLastUpdated(LocalDate.now());  // Update the last updated timestamp
        temporalApplicantRepository.saveTemporalApplicant(applicant);  // Save the changes
    }
}

}
