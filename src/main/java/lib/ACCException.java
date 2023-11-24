package lib;

public class ACCException extends Exception{
    public E exception;

    ACCException(E exception) {
        super(exception.getMessage());
        this.exception = exception;
    }
}
