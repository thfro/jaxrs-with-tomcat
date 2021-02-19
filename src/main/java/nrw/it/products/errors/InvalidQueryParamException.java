package nrw.it.products.errors;

public class InvalidQueryParamException extends RuntimeException {

    private int errorCode;

    public InvalidQueryParamException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
