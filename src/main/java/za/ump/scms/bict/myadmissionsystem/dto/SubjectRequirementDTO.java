/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.dto;

/**
 *
 * @author mphep
 */
public class SubjectRequirementDTO {
    private Long subjectId;
    private String name;
    private int requiredLevel;

    public SubjectRequirementDTO(Long subjectId, String name, int requiredLevel) {
        this.subjectId = subjectId;
        this.name = name;
        this.requiredLevel = requiredLevel;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }
    
    
}
