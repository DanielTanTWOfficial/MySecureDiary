package example.com.mysecurediary;

import java.security.MessageDigest;

/**
 * Created by teckw on 8/1/2017.
 */

public class HashData {
    public String SHA_512Hash(String dataToHash, String salt){
        String generatedData = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(dataToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedData = sb.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return generatedData;
    }
}
