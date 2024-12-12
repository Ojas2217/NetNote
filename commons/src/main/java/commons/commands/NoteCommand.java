package commons.commands;

/**
 * exists to implement the command design pattern for note commands
 */
public interface NoteCommand {
    void execute();

    void undo();

    boolean isReversible();
}
