package commands;

import java.util.function.Function;

/**
 * Represents a command that can be a part (or
 * the entirety) of a command send by a user.
 * @param <T> Type of the argument of this command
 */
public class CommandFragment<T> {

    /**
     * The command as a string.
     */
    private final String command;

    /**
     * The type of the argument of the command.
     */
    private final Class<T> argumentType;

    /**
     * The functions that has to scan to get the
     * right object based on a string (argument).
     */
    private final Function<String, T> scanner;

    CommandFragment(String command, Class<T> argumentType, Function<String, T> scanner) {
        this.command = command;
        this.argumentType = argumentType;
        this.scanner = scanner;
    }

    /**
     * Generates the right object by calling the function.
     *
     * @param argument The command argument
     * @return An object.
     */
    public T process(String argument) {
        return scanner.apply(argument);
    }

    /**
     * Accessor for the type of the argument.
     *
     * @return The type of the argument.
     */
    public Class<T> getArgumentType() {
        return argumentType;
    }
}
