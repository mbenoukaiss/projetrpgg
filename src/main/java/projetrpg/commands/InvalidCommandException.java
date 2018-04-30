package projetrpg.commands;

/**
 * Exception thrown if a command is not valid.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public class InvalidCommandException extends Exception {

    public InvalidCommandException(String s) {
        super(s);
    }

}
