package pl.bankproject.repository.exceptions;

public class WrongClientDetailsException extends RuntimeException{
    public WrongClientDetailsException(String message) {
        super(message);
    }

    public WrongClientDetailsException() {
    }
}
