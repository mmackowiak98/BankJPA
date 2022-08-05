package pl.bankproject.exceptions;

public class NoSufficientFundsException extends RuntimeException{
    public NoSufficientFundsException(String message) {
        super(message);
    }
}
