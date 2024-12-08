package commons.commands;

import java.util.Stack;

/**
 * This class exists to simplify the undo-redo logic by implementing the command pattern.
 * This implementation does not entirely work correctly with text, since we don't send changes, we send the entire note.
 * */
public class CommandManager {
    private final Stack<NoteCommand> executedCommands = new Stack<>();
    private final Stack<NoteCommand> undoneCommands = new Stack<>();

    /**
     * Command execution should go through this method to ensure that it is easily undoable and redoable
     * when needed.
     * */
    public void execute(NoteCommand command) {
        command.execute();

        // only reversible commands are on the stack, since no point in storing others
        if (command.isReversible()) {
            executedCommands.push(command);
        }
    }

    /**
     * Handles undo logic of the most recently executed command.
     * */
    public void undo() {
        if (executedCommands.isEmpty()) return;

        NoteCommand command = executedCommands.pop();
        command.undo();
        undoneCommands.push(command);
    }

    /**
     * Handles redo logic of the most recently undone command.
     * */
    public void redo() {
        if (undoneCommands.isEmpty()) return;

        NoteCommand command = undoneCommands.pop();
        command.execute();
        executedCommands.push(command);
    }

    public boolean canUndo() {
        return !executedCommands.isEmpty();
    }

    public boolean canRedo() {
        return !undoneCommands.isEmpty();
    }

    public void clearCommandStacks() {
        executedCommands.clear();
        undoneCommands.clear();
    }
}
