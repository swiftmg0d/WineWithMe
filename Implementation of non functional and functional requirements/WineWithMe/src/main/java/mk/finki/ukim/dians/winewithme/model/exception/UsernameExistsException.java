package mk.finki.ukim.dians.winewithme.model.exception;

public class UsernameExistsException extends RuntimeException {

    public UsernameExistsException(String username) {
        super(String.format("%s exists, try diffrent username",username));
    }
}
