package sendDesk360.model.encryption;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Utility class for handling encryption-related conversions.
 */
public class EncryptionUtils {

    private static final int IV_SIZE = 16;  // Size of the initialization vector (IV)

    /**
     * Converts a character array to a byte array using the default charset.
     *
     * @param chars the input character array
     * @return the resulting byte array
     */
    public static byte[] toByteArray(char[] chars) {
    	try {
            ByteBuffer byteBuffer = Charset.defaultCharset().encode(CharBuffer.wrap(chars));
            return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
        } catch (Exception e) {
            System.err.println("Error converting char array to byte array: " + e.getMessage());
            return new byte[0];  // Return empty array in case of an error
        }
    }

    /**
     * Converts a byte array back to a character array using the default charset.
     *
     * @param bytes the input byte array
     * @return the resulting character array
     */
    public static char[] toCharArray(byte[] bytes) {
    	try {
            CharBuffer charBuffer = Charset.defaultCharset().decode(ByteBuffer.wrap(bytes));
            return Arrays.copyOf(charBuffer.array(), charBuffer.limit());
        } catch (Exception e) {
            System.err.println("Error converting byte array to char array: " + e.getMessage());
            return new char[0];  // Return empty array in case of an error
        }
    }

    /**
     * Generates a 16-byte initialization vector (IV) from a given text.
     * If the input text is shorter than 16 characters, it cycles through the text.
     *
     * @param text the input character array
     * @return a byte array representing the initialization vector (IV)
     */
    public static byte[] getInitializationVector(char[] text) {
    	if (text == null || text.length == 0) {
            System.err.println("Invalid input text for IV generation.");
            return new byte[0];  // Return empty array for invalid input
        }
        
        try {
            char[] iv = new char[IV_SIZE];
            int textPointer = 0;
            int ivPointer = 0;

            // Cycle through the text to fill the IV array
            while (ivPointer < IV_SIZE) {
                iv[ivPointer++] = text[textPointer++ % text.length];
            }
            return toByteArray(iv);
        } catch (Exception e) {
            System.err.println("Error generating initialization vector: " + e.getMessage());
            return new byte[0];  // Return empty array in case of an error
        }
    }
    
    /**
     * Generates a random 16-byte initialization vector (IV).
     *
     * @return a byte array representing the initialization vector (IV)
     */
    public static byte[] generateRandomIV() {
    	try {
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            return iv;
        } catch (Exception e) {
            System.err.println("Error generating random IV: " + e.getMessage());
            return new byte[0];  // Return empty array in case of an error
        }
    }
}