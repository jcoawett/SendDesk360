package sendDesk360.model.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A helper class for encrypting and decrypting data using AES encryption.
 */
public class EncryptionHelper {

    private static final String SECRET_KEY = "MySuperSecretKey";  // Example key for AES

    /**
     * Encrypts plain text using AES encryption in CBC mode with PKCS5 padding.
     *
     * @param plainText the byte array of the plain text to encrypt
     * @param iv the initialization vector (IV) byte array
     * @return the encrypted byte array (cipher text)
     * @throws Exception if encryption fails
     */
    public byte[] encrypt(byte[] plainText, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(plainText);
    }

    /**
     * Decrypts cipher text using AES encryption in CBC mode with PKCS5 padding.
     *
     * @param cipherText the byte array of the encrypted text
     * @param iv the initialization vector (IV) byte array
     * @return the decrypted byte array (plain text)
     * @throws Exception if decryption fails
     */
    public byte[] decrypt(byte[] cipherText, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(cipherText);
    }
}