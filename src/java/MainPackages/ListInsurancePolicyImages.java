/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackages;

import OtherPackages.VerifyUserFromLogicServer;
import OtherPackages.FetchDirectories;
import OtherPackages.FetchFiles;
import OtherPackages.MakeMD5String;
import static com.mongodb.util.JSON.serialize;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
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
                                List<Document> files_definitions=new ArrayList<>();
                                
                                String is_policy_image="policy_image";
                                //make md5 email
                                MakeMD5String md5_object=new MakeMD5String(is_policy_image);
                                String is_policy_image_md5=md5_object.Md5StringIs();
                                
                                //do the listing
                                String UserDir=UPLOAD_DIR_NAME+"/"+is_policy_image_md5;//dir name
                                FetchDirectories fetch_dir_object=new FetchDirectories(UserDir);
                                
                                List<Document> DirectoriesAre=fetch_dir_object.DirectoriesAre();
                                
                                Integer size_of_dir=DirectoriesAre.size();
                                
                                if(size_of_dir>0)//check if empty
                                {
                                        for (int i = 0; i < size_of_dir; i++) 
                                        {
                                            Document directory_is = DirectoriesAre.get(i); 
                                            String dir_name=directory_is.getString("name");
                                            String dir_absolute_path=directory_is.getString("absolute_path");
                                            
                                           
                                            if(!dir_name.equalsIgnoreCase("WEB-INF") && !dir_name.equalsIgnoreCase("META-INF"))
                                            {
                                                            //list files
                                                            FetchFiles fetch_files_object=new FetchFiles(dir_absolute_path);

                                                            List<Document> fetch_files=fetch_files_object.FilesAre();

                                                            Integer size_of_files=fetch_files.size();
                                                            
                                                             

                                                            for (int j = 0; j < size_of_files; j++) 
                                                            {
                                                                    Document file_is = fetch_files.get(j); 
                                                                    String file_name=file_is.getString("name");
                                                                    //String file_absolute_path=file_is.getString("absolute_path");
                                                                    Object last_modified=file_is.get("last_modified");
                                                                    Object size=file_is.get("size");
                                                                    
                                                                    //link
                                                                    String sharable_link=FILE_SERVER_BASE_URL+is_policy_image_md5+"/"+dir_name+"/"+file_name;
                                                                    
                                                                   
                                                                            Document file_details = new Document(); 
                                                                            file_details.append("name", file_name);
                                                                            file_details.append("last_modified", last_modified);
                                                                            file_details.append("link", sharable_link);
                                                                            file_details.append("size", size);

                                                                            files_definitions.add(file_details);
                                                                    

                                                                            
                                                            }
                                            } 
                                        }
                                    
                                    
                                    return_document.append("check",true );
                                    return_document.append("message", files_definitions);
                                
                                }
                                else
                                {
                                    //set message
                                    return_document.append("check",false );
                                    return_document.append("message", "No images uploaded!");
                                }
                                
                                
                                    
                                
                                
                            }
                            else
                            {
                                Boolean check=false;
                                //set message
                                return_document.append("check",check );
                                return_document.append("message", "Your session has expired, please log in again");
                                
                            }
                        } 
                        catch (MalformedURLException ex) 
                        {
                            //Logger.getLogger(UploadFiles.class.getName()).log(Level.SEVERE, null, ex);
                            Boolean check=false;
                            //set message
                            return_document.append("check",check );
                            return_document.append("message", "Ops, something went wrong");
                            
                        } 
                        catch (ProtocolException ex) 
                        {
                            //Logger.getLogger(UploadFiles.class.getName()).log(Level.SEVERE, null, ex);
                            Boolean check=false;
                            //set message
                            return_document.append("check",check );
                            return_document.append("message", "Ops!, something went wrong");
                           
                        } 
                        catch (JSONException ex) 
                        {
                            //Logger.getLogger(UploadFiles.class.getName()).log(Level.SEVERE, null, ex);
                            Boolean check=false;
                            //set message
                            return_document.append("check",check );
                            return_document.append("message", "Oh my!, something went wrong");
                            
                        }
                }
                else
                {
                        Boolean check=false;
                        //set message
                        return_document.append("check",check );
                        return_document.append("message", "Please fill in the required fields");
                        
                    
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
