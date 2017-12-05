/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OtherPackages;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mutemi
 */
public class GetAllRequestVariables 
{
    private String RequestVriablesAre;
    private int ContentLength;
    
    public GetAllRequestVariables(HttpServletRequest SentRequestIs,String Separator)
    {
        Enumeration get_var_names;
        get_var_names=SentRequestIs.getParameterNames();
        
        ContentLength=0;//intialize
        
            //loop through the enumaration
            int Counter=0;//count so to know how to attach
            while (get_var_names.hasMoreElements()) 
            {//start of while get_var_names.hasMoreElements()
                
                //get name
                String get_var_name=(String) get_var_names.nextElement();//type cas to string
               
                
                String[] get_var_vals = SentRequestIs.getParameterValues(get_var_name);
                
                if(get_var_vals.length==0)//if array has 0 values
                {
                    //System.out.println(get_var_name+": "+"NULL");
                    
                }
                else if(get_var_vals.length==1)//if array has one value
                {
                    
                    String get_var_val=get_var_vals[0];
                    //System.out.println(get_var_name+": "+get_var_val);
                    if(Counter==0)//if at first item just add do not concatnate
                    {
                        get_var_val=get_var_name.equalsIgnoreCase("password")? "*****" :get_var_val;//if is password replace
                        RequestVriablesAre=get_var_name+"="+get_var_val;//aggregate to main stirng
                        ContentLength+=get_var_name.length()+get_var_name.length();//aggregate length
                    }
                    else
                    {
                        get_var_val=get_var_name.equalsIgnoreCase("password")? "*****" :get_var_val;//if is password replace
                        RequestVriablesAre+=Separator+""+get_var_name+"="+get_var_val;//aggregate to main stirng
                        ContentLength+=get_var_name.length()+get_var_val.length();//aggregate length
                    }
                    
                    
                    Counter++;//increment counter
                }
                else// if array has more than one value
                {
                    for (int i = 0; i < get_var_vals.length; i++) 
                    {
                         //System.out.println(get_var_name+": "+get_var_vals[i]);
                        if(Counter==0)//if at first item just add do not concatnate
                        {
                            get_var_vals[i]=get_var_name.equalsIgnoreCase("password")? "*****" :get_var_vals[i];//if is password replace
                            RequestVriablesAre=get_var_name+"="+get_var_vals[i];//aggregate to main stirng
                            ContentLength+=get_var_name.length()+get_var_vals[i].length();//aggregate length
                        }
                        else
                        {
                            get_var_vals[i]=get_var_name.equalsIgnoreCase("password")? "*****" :get_var_vals[i];//if is password replace
                            RequestVriablesAre+=Separator+""+get_var_name+"="+get_var_vals[i];//aggregate to main stirng
                            ContentLength+=get_var_name.length()+get_var_vals[i].length();//aggregate length
                        }
                          
                          
                          Counter++;//increment counter
                    }
                }
            }//end of while get_var_names.hasMoreElements()
        
    }
    
    public String RequestVriablesAre()
    {
        return RequestVriablesAre;
    }
    
    public int ContentLength()
    {
        return ContentLength;
    }
}
