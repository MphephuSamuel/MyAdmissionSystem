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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author mphep
 */
@Entity
@Table(name="MARK")
public class Mark implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MARK_ID") 
    private Long id;

    @ManyToOne
    @JoinColumn(name="SUBJECT_ID", nullable=false)
    private Subject subject;
    
    @ManyToOne
    @JoinColumn(name="APPLICANT_ID")
    private Applicant applicant;
    
    @Column(name = "GRADE_LEVEL")
    private int gradeLevel;
    
    @Column(name = "PERCENTAGE")
    private int percentage;
    
    @Column(name = "LEVEL")
    private int level;
    
    @Column(name = "EXAM_YEAR")
    private int examYear;

    public Mark() {
    }

    public Mark(Subject subject, Applicant applicant, int gradeLevel, int percentage, int level, int examYear) {
        this.subject = subject;
        this.applicant = applicant;
        this.gradeLevel = gradeLevel;
        this.percentage = percentage;
        this.level = level;
        this.examYear = examYear;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExamYear() {
        return examYear;
    }

    public void setExamYear(int examYear) {
        this.examYear = examYear;
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
        if (!(object instanceof Mark)) {
            return false;
        }
        Mark other = (Mark) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.ump.scms.bict.myadmissionsystem.model.Mark[ id=" + id + " ]";
    }
    
}
