/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;

/**
 *
 * @author mphep
 */
@Entity
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="DOCUMENT_NAME")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="APPLICANT_ID")
    private Applicant applicant;
    
    // === ID Document Fields ===
    @Lob
    @Column(name = "ID_DOCUMENT_DATA")
    private byte[] idDocumentData;

    @Column(name = "ID_DOCUMENT_FILENAME")
    private String idDocumentFilename;

    @Column(name = "ID_DOCUMENT_FILETYPE")
    private String idDocumentFileType;

    public Document(Applicant applicant, byte[] idDocumentData, String idDocumentFilename, String idDocumentFileType, byte[] g12TranscriptData, String g12TranscriptFilename, String g12TranscriptFileType) {
        this.applicant = applicant;
        this.idDocumentData = idDocumentData;
        this.idDocumentFilename = idDocumentFilename;
        this.idDocumentFileType = idDocumentFileType;
        this.g12TranscriptData = g12TranscriptData;
        this.g12TranscriptFilename = g12TranscriptFilename;
        this.g12TranscriptFileType = g12TranscriptFileType;
    }

    public Document(){
        
    }
    
    // === G12 Transcript Fields ===
    @Lob
    @Column(name = "G12_TRANSCRIPT_DATA")
    private byte[] g12TranscriptData;

    @Column(name = "G12_TRANSCRIPT_FILENAME")
    private String g12TranscriptFilename;

    @Column(name = "G12_TRANSCRIPT_FILETYPE")
    private String g12TranscriptFileType;
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public byte[] getIdDocumentData() {
        return idDocumentData;
    }

    public void setIdDocumentData(byte[] idDocumentData) {
        this.idDocumentData = idDocumentData;
    }

    public String getIdDocumentFilename() {
        return idDocumentFilename;
    }

    public void setIdDocumentFilename(String idDocumentFilename) {
        this.idDocumentFilename = idDocumentFilename;
    }

    public String getIdDocumentFileType() {
        return idDocumentFileType;
    }

    public void setIdDocumentFileType(String idDocumentFileType) {
        this.idDocumentFileType = idDocumentFileType;
    }

    public byte[] getG12TranscriptData() {
        return g12TranscriptData;
    }

    public void setG12TranscriptData(byte[] g12TranscriptData) {
        this.g12TranscriptData = g12TranscriptData;
    }

    public String getG12TranscriptFilename() {
        return g12TranscriptFilename;
    }

    public void setG12TranscriptFilename(String g12TranscriptFilename) {
        this.g12TranscriptFilename = g12TranscriptFilename;
    }

    public String getG12TranscriptFileType() {
        return g12TranscriptFileType;
    }

    public void setG12TranscriptFileType(String g12TranscriptFileType) {
        this.g12TranscriptFileType = g12TranscriptFileType;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.ump.scms.bict.myadmissionsystem.model.Document[ id=" + id + " ]";
    }
    
}
