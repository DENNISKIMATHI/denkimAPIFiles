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
public class FetchDirectories 
{
    List<Document> DirectoriesAre = new  ArrayList<>(); 
    
    public FetchDirectories(String UserDir)
    {
        File root = new File( UserDir );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) 
        {
            if ( f.isDirectory() ) 
            {
                Document directory_is = new Document(); 
                
                directory_is.append("name", f.getName());
                directory_is.append("absolute_path", f.getAbsolutePath());
                
                DirectoriesAre.add(directory_is);
                //this( f.getAbsolutePath() );
                //System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else 
            {
               // System.out.println( "File:" + f.getAbsoluteFile() );
            }
        }
    }
    
    public List<Document> DirectoriesAre()
    {
        return DirectoriesAre;
    }
}
