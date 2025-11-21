/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import za.ump.scms.bict.myadmissionsystem.model.Admin;
import za.ump.scms.bict.myadmissionsystem.repository.AdminRepository;

/**
 *
 * @author mphep
 */
@Stateless
public class AdminService {
    @Inject
    private AdminRepository adminRepository;
    
    public String saveAdmin(Admin admin){
        adminRepository.saveAdmin(admin);
        return "";
    }
}
