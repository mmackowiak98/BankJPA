package pl.bankproject.exceptions;

public class NoSuchClientInRepositoryException extends RuntimeException{
    public NoSuchClientInRepositoryException(String message) {
        super(message);
    }
}
