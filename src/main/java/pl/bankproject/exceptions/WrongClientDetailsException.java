package pl.bankproject.exceptions;

public class WrongClientDetailsException extends RuntimeException{
    public WrongClientDetailsException(String message) {
        super(message);
    }

    public WrongClientDetailsException() {
    }
}
