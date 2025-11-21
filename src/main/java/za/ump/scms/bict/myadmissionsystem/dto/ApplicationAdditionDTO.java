/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.dto;

/**
 *
 * @author mphep
 */
public class ApplicationAdditionDTO {
    private int choiceNumber;
    private Long subjectId;

    public ApplicationAdditionDTO() {
    }

    public ApplicationAdditionDTO(int choiceNumber, Long subjectId) {
        this.choiceNumber = choiceNumber;
        this.subjectId = subjectId;
    }

    public int getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(int choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
