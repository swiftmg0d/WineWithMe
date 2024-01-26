package mk.finki.ukim.dians.winewithme.model.exception.password.uni;

public class PasswordNotMatchException extends RuntimeException {
  public PasswordNotMatchException(String msg) {
      super(msg);
    }
}
