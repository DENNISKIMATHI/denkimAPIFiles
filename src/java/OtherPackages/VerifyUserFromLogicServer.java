/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OtherPackages;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author mutemi
 */
public class VerifyUserFromLogicServer 
{
    private Document ReturnDocument = new Document(); 
    
    public VerifyUserFromLogicServer(HttpServletRequest SentRequestIs,String SessionIs,String VerifyURLIs) throws MalformedURLException, ProtocolException, IOException, JSONException
    {
        String APIKeyIs = SentRequestIs.getHeader("Authorization");//get authorization header
        String RefererIS = SentRequestIs.getHeader("Origin");
        String CookieIs = SentRequestIs.getHeader("Cookie");
        
        String urlParameters="session_key="+SessionIs;
        
        //String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(VerifyURLIs);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", APIKeyIs);
                con.setRequestProperty("Cookie", CookieIs);
                con.setRequestProperty("Origin", RefererIS);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		//int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Post parameters : " + urlParameters);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) 
                {
			response.append(inputLine);
		}
		in.close();

		//print result
		//System.out.println(response.toString());
                
                
                String FeedbackJsonIs=response.toString();
                
                JSONObject jsonobj=new JSONObject(FeedbackJsonIs);//make jsonobject from jsonstring
                ReturnDocument.append("check", jsonobj.getBoolean("check"));
                ReturnDocument.append("full_names", jsonobj.getString("full_names"));
                ReturnDocument.append("email_address", jsonobj.getString("email_address"));
                ReturnDocument.append("phone_number", jsonobj.getString("phone_number"));
                ReturnDocument.append("national_id", jsonobj.getString("national_id"));
                
    }
    
    public Document ReturnDocument()
    {
        return ReturnDocument;
    }
}
