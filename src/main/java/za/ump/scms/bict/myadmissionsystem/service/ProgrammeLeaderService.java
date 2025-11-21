package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import za.ump.scms.bict.myadmissionsystem.model.Programme;
import za.ump.scms.bict.myadmissionsystem.model.ProgrammeLeader;
import za.ump.scms.bict.myadmissionsystem.repository.ProgrammeLeaderRepository;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mphep
 */
@Stateless
public class ProgrammeLeaderService implements Serializable{
    
    @Inject
    private ProgrammeLeaderRepository programmeLeaderRepository;
    
    
    @Inject
    private ProgrammeService programmeService;
    
    public void saveProgrammeLeader(Long programmeId, String username){
        Programme programme = programmeService.getProgrammeById(programmeId);
        System.out.println(programme.getId());
        ProgrammeLeader programmeLeader = new ProgrammeLeader(programme, username);
        programmeLeaderRepository.saveProgrammeLeader(programmeLeader);
        
    }
    
    public ProgrammeLeader getProgrammeLeaderById(Long programmeLeaderID){
        return programmeLeaderRepository.getProgrammeLeaderById(programmeLeaderID);
    }
    
    public List<ProgrammeLeader> getAllProgrammeLeaders(){
        return programmeLeaderRepository.getAllProgrammeLeaders();      
    }
    
    public ProgrammeLeader getProgrammeLeaderByUsername(String username){
        System.out.println("programme-leader username in the service is " + username );
        return programmeLeaderRepository.getProgrammeLeaderUsername(username);
    }
}
