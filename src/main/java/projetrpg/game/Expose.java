package projetrpg.game;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation used for the command see.
 * When a field is annotated by this annotation
 * it will be available in the see command.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Expose {

    /**
     * The name of the field.
     *
     * @return The name.
     */
    String value();
}
