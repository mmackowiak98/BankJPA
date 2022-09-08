package pl.bankproject.repository.exceptions;

public class NoSufficientFundsException extends RuntimeException{
    public NoSufficientFundsException(String message) {
        super(message);
    }
}
