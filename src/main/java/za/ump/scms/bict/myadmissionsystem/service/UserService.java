/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import za.ump.scms.bict.myadmissionsystem.model.Users;
import za.ump.scms.bict.myadmissionsystem.repository.UserRepository;

/**
 *
 * @author mphep
 */
@Stateless
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    
    
    public void userRegister(String username, String password, String role){
        Users user = new Users(username, password, role);
        userRepository.saveUser(user);
    }
    
    public String authenticate(String username, String password, String role){
        Users user = userRepository.findByUsername(username);
        boolean passwordMatch;
        if(user!=null){
            passwordMatch = BCrypt.verifyer()
                .verify(password.toCharArray(), user.getPassword()).verified;
             if(passwordMatch && role.equals(user.getRole())){
                 return role;
             }       
        }
        
        return null;
    }
    
    
}
