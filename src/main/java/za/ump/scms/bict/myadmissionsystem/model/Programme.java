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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mphep
 */
@Entity
@Table(name="PROGRAMME")
public class Programme implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PROGRAMME_ID")
    private Long id;

    @Column(name="NAME")
    private String name;
    
    @Column(name="MIN_APS")
    private int minAps;
    
    @Column(name="MAX_CAPACITY")
    private int maxCapacity;
    
    @OneToMany(mappedBy = "programme", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Application> applications;
    
    @OneToOne(mappedBy = "programme", cascade=CascadeType.ALL, orphanRemoval=true)
    private ProgrammeLeader programmeLeader;

    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequiredSubject> requiredSubjects;
    
    public Programme() {
    }

    public Programme(String name, int minAps, int maxCapacity) {
        this.name = name;
        this.minAps = minAps;
        this.maxCapacity=maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinAps() {
        return minAps;
    }

    public void setMinAps(int minAps) {
        this.minAps = minAps;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public ProgrammeLeader getProgrammeLeader() {
        return programmeLeader;
    }

    public void setProgrammeLeader(ProgrammeLeader programmeLeader) {
        this.programmeLeader = programmeLeader;
    }

    public List<RequiredSubject> getRequiredSubjects() {
        return requiredSubjects;
    }

    public void setRequiredSubjects(List<RequiredSubject> requiredSubjects) {
        this.requiredSubjects = requiredSubjects;
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
        if (!(object instanceof Programme)) {
            return false;
        }
        Programme other = (Programme) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.ump.scms.bict.myadmissionsystem.model.Programme[ id=" + id + " ]";
    }
    
}
