/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OtherPackages;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mutemi
 */
public class VerifyRequest 
{
    private Boolean IsRequestValid;
    private String CookieIs;
    
    
    public VerifyRequest(HttpServletRequest SentRequestIs,String VerifyURLIs,String Cookiewas) throws IOException, Exception
    {
        //get request get or post values and names
        GetAllRequestVariables TheVariablesAre=new GetAllRequestVariables(SentRequestIs,"[AND]");//use [AND] instead so values are not cut
        String VariablesAre=TheVariablesAre.RequestVriablesAre();//get params
        
        String APIKeyIs = SentRequestIs.getHeader("Authorization");//get authorization header
        String ContentLength = String.valueOf(SentRequestIs.getContentLength());//length
        String RefererIS = SentRequestIs.getHeader("Origin");
        String RequestMethod = SentRequestIs.getMethod();
        String ScriptName = SentRequestIs.getRequestURI();
        String ServerName = SentRequestIs.getServerName();
        String RemoteAddress = SentRequestIs.getRemoteAddr();
        String RemotePort = String.valueOf( SentRequestIs.getServerPort() );
        String ServerProtocol = SentRequestIs.getProtocol();
        String ServerInfo = String.valueOf( SentRequestIs.getServletContext().getServerInfo());
        String TimeStampIs=String.valueOf( System.currentTimeMillis()/1000 );
        
        
            //make string
            String StringVarsAre="content_length="+ContentLength+
                                        "&query_string="+VariablesAre+
                                        "&referer="+RefererIS+
                                        "&request_method="+RequestMethod+
                                        "&script_name="+ScriptName+
                                        "&server_name="+ServerName+
                                        "&remote_addr="+RemoteAddress+
                                        "&server_port="+RemotePort+
                                        "&server_protocol="+ServerProtocol+
                                        "&server_info="+ServerInfo+
                                        "&request_time_stamp="+TimeStampIs;
           //do request       
           SendPostRequest MyPost=new SendPostRequest(VerifyURLIs,APIKeyIs,Cookiewas,StringVarsAre);    
           String MyPostStringIs=MyPost.FeedbackJsonIs();
           
           JSONObject jsonobj=new JSONObject(MyPostStringIs);//make jsonobject from jsonstring
           IsRequestValid=jsonobj.getBoolean("check");//get check
           CookieIs=jsonobj.getString("Set-Cookie");//get cookie
         
           
           //System.out.println(IsRequestValid+"=="+CookieIs);
          
           /*
        System.out.println(APIKeyIs);
        System.out.println(ContentLength);
        System.out.println(VariablesAre);
        System.out.println(RefererIS);
        System.out.println(RequestMethod);
        System.out.println(ScriptName);
        System.out.println(ServerName);
        System.out.println(RemoteAddress);
        System.out.println(RemotePort);
        System.out.println(ServerProtocol);
        System.out.println(ServerInfo);
        System.out.println(TimeStampIs);
                   */
    }
    
    public Boolean IsRequestValid()
    {
        return IsRequestValid;
    }
    
    public String CookieIs()
    {
        return CookieIs;
    }
}
