package projetrpg.commands;

import projetrpg.entities.player.Player;

import java.util.*;

/**
 * Represents a command typed by a user.
 */
public class Command {

    /**
     * The parser that generated and should
     * be used to send the command.
     */
    private final CommandParser parser;

    private Player player;

    /**
     * Map of the command fragments with their
     * argument used in this command.
     */
    private final Map<CommandFragment, String> commands;

    Command(CommandParser parser, Player player) {
        this.parser = parser;
        this.commands = new LinkedHashMap<>(); //Keeping insertion order required
        this.player = player;
    }

    /**
     * Adds a command fragment.
     *
     * @param cf The fragment
     * @param strArgument The corresponding argument
     */
    public void insertFragment(CommandFragment cf, String strArgument) {
        commands.put(cf, strArgument);
    }

    /**
     * Generates the right objects based on the
     * arguments.
     *
     * @return The arguments as objects.
     */
    public Map<Object, Class> generateArguments() {
        Map<Object, Class> args = new HashMap<>();

        for(Map.Entry<CommandFragment, String> centry : commands.entrySet()) {
            args.put(centry.getKey().process(this.player, centry.getValue()), centry.getKey().getArgumentType());
        }

        return args;
    }

    /**
     * Sends the command to listeners.
     */
    public String send() {
        return parser.send(this);
    }

    /**
     * Accessor for the command fragments used
     * in this command.
     *
     * @return The command fragments.
     */
    public Set<CommandFragment> getCommandFragments() {
        return commands.keySet();
    }


}
