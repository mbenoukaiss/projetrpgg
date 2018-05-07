package projetrpg.game;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for the command see.
 * When a field is annotated by this annotation
 * it will be available in the see command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Expose {

    /**
     * The name of the field.
     *
     * @return The name.
     */
    String value();
}
