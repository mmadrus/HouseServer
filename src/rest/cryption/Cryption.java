package rest.cryption;



import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Cryption {

    /*
    The encryptor/decryptor class.
    You call the method 'cryption' with parameters String and mode.
    Encrypt the String = Mode 1  (Cipher.ENCRYPT_MODE, kan också använda)
    Decrypt the String = Mode 2 (Cipher.DECRYPT.MODE, kan också använda)


     */

    private static String salt = "ssshhhhhhhhhhh!!!!";



    public static String cryption(String strToCrypt, int mode) {
        String string = "";

        try {

            IvParameterSpec iv = new IvParameterSpec(getIv());
            SecretKeySpec sKeySpec = new SecretKeySpec(generateKeyFromString(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(mode, sKeySpec, iv);
//            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

            switch (mode) {

                case 1:
                    string = Base64.getEncoder().encodeToString(cipher.doFinal(strToCrypt.getBytes("UTF-8")));
                    break;

                case 2:
                    string = new String(cipher.doFinal(Base64.getDecoder().decode(strToCrypt)));

                    break;

            }
            System.out.println("Mode: " + mode);
            System.out.println(string);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    private static byte[] generateKeyFromString() {

        return new PasswordHash().generateHash(salt).substring(0, 16).getBytes();
    }

    private static byte[] getIv() {

        return new byte[]{0x04, 0x02, 0x0f, 0x0a,
                0x08, 0x08, 0x05, 0x0c,
                0x03, 0x01, 0x0e, 0x0f,
                0x08, 0x08, 0x05, 0x0c};

    }

}