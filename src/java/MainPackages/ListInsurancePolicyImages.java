/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackages;

import OtherPackages.VerifyUserFromLogicServer;
import static com.mongodb.util.JSON.serialize;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.json.JSONException;

/**
 *
 * @author mutemi
 */
public class ListInsurancePolicyImages extends HttpServlet 
{
    //public static String AUTHORIZATION_URL_PARAM_NAME="authorization_url";
    public static String LOGIC_SERVER_BASE_URL_PARAM_NAME="logic_server_base_url";
    public static String FILE_SERVER_BASE_URL_PARAM_NAME="file_server_base_url";
    public static String LOGIC_SERVER_VERIFY_USER_PARAM_NAME="logic_server_verify_user_url";
    public static String UPLOAD_DIR_NAME_PARAM_NAME="upload_dir";
    
   // public String AUTHORIZATION_URL;
    public String LOGIC_SERVER_BASE_URL;
    public String FILE_SERVER_BASE_URL;
    public String LOGIC_SERVER_VERIFY_USER;
    public String UPLOAD_DIR_NAME;
    
        private int image_max_file_size = 7000 * 1024;//bytes for image
        private String[] image_acceptable_content_types=new String[]{"image/jpeg","image/jpg","image/png","image/bmp","image/gif"};//for image
        private String[] image_acceptable_extensions=new String[]{"jpg","jpeg","png","bmp","gif"};//for image
    
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
        
        //AUTHORIZATION_URL=Configuration.getServletContext().getInitParameter(AUTHORIZATION_URL_PARAM_NAME);//check web.xml on how to set these
        LOGIC_SERVER_BASE_URL=Configuration.getServletContext().getInitParameter(LOGIC_SERVER_BASE_URL_PARAM_NAME);//check web.xml on how to set these
        FILE_SERVER_BASE_URL=Configuration.getServletContext().getInitParameter(FILE_SERVER_BASE_URL_PARAM_NAME);//check web.xml on how to set these
        LOGIC_SERVER_VERIFY_USER=Configuration.getServletContext().getInitParameter(LOGIC_SERVER_VERIFY_USER_PARAM_NAME);//check web.xml on how to set these
        UPLOAD_DIR_NAME=Configuration.getServletContext().getInitParameter(UPLOAD_DIR_NAME_PARAM_NAME);//check web.xml on how to set these
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
        //get parameters
        String session_key=request.getParameter("session_key");
        
        Document return_document = new Document(); 
                if(!session_key.isEmpty())
                {
                        try 
                        {
                            String VerifyURLIs=LOGIC_SERVER_BASE_URL+LOGIC_SERVER_VERIFY_USER;
                            //VERIFY info
                            VerifyUserFromLogicServer MyVerifyObj= new VerifyUserFromLogicServer(request, session_key, VerifyURLIs);
                            Document ReturnDocumentIs=MyVerifyObj.ReturnDocument();

                           // System.out.println(ReturnDocumentIs);
                            //check and do upload
                            Boolean IsVerifyValid=ReturnDocumentIs.getBoolean("check");
                            //String email_address_is=ReturnDocumentIs.getString("email_address");

                            if(IsVerifyValid==true)
                            {
                                //do the listing
                                
                                //set message
                                return_document.append("check",true );
                                 return_document.append("message", "hhaha");
                            }
                            else
                            {
                                Boolean check=false;
                                //set message
                                return_document.append("check",check );
                                return_document.append("message", "Your session has expired, please log in again");
                                return_document.append("file", check);
                            }
                        } 
                        catch (MalformedURLException ex) 
                        {
                            //Logger.getLogger(UploadFiles.class.getName()).log(Level.SEVERE, null, ex);
                            Boolean check=false;
                            //set message
                            return_document.append("check",check );
                            return_document.append("message", "Ops, something went wrong");
                            return_document.append("file", check);
                        } 
                        catch (ProtocolException ex) 
                        {
                            //Logger.getLogger(UploadFiles.class.getName()).log(Level.SEVERE, null, ex);
                            Boolean check=false;
                            //set message
                            return_document.append("check",check );
                            return_document.append("message", "Ops!, something went wrong");
                            return_document.append("file", check);
                        } 
                        catch (JSONException ex) 
                        {
                            //Logger.getLogger(UploadFiles.class.getName()).log(Level.SEVERE, null, ex);
                            Boolean check=false;
                            //set message
                            return_document.append("check",check );
                            return_document.append("message", "Oh my!, something went wrong");
                            return_document.append("file", check);
                        }
                }
                else
                {
                        Boolean check=false;
                        //set message
                        return_document.append("check",check );
                        return_document.append("message", "Please fill in the required fields");
                        return_document.append("file", check);
                    
                }
                
                String json_object= serialize(return_document);//make into json object
                response.setContentType("application/json;charset=UTF-8");
                
                        try (PrintWriter out = response.getWriter()) 
                        {  
                            out.println(json_object);
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
