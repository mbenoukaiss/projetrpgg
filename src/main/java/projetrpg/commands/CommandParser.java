package projetrpg.commands;

import projetrpg.entities.player.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Class used to parse a command.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public class CommandParser {

    /**
     * Map of the classes associated to an instance of
     * that class which contain the methods
     * listening to commands sent by the user.
     */
    private final Map<Class, Object> listeners;

    /**
     * Map associating each method annotated by Listener
     * to the array sent to the annotation.
     *
     * key   Command fragments set
     * value Set of methods to invoke
     */
    private final Map<Set<CommandFragment>, Set<Method>> methods;

    /**
     * Map allowing to store command fragments, the
     * key is the command as a string.
     *
     * key   Command as a string
     * value Corresponding fragment
     */
    private final Map<String, CommandFragment> commands;

    public CommandParser() {
        listeners = new HashMap<>();
        methods = new HashMap<>();
        commands = new HashMap<>();
    }

    /**
     * Make a command object out of a string
     *
     * @param command Command as a string
     * @return Corresponding command object
     */
    public Command parse(Player player, String command) throws InvalidCommandException {
        Command c = new Command(this, player);
        String[] carray = command.split(" ");

        CommandFragment currentFragment = null;
        StringBuilder nextArgument = null;

        for(String s : carray) {
            s = s.toLowerCase();
            
            if(commands.containsKey(s)) {
                if(currentFragment != null) { //Not the first command
                    c.insertFragment(currentFragment, nextArgument.toString());
                }

                currentFragment = commands.get(s);
                nextArgument = new StringBuilder();
            } else {
                if(nextArgument == null) {
                    throw new InvalidCommandException("First word was not a command");
                } else if(nextArgument.length() > 0) {
                    nextArgument.append(" ");
                }

                nextArgument.append(s);
            }

            c.insertFragment(currentFragment, nextArgument.toString());
        }

        return c;
    }

    /**
     * Dispatch the command to all the methods listening to
     * this kind of command.
     *
     * @param command The command to send
     */
    public String send(Command command) {
        Set<CommandFragment> commands = command.getCommandFragments();

        if(methods.keySet().contains(commands)) {
            Set<Method> commandMethods = methods.get(commands);

            if(commandMethods != null) {
                for (Method listener : commandMethods) {
                    try {
                        if(methods.keySet().contains(commands)) {
                            return (String) listener.invoke(
                                    listeners.get(listener.getDeclaringClass()),
                                    command.generateArguments().keySet().toArray()
                            );
                        }
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ignored) { }
                }
            }
        }

        return null;
    }

    /**
     * Tries to autocomplete a command.
     * Currently only works for the command itself,
     * not the arguments.
     *
     * @param beginning What the user typed.
     * @return A command the user would possibly type.
     */
    public Optional<String> complete(String beginning) {
        return commands.keySet().stream()
                .filter(c -> c.startsWith(beginning))
                .findFirst();
    }

    /**
     * Registers a listener class.
     *
     * @param listenerInstance Instance of the class
     * @param listener Class
     */
    public <T> void registerListener(T listenerInstance, Class<T> listener) throws InvalidAnnotationException {
        listeners.put(listener, listenerInstance);

        for(Method method : listener.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Listener.class)) {
                Listener annotation = method.getAnnotation(Listener.class);

                Set<CommandFragment> fragments = new HashSet<>();
                for(String s : annotation.value()) {
                    if(commands.containsKey(s)) {
                        fragments.add(commands.get(s));
                    } else {
                        throw new InvalidAnnotationException("Unknown command fragment " + s);
                    }
                }

                Set<Method> methodsInSet = methods.get(fragments);
                if(methodsInSet == null) methodsInSet = new HashSet<>();
                methodsInSet.add(method);
                methods.put(fragments, methodsInSet);
            }
        }
    }

    /**
     * Registers a command fragment.
     *
     * @param command The command as a string
     * @param argType The type of the argument of the command
     * @param scanner Function scanning for the object targetted
     *                by the command depending on the argument.
     */
    public <T> void registerCommand(String command, Class<T> argType, BiFunction<Player, String, T> scanner) {
        commands.put(command, new CommandFragment<>(command, argType, scanner));
    }

    /**
     * Registers a command fragment that does not take
     * any argument.
     *
     * @param command The command as a string
     * @param argType The type of the argument of the command
     */
    public <T> void registerCommand(String command, Class<T> argType) {
        commands.put(command, new CommandFragment<>(command, argType, (player, arg) -> null));
    }

}
