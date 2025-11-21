/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mphep
 */
@Entity
@Table(name="DECISION")
public class Decision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="DECISION_ID")
    private Long id;
    
    @OneToOne
    @JoinColumn(name="APPLICATION_ID", nullable=false)
    private Application application;
    
    @Column(name="DECISION")
    private String decision;
    
    @Column(name="DECISION_DATE")
    @Temporal(TemporalType.DATE)
    private Date decisionDate;
    
    @Column(name="DECISION_NOTE")
    private String decisionNote;
    
    @OneToOne(mappedBy = "decision", cascade=CascadeType.ALL, orphanRemoval=true)
    private Notification notification;

    public Decision() {
    }

    public Decision(Application application, String decision, Date decisionDate, String decisionNote) {
        this.application = application;
        this.decision = decision;
        this.decisionDate = decisionDate;
        this.decisionNote = decisionNote;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getDecisionNote() {
        return decisionNote;
    }

    public void setDecisionNote(String decisionNote) {
        this.decisionNote = decisionNote;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
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
        if (!(object instanceof Decision)) {
            return false;
        }
        Decision other = (Decision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.ump.scms.bict.myadmissionsystem.model.Decision[ id=" + id + " ]";
    }
    
}
