package lib;

public enum E {
    InvalidWebpage("This page is not valid");

    private final String message;
    E(String message) {
        this.message = message;
    }

    public String getMessage(String string) { return this.message + string; }

    public String getMessage() { return this.message; }
}
