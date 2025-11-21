package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import za.ump.scms.bict.myadmissionsystem.model.Applicant;
import za.ump.scms.bict.myadmissionsystem.model.Document;
import za.ump.scms.bict.myadmissionsystem.repository.DocumentRepository;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mphep
 */
@Stateless
public class DocumentService {
    
    @Inject
    private DocumentRepository documentRepository;
    
    @Inject
    private ApplicantService applicantService;
    

    
    public void saveDocument( Long applicantId, byte[] idDocData, String idDocFileName, String idDocType,
                                     byte[] g12Data, String g12FileName, String g12Type){
        
        Applicant  applicant = applicantService.getApplicantByApplicantId(applicantId);
        if (applicant!=null && g12Data != null && g12FileName != null && g12Type != null) {
                Document document = new Document(applicant, idDocData,  idDocFileName, idDocType, g12Data, g12FileName, g12Type);
                documentRepository.saveDocument(document);
            }
    }
    
    public Document getDocumentByApplicantId(Long applicantId) {
        return documentRepository.findByApplicantId(applicantId);
    }
}

