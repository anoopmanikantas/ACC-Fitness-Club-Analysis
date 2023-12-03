package lib;

public enum E {

    InvalidFee("Fees cannot have spaces or the value entered is invalid and it cannot be 0, please try again!"),
    GymNotFound("The Gym you are searching for does not exist, please try again."),
    CompletedWordsNotFound("Unable to complete the words based on your input. Try Again"),
    InputOptionDoesNotExist("Input option does not exist, please enter a valid number."),
    StringInputCannotBeEmpty("String input cannot be empty, please try again."),
    MoreThanOneWordEntered("Please enter only one word for amenity and try again."),
    InputNotInteger("You have entered non number value. Try again!");

    private final String message;
    E(String message) {
        this.message = message;
    }

    public String getMessage() { return this.message; }
}
