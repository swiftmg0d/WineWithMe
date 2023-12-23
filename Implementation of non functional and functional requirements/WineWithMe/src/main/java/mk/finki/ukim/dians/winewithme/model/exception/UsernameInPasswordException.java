package mk.finki.ukim.dians.winewithme.model.exception;

public class UsernameInPasswordException extends RuntimeException{
    public UsernameInPasswordException() {
        super("Your password shouldn't contain your username!");
    }
}
