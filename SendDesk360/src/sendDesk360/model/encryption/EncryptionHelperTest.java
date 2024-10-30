package sendDesk360.model.encryption;


/*******
 * <p> EncryptionHelperTestbed Class </p>
 * 
 * <p> Description: A testbed for testing the EncryptionHelper class </p>
 * 
 * <p> Copyright: Lillian Elizabeth Seebold © 2024 </p>
 * 
 * @author Lillian Elizabeth Seebold
 * 
 * @version 1.00	2024-10-30 Initial creation for testing the EncryptionHelper class 
 */

import sendDesk360.model.encryption.EncryptionHelper;
import java.util.Arrays;

public class EncryptionHelperTest {

    /* Static class variables */
    static int numPassed = 0; // The number of tests that passed
    static int numFailed = 0; // The number of tests that failed

    /* Main Method to run the test bed */
    public static void main(String[] args) {
        //if we would like to automatically test the EncryptionHelper, then we can manually invoke it here. 
    }
    
    public static void runTests() {
    	System.out.println("____________________________________________________________________________");
        System.out.println("\nEncryption Helper Testing Automation");

        // Initialize the EncryptionHelper
        EncryptionHelper encryptionHelper = new EncryptionHelper();
        
        // Test cases: Encrypt and Decrypt functionality
        performTestCase(1, "Hello World!".getBytes(), new byte[16], true); // Test with valid input
        performTestCase(2, "Short".getBytes(), new byte[16], false); // Test with short input
        performTestCase(3, "".getBytes(), new byte[16], false); // Test with empty input
        performTestCase(4, "AnotherTest123!".getBytes(), new byte[16], true); // Another valid input

        // Display the results to the user
        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /**
     * This method performs a test case for the EncryptionHelper class.
     *
     * @param testCase The number of the test case
     * @param inputText The byte array input to encrypt and decrypt
     * @param iv The initialization vector
     * @param expectedPass Boolean indicating if the test is expected to pass
     */
    private static void performTestCase(int testCase, byte[] inputText, byte[] iv, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
        System.out.println("Input: " + Arrays.toString(inputText));
        System.out.println("IV: " + Arrays.toString(iv));
        System.out.println("______________");
        
        try {
            EncryptionHelper encryptionHelper = new EncryptionHelper();
            byte[] encrypted = encryptionHelper.encrypt(inputText, iv);
            byte[] decrypted = encryptionHelper.decrypt(encrypted, iv);

            // Check if the result matches the expected outcome
            if (expectedPass) {
                if (Arrays.equals(inputText, decrypted)) {
                    System.out.println("***Success*** Test case passed. Input matches decrypted output.");
                    numPassed++;
                } else {
                    System.out.println("***Failure*** Test case failed. Decrypted output does not match input.");
                    numFailed++;
                }
            } else {
                System.out.println("***Failure*** Test case failed. Expected to fail but passed.");
                numFailed++;
            }
        } catch (Exception e) {
            System.out.println("***Exception*** An error occurred: " + e.getMessage());
            if (!expectedPass) {
                System.out.println("Expected failure. This is a pass.");
                numPassed++;
            } else {
                numFailed++;
            }
        }
    }
}
