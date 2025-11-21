/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.controller;


import at.favre.lib.crypto.bcrypt.BCrypt;   
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import za.ump.scms.bict.myadmissionsystem.resources.login.LoginRouter;
import za.ump.scms.bict.myadmissionsystem.service.UserService;

/**
 *
 * @author mphep
 */
@SessionScoped
@Named
public class UserBean implements Serializable{
    
    private String username;
    private String password;
    private String role;
    
    @Inject
    private UserService userService;
    
    @Inject
    private LoginRouter loginRouter;
    
    public String signup(){
        role = loginRouter.getSelectedRole();
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        userService.userRegister(username, hashedPassword, role);
        
        return null;
    }
    
    public String login(){
        
        role=loginRouter.getSelectedRole();
        String userRole = userService.authenticate(username, password, role);
        System.out.println("username "  + username + " \npassword " + password + " \nrole " + role);
        if(userRole!=null){
            if ("admin".equals(userRole)) {
            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("role", userRole);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", username);

                return "admin/admin.xhtml?faces-redirect=true";
            }
            
            else if ("student".equals(userRole)){
                FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("role", userRole);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", username);
                return "student-dashboard.xhtml?faces-redirect=true";
            }
            else if("programmeLeader".equals(userRole)){
                System.out.println("programme-leader login touched");
                FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("role", userRole);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", username);
                return "programme-leader/programme-leader-dashboard.xhtml?faces-redirect=true" + System.currentTimeMillis();
            }
        }
            FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage("Invalid login or not an admin."));
            return "login.xhtml?faces-redirect=true&error=true";
    }
    
    public void redirectIfNotLoggedIn(String pageOwner) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String role = (String) facesContext.getExternalContext().getSessionMap().get("role");

        if (role == null || !pageOwner.equals(role)) {
            try {
                String contextPath = facesContext.getExternalContext().getRequestContextPath();
                facesContext.getExternalContext().redirect(contextPath + "/login.xhtml?error=true");
            } catch (IOException e) {
                e.printStackTrace(); 
            }
        }
    }
    
    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
    
    public void redirectIfNotLoggedInAsStudent() {
        redirectIfNotLoggedIn("student");
    }
    
    public void redirectIfNotLoggedInAsAdmin() {
        redirectIfNotLoggedIn("admin");
    }
    
    public void redirectIfNotLoggedInAsProgrammeLeader() {
    redirectIfNotLoggedIn("programmeLeader");
}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
