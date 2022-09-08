package pl.bankproject.repository.exceptions;

public class NoSuchClientInRepositoryException extends RuntimeException{
    public NoSuchClientInRepositoryException(String message) {
        super(message);
    }
}
