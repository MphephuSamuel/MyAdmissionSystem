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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mphep
 */
// existing package and imports remain unchanged

@Entity
@Table(name="APPLICATION")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="APPLICATION_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="APPLICANT_ID", nullable=false)
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name="PROGRAMME_ID", nullable=false)
    private Programme programme;

    @Column(name="CHOICE_NUMBER")
    private int choiceNumber;

    @Temporal(TemporalType.DATE)
    @Column(name="APPLICATION_DATE")
    private Date applicationDate;

    @Column(name="APPLICATION_STATUS")
    private String applicationStatus;

    public Application() {}

    public Application(Applicant applicant, Programme programme, int choiceNumber, Date applicationDate, String applicationStatus) {
        this.applicant = applicant;
        this.programme = programme;
        this.choiceNumber = choiceNumber;
        this.applicationDate = applicationDate;
        this.applicationStatus = applicationStatus;
    }

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

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public int getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(int choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    // ✅ Convenience method
    public String getStatus() {
        return getApplicationStatus();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Application)) {
            return false;
        }
        Application other = (Application) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "za.ump.scms.bict.myadmissionsystem.model.Application[ id=" + id + " ]";
    }
}
