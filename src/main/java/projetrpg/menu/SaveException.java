package projetrpg.menu;

public class SaveException extends Exception {

    public SaveException(String s) {
        super(s);
    }

    public SaveException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
