/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Programme;
import za.ump.scms.bict.myadmissionsystem.repository.ProgrammeRepository;

/**
 *
 * @author mphep
 */
@Stateless
public class ProgrammeService implements Serializable {
    @Inject
    private ProgrammeRepository programmeRepository;
    
    
    public void saveProgramme(Programme programme){
        programmeRepository.saveProgramme(programme);
    }
    
    public List<Programme> getAllProgrammes(){
        return programmeRepository.getAllProgrammes();
    }
    
    public Programme getProgrammeById(Long programmeId){
        return programmeRepository.getProgrammeById(programmeId);
    }
    
    public Programme findProgrammeByName(String name){
        return programmeRepository.findByName(name);
    }
}
