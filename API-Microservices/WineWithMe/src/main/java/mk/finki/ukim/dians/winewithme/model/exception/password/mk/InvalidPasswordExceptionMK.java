package mk.finki.ukim.dians.winewithme.model.exception.password.mk;

public class InvalidPasswordExceptionMK extends RuntimeException {
    public InvalidPasswordExceptionMK(int conditionViolated) {
        super(getErrorMessage(conditionViolated));
    }

    private static String getErrorMessage(int conditionViolated) {
        switch (conditionViolated) {
            case 1:
                return "Должината на лозинката треба да биде помеѓу 8 до 15 карактери";
            case 2:
                return "Лозинката не треба да содржи простор";
            case 3:
                return "Лозинката треба да содржи барем една цифра(0-9)";
            case 4:
                return "Лозинката треба да содржи барем еден посебен карактер";
            case 5:
                return "Лозинката треба да содржи најмалку една голема буква(A-Z)";
            case 6:
                return "Лозинката треба да содржи најмалку една мала буква(a-z)";
            default:
                return "Непозната грешка";
        }
    }
}

