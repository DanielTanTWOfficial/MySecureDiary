package example.com.mysecurediary;

import java.security.SecureRandom;

/**
 * Created by teckw on 8/1/2017.
 */

public class GenerateSalt {
    public byte[] getSalt(){
        final SecureRandom r = new SecureRandom();
        byte[] salt = new byte[64];
        r.nextBytes(salt);
        return salt;
    }
}
