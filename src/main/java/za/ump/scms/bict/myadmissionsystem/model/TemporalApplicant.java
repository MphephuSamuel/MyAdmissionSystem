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
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author mphep
 */
@Entity
@Table(name="TEMPORAL_APPLICANT")
public class TemporalApplicant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TEMPORAL_APPLICANT_ID")
    private Long id;
    
    @Column(name="ID_NUMBER", nullable=false, unique=true)
    private String idNumber;
    
    @Column(name="APPLICATION_DATA", columnDefinition="TEXT")
    private String applicationData;
    
    @Column(name="LAST_UPDATED")
    private LocalDate lastUpdated;

      // === ID Document Fields ===
    @Lob
    @Column(name = "ID_DOCUMENT_DATA")
    private byte[] idDocumentData;

    @Column(name = "ID_DOCUMENT_FILENAME")
    private String idDocumentFilename;

    @Column(name = "ID_DOCUMENT_FILETYPE")
    private String idDocumentFileType;

    // === G12 Transcript Fields ===
    @Lob
    @Column(name = "G12_TRANSCRIPT_DATA")
    private byte[] g12TranscriptData;

    @Column(name = "G12_TRANSCRIPT_FILENAME")
    private String g12TranscriptFilename;

    @Column(name = "G12_TRANSCRIPT_FILETYPE")
    private String g12TranscriptFileType;

    
    public TemporalApplicant() {
    }

    public TemporalApplicant(String idNumber, String applicationData, LocalDate lastUpdated) {
        this.idNumber = idNumber;
        this.applicationData = applicationData;
        this.lastUpdated = lastUpdated;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getApplicationData() {
        return applicationData;
    }

    public void setApplicationData(String applicationData) {
        this.applicationData = applicationData;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof TemporalApplicant)) {
            return false;
        }
        TemporalApplicant other = (TemporalApplicant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.ump.scms.bict.myadmissionsystem.model.ApplicationSession[ id=" + id + " ]";
    }
    
}
