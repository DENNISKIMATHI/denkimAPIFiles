/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackages;

import OtherPackages.VerifyRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mutemi
 */
public class Login extends HttpServlet 
{
    public static String AUTHORIZATION_URL_PARAM_NAME="authorization_url";
    
    public String AUTHORIZATION_URL;
    
     /**
     *
     * @param Configuration
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig Configuration) throws ServletException
    {
        //call the super class
        super.init(Configuration);
        
        AUTHORIZATION_URL=Configuration.getServletContext().getInitParameter(AUTHORIZATION_URL_PARAM_NAME);//check web.xml on how to set these
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        try 
        {
            String Cookiewas="JSESSIONID=20FDCA5AB1E20BC7E72E2FC9298A4D25";//precached cookie
            VerifyRequest MyVerification=new VerifyRequest(request, AUTHORIZATION_URL,Cookiewas);
            
            String CookieIs=MyVerification.CookieIs();//cache cookie for future use
            Boolean IsRequestValid=MyVerification.IsRequestValid();
            
            System.out.println(IsRequestValid+"=="+CookieIs);
            
        } catch (Exception ex) 
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() 
    {
        return "Short description";
    }// </editor-fold>

}
