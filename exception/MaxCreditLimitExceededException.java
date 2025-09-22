package edu.ccrm.exception;

public class MaxCreditLimitExceededException extends Exception {
    private final int attemptedCredits;
    private final int maxAllowed;
    
    public MaxCreditLimitExceededException(String message, int attempted, int max) {
        super(message);
        this.attemptedCredits = attempted;
        this.maxAllowed = max;
    }
    
    public int getAttemptedCredits() { return attemptedCredits; }
    public int getMaxAllowed() { return maxAllowed; }
}
