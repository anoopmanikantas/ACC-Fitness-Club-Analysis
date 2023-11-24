package lib;

public enum E {

    InvalidWebpage("This page is not valid");

    private final String message;
    E(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
