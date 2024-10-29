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
        ByteBuffer byteBuffer = Charset.defaultCharset().encode(CharBuffer.wrap(chars));
        return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
    }

    /**
     * Converts a byte array back to a character array using the default charset.
     *
     * @param bytes the input byte array
     * @return the resulting character array
     */
    public static char[] toCharArray(byte[] bytes) {
        CharBuffer charBuffer = Charset.defaultCharset().decode(ByteBuffer.wrap(bytes));
        return Arrays.copyOf(charBuffer.array(), charBuffer.limit());
    }

    /**
     * Generates a 16-byte initialization vector (IV) from a given text.
     * If the input text is shorter than 16 characters, it cycles through the text.
     *
     * @param text the input character array
     * @return a byte array representing the initialization vector (IV)
     */
    public static byte[] getInitializationVector(char[] text) {
        char[] iv = new char[IV_SIZE];
        int textPointer = 0;
        int ivPointer = 0;

        // Cycle through the text to fill the IV array
        while (ivPointer < IV_SIZE) {
            iv[ivPointer++] = text[textPointer++ % text.length];
        }
        return toByteArray(iv);
    }
    
    /**
     * Generates a random 16-byte initialization vector (IV).
     *
     * @return a byte array representing the initialization vector (IV)
     */
    public static byte[] generateRandomIV() {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }
}