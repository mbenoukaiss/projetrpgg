package projetrpg.commands;

/**
 * Exception thrown when an annotation is not
 * valid (e.g. unknown command fragment)
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public class InvalidAnnotationException extends Exception {

    public InvalidAnnotationException(String s) {
        super(s);
    }

}
