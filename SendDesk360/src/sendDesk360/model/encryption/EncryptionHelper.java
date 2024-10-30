package sendDesk360.model.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.util.Arrays;

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
    	Cipher cipher; 
    	try {
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            return cipher.doFinal(plainText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.err.println("Encryption algorithm or padding scheme not found: " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("Invalid encryption key: " + e.getMessage());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            System.err.println("Error during encryption: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during encryption: " + e.getMessage());
        }
    	return new byte[0]; // Return empty array in case of an error
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
    	try {
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            return cipher.doFinal(cipherText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.err.println("Decryption algorithm or padding scheme not found: " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("Invalid decryption key: " + e.getMessage());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            System.err.println("Error during decryption: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during decryption: " + e.getMessage());
        }
        return new byte[0]; // Return empty array in case of an error
    }
}