package projetrpg.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation telling if a class is listening to
 * commands sent by the user.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Listener {

    /**
     * Array of command fragments, which combined
     * fit the method's arguments
     */
    String[] value();
}
