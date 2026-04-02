package edu.kit.kastel.models;

/**
 * Utility class providing mathematical operations.
 *
 * @author udkcf
 * @version 1.0
 */
public final class MathUtils {
    /** The minimum value for the greatest common divisor. */
    public static final int MIN_GCD = 100;
    private static final int NO_REMAINDER = 0;
    private static final int FIRST_DIVISOR = 2;

    private MathUtils() {
    }

    /**
     * Computes the greatest common divisor (Euclidean algorithm).
     *
     * @param firstNumber  the first integer
     * @param secondNumber the second integer
     * @return the greatest common divisor of the two numbers
     */
    public static int gcd(int firstNumber, int secondNumber) {
        int currentDividend = firstNumber;
        int currentDivisor = secondNumber;

        while (currentDivisor != NO_REMAINDER) {
            int remainder = currentDividend % currentDivisor;
            currentDividend = currentDivisor;
            currentDivisor = remainder;
        }

        return currentDividend;
    }

    /**
     * Checks whether a number is prime.
     *
     * @param number the integer to check
     * @return true if the number is prime, false otherwise
     */
    public static boolean isPrime(int number) {
        for (int i = FIRST_DIVISOR; i * i <= number; i++) {
            if (number % i == NO_REMAINDER) {
                return false;
            }
        }
        return true;
    }
}
