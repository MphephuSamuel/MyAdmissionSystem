/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ump.scms.bict.myadmissionsystem.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author BLESSING MASUKU
 */
@WebFilter(urlPatterns = {"/applicant/admin-dashboard.xhtml", "/applicant/student-dashboard.xhtml"})
public class LoginFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String role = (session != null) ? (String) session.getAttribute("role") : null;
        String uri = req.getRequestURI();

        if (uri.contains("admin-dashboard.xhtml") && !"admin".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        } else if (uri.contains("student-dashboard.xhtml") && !"student".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        } else if (uri.contains("programme-leader-dashboard.xhtml") && !"programmeLeader".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
        else {
            chain.doFilter(request, response); // let them through
        }
    }
}

