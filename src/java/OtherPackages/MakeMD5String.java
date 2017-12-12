/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OtherPackages;

/**
 *
 * @author mutemi
 */
public class MakeMD5String 
{
    private String md5_string;
            
    public MakeMD5String(String input) 
    {
         long r = new java.util.Random().nextLong();
            //String input = String.valueOf(r);
            String md5 = null;

            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                //Update input string in message digest
                digest.update(input.getBytes(), 0, input.length());
                //Converts message digest value in base 16 (hex)
                md5_string = new java.math.BigInteger(1, digest.digest()).toString(16);
            }
            catch (java.security.NoSuchAlgorithmException e) 
            {
                md5_string=null;
            }
            
    }
    
    public String Md5StringIs()
    {
        return md5_string;
    }
}
