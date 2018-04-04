/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OtherPackages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author mutemi
 */
public class FetchFiles 
{
    List<Document> FilesAre = new  ArrayList<>(); 
    
    public FetchFiles(String UserDir)
    {
        File root = new File( UserDir );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) 
        {
            if ( f.isFile() ) 
            {
                Document file_is = new Document(); 
                
                file_is.append("name", f.getName());
                file_is.append("absolute_path", f.getAbsolutePath());
                file_is.append("last_modified", f.lastModified());
                file_is.append("size", f.length());
                
                FilesAre.add(file_is);
            }
            else 
            { 
               // System.out.println( "File:" + f.getAbsoluteFile() );
            }
        }
    }
    
    public List<Document> FilesAre()
    {
        return FilesAre;
    }
}
