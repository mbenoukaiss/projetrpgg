package commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

/**
 * Class used to parse a command.
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
    public Command parse(String command) throws InvalidCommandException {
        Command c = new Command(this);
        String[] carray = command.split(" ");

        CommandFragment currentFragment = null;
        StringBuilder nextArgument = null;

        for(String s : carray) {
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
    public void send(Command command) {
        Set<CommandFragment> commands = command.getCommandFragments();

        if(methods.keySet().contains(commands)) {
            Set<Method> commandMethods = methods.get(commands);

            if(commandMethods != null) {
                for(Method listener : commandMethods) {
                    try {
                        if(methods.keySet().contains(commands)) {
                            listener.invoke(
                                    listeners.get(listener.getDeclaringClass()),
                                    command.generateArguments().keySet().toArray()
                            );
                        }
                    } catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException ignored) {
                        /*
                          Exception is ignored as it may be normal to not have the same arguments
                          as a listener may want to select only some type of object (ex: the scanner
                          provides an entity but the listener only wants Guards
                          Though there may be better way to do it, checking is probably slower than
                          letting invoke throw an exception
                         */
                    }
                }
            }
        }
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
     * Unregisters a listener class.
     *
     * @param listener The class to unregister
     */
    public <T> void unregisterListener(Class<T> listener) {
        Set<Method> methodsToRemove = new HashSet<>();

        for (Method method : listener.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Listener.class)) {
                methodsToRemove.add(method);
            }
        }

        listeners.remove(listener);

        for(Set<Method> mlist : methods.values()) {
            mlist.removeAll(methodsToRemove);
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
    public <T> void registerCommand(String command, Class<T> argType, Function<String, T> scanner) {
        commands.put(command, new CommandFragment<>(command, argType, scanner));
    }

    /**
     * Unregisters a command fragment.
     *
     * @param command The fragment to unregister
     */
    public void unregisterCommand(String command) {
        commands.remove(command);
    }

}
