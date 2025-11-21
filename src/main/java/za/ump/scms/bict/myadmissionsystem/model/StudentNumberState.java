package za.ump.scms.bict.myadmissionsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_number_state")
public class StudentNumberState {
    
    @Id
    private Long id = 1L; // Singleton row

    private int currentPrefix = 250;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCurrentPrefix() {
        return currentPrefix;
    }

    public void setCurrentPrefix(int currentPrefix) {
        this.currentPrefix = currentPrefix;
    }
}