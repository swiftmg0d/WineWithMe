package mk.finki.ukim.dians.winewithme.model.exception;

public class PasswordNotMatchException  extends RuntimeException{
    public PasswordNotMatchException() {
        super("The passwords didn't match!");
    }
}
