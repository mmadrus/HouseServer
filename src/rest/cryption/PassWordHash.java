package rest.cryption;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PassWordHash {

    public String generateHash(String PasswordToHash) {
        String generatedPasswordHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(PasswordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPasswordHash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPasswordHash;
    }

}
