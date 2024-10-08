package sendDesk360.model;

public class PasswordValidator {

    /**
     * Validates the given password according to various criteria:
     * - Must not be empty.
     * - Must be at least 8 characters long.
     * - Must contain at least one uppercase letter.
     * - Must contain at least one lowercase letter.
     * - Must contain at least one numeric digit.
     * - Must contain at least one special character.
     * - Must not contain invalid characters.
     *
     * @param input The password input to validate.
     * @return An empty string if the password is valid,
     *         or a message indicating any validation errors.
     */
    public static String validatePassword(String input) {
        if (input == null || input.isEmpty()) {
            return "*** Error *** The password is empty!";
        }

        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumericDigit = false;
        boolean foundSpecialChar = false;

        String specialCharacters = "~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/";

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            // Check for uppercase letter
            if (currentChar >= 'A' && currentChar <= 'Z') {
                foundUpperCase = true;
            }
            // Check for lowercase letter
            else if (currentChar >= 'a' && currentChar <= 'z') {
                foundLowerCase = true;
            }
            // Check for numeric digit
            else if (currentChar >= '0' && currentChar <= '9') {
                foundNumericDigit = true;
            }
            // Check for special character
            else if (specialCharacters.indexOf(currentChar) >= 0) {
                foundSpecialChar = true;
            }
            // Invalid character found
            else {
                return "*** Error *** An invalid character has been found!";
            }
        }

        boolean isLongEnough = input.length() >= 8;

        // Build the error message
        StringBuilder errMessage = new StringBuilder();

        if (!foundUpperCase) {
            errMessage.append("Upper case; ");
        }
        if (!foundLowerCase) {
            errMessage.append("Lower case; ");
        }
        if (!foundNumericDigit) {
            errMessage.append("Numeric digits; ");
        }
        if (!foundSpecialChar) {
            errMessage.append("Special character; ");
        }
        if (!isLongEnough) {
            errMessage.append("Long enough (at least 8 characters); ");
        }

        if (errMessage.length() == 0) {
            // All conditions are satisfied
            return "";
        } else {
            return errMessage.toString() + "conditions were not satisfied";
        }
    }
}