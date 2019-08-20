package com.github.phillipkruger.jello.security;

import java.io.IOException;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Test report of all the cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
//@BasicAuthenticationMechanismDefinition(
//    realmName="${'test realm'}" 
//)
//@WebServlet("/report")
//@DeclareRoles({ "user" , "admin"})
//@ServletSecurity(@HttpConstraint(rolesAllowed = "user"))
public class CardReportServlet {//extends HttpServlet {
//
//    @Override @RolesAllowed("user")
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        response.getWriter().write("This is a servlet \n");
//
//        String webName = null;
//        if (request.getUserPrincipal() != null) {
//            webName = request.getUserPrincipal().getName();
//        }
//
//        response.getWriter().write("web username: " + webName + "\n");
//
//        response.getWriter().write("web user has role \"foo\": " + request.isUserInRole("foo") + "\n");
//        response.getWriter().write("web user has role \"bar\": " + request.isUserInRole("bar") + "\n");
//        response.getWriter().write("web user has role \"kaz\": " + request.isUserInRole("kaz") + "\n");
//    }
//    
}
