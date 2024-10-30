package sendDesk360.model.encryption;

/*******
 * <p> EncryptionUtilsTest Class </p>
 * 
 * <p> Description: A test bed for the EncryptionUtils class. </p>
 * 
 * <p> Copyright: Lillian Elizabeth Seebold © 2024 </p>
 * 
 * @author Lillian Elizabeth Seebold
 * 
 * @version 1.00 2024-10-30
 * 
 */

public class EncryptionUtilsTest {

    /* Static class variables */
    static int numPassed = 0; // the # of tests that have passed 
    static int numFailed = 0; // the # of tests that have failed 

    /* Main Method to run the test bed */
    public static void main(String[] args) {
        // The main method can be invoked manually when testing.
    }

    // Method to run the tests, can be invoked from the main method.
    public static void runTests() {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nTesting Automation for EncryptionUtils");

        // Test Cases
        performTestCase(1, new char[]{'H', 'e', 'l', 'l', 'o'}, true); // Test case: Convert char array to byte array
        performTestCase(2, new byte[]{72, 101, 108, 108, 111}, true); // Test case: Convert byte array back to char array
        performTestCase(3, new char[]{}, false); // Test case: Invalid IV generation input (empty)
        performTestCase(4, new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'}, true); // Test case: Generate IV
        performTestCase(5, new char[] {(Character) null}, false); // Test case: Invalid input (null)

        // Print results
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /**
     * This method enumerates a test case and checks the results of the EncryptionUtils methods.
     *
     * This method does not handle the case of a byteArray that need to be de-crypted (Case #2) 
     * 
     * @param testCase The number of the test case 
     * @param inputText the input data to be tested 
     * @param expectedPass a boolean variable representing if this test case SHOULD pass or fail 
     * 
     */
    private static void performTestCase(int testCase, char[] inputText, boolean expectedPass) {
        System.out.println("Test case: " + testCase);
        
        switch (testCase) {
            case 1:
                // Test converting char array to byte array
                byte[] byteArray = EncryptionUtils.toByteArray(inputText);
                if (byteArray.length > 0) {
                    System.out.println("***Success*** Conversion from char array to byte array passed.");
                    numPassed++;
                } else {
                    System.out.println("***Failure*** Conversion from char array to byte array failed.");
                    numFailed++;
                }
                break;
                
            case 3:
                // Test IV generation with empty char array
                byte[] ivEmpty = EncryptionUtils.getInitializationVector(new char[]{});
                if (ivEmpty.length == 0) {
                    System.out.println("***Success*** IV generation with empty input returned expected result.");
                    numPassed++;
                } else {
                    System.out.println("***Failure*** IV generation with empty input did not return expected result.");
                    numFailed++;
                }
                break;

            case 4:
                // Test IV generation with valid input
                byte[] iv = EncryptionUtils.getInitializationVector(new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'});
                if (iv.length == 16) {
                    System.out.println("***Success*** IV generated successfully.");
                    numPassed++;
                } else {
                    System.out.println("***Failure*** IV generation failed.");
                    numFailed++;
                }
                break;

            case 5:
                // Test IV generation with null input
                byte[] ivNull = EncryptionUtils.getInitializationVector(null);
                if (ivNull.length == 0) {
                    System.out.println("***Success*** IV generation with null input returned expected result.");
                    numPassed++;
                } else {
                    System.out.println("***Failure*** IV generation with null input did not return expected result.");
                    numFailed++;
                }
                break;

            default:
                System.out.println("***Failure*** Invalid test case number.");
                numFailed++;
                break;
        }

        System.out.println(); // Add an empty line for better readability
    }
    
    /**
     * This method enumerates a test case and checks the results of the EncryptionUtils methods.
     *
     * This method does WILL handle the case of a byteArray that need to be de-crypted (Case #2) but will not handle all other test cases
     * 
     * @param testCase The number of the test case 
     * @param inputText the input data to be tested 
     * @param expectedPass a boolean variable representing if this test case SHOULD pass or fail 
     * 
     */
    private static void performTestCase(int testCase, byte[] inputText, boolean expectedPass) {
        System.out.println("Test case: " + testCase);
        
        switch (testCase) {
            case 2:
                // Test converting byte array back to char array
                char[] charArray = EncryptionUtils.toCharArray(new byte[]{72, 101, 108, 108, 111});
                if (new String(charArray).equals("Hello")) {
                    System.out.println("***Success*** Conversion from byte array to char array passed.");
                    numPassed++;
                } else {
                    System.out.println("***Failure*** Conversion from byte array to char array failed.");
                    numFailed++;
                }
                break;

            default:
                System.out.println("***Failure*** Invalid test case number.");
                numFailed++;
                break;
        }

        System.out.println(); // Add an empty line for better readability
    }
}
