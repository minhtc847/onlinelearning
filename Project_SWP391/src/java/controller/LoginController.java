/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.DAOUsers;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.users;
import model.users_roles;
import static utils.Constans.LinkYouTube;
import static utils.Constans.LinkYouTubeEmbed;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="LoginController", urlPatterns={"/login"})
public class LoginController extends HttpServlet {
   
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        DAOUsers dao = new DAOUsers();
        
        users acc = dao.login(email, password);
        if (acc == null){
            request.setAttribute("mess", "Wrong email or pass");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }else{
            HttpSession session = request.getSession();
            users u = dao.getUserbyEmail(email);
            users_roles userRole = dao.getRoleByUserID(acc.getUser_id());
            System.out.println(userRole.toString());
            session.setAttribute("userRole", userRole);
            session.setAttribute("acc", acc);
            session.setAttribute("e", email);
            session.setAttribute("LinkYouTubeEmbed", LinkYouTubeEmbed);
            System.out.println(userRole.toString());
            response.sendRedirect("Home");
            
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
