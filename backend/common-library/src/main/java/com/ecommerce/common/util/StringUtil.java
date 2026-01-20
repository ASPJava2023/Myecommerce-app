package com.ecommerce.common.util;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class for string operations
 */
public final class StringUtil {

    private StringUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,15}$");

    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if string is not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Capitalize first letter
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Convert to title case
     */
    public static String toTitleCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(capitalize(word)).append(" ");
            }
        }
        return result.toString().trim();
    }

    /**
     * Generate random UUID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate correlation ID
     */
    public static String generateCorrelationId() {
        return "CID-" + UUID.randomUUID().toString();
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Mask email for privacy
     * Example: john.doe@example.com -> j***e@example.com
     */
    public static String maskEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String localPart = parts[0];
        if (localPart.length() <= 2) {
            return email;
        }
        return localPart.charAt(0) + "***" + localPart.charAt(localPart.length() - 1) + "@" + parts[1];
    }

    /**
     * Mask phone number for privacy
     * Example: +1234567890 -> +123***7890
     */
    public static String maskPhone(String phone) {
        if (isEmpty(phone) || phone.length() < 6) {
            return phone;
        }
        int visibleDigits = 3;
        String prefix = phone.substring(0, visibleDigits);
        String suffix = phone.substring(phone.length() - 4);
        return prefix + "***" + suffix;
    }

    /**
     * Truncate string to specified length
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    /**
     * Remove special characters
     */
    public static String removeSpecialCharacters(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    /**
     * Generate slug from string
     * Example: "Hello World!" -> "hello-world"
     */
    public static String generateSlug(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    /**
     * Check if string contains only digits
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("\\d+");
    }

    /**
     * Check if string contains only alphabets
     */
    public static boolean isAlphabetic(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    /**
     * Check if string contains only alphanumeric characters
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z0-9]+");
    }

    /**
     * Reverse string
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Count occurrences of substring
     */
    public static int countOccurrences(String str, String substring) {
        if (isEmpty(str) || isEmpty(substring)) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }
}
