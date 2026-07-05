package zoo.command;

import zoo.result.Result;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CommandManager<T> {
    private static final Logger LOGGER = Logger.getLogger(CommandManager.class.getName());

    private final Deque<Command<? super T, ZooError, String>> undoStack = new ArrayDeque<>();
    private final Deque<Command<? super T, ZooError, String>> redoStack = new ArrayDeque<>();

    public Result<ZooError, String> executeCommand(Command<? super T, ZooError, String> command, T target) {
        LOGGER.log(Level.INFO, () -> "executeCommand(command=%s, target=%s)".formatted(commandDescription(command), target));

        if (command == null) {
            return logAndReturnFailure(ZooError.NULL_COMMAND, target, "Kommando fehlt; Ausführung abgebrochen.");
        }
        if (target == null) {
            return logAndReturnFailure(ZooError.NULL_TARGET, target, "Ziel fehlt; Ausführung abgebrochen.");
        }

        Result<ZooError, String> result = command.execute(target);
        if (result instanceof Result.Success<?, ?>) {
            undoStack.push(command);
            redoStack.clear();
            LOGGER.info(() -> successText(result));
        } else if (result instanceof Result.Failure<?, ?> failure) {
            LOGGER.warning(() -> "Kommando '%s' fehlgeschlagen: %s".formatted(command.description(), failure.error()));
        }

        logTargetState("executeCommand abgeschlossen", target);
        return result;
    }

    public Result<ZooError, String> undo(T target) {
        LOGGER.log(Level.INFO, () -> "undo(target=%s)".formatted(target));

        if (target == null) {
            return logAndReturnFailure(ZooError.NULL_TARGET, target, "Ziel fehlt; Undo abgebrochen.");
        }
        if (undoStack.isEmpty()) {
            return logAndReturnFailure(ZooError.NO_COMMAND_TO_UNDO, target, "Undo nicht möglich: undoStack ist leer.");
        }

        Command<? super T, ZooError, String> command = undoStack.peek();
        Result<ZooError, String> result = command.undo(target);
        if (result instanceof Result.Success<?, ?>) {
            undoStack.pop();
            redoStack.push(command);
            LOGGER.info(() -> successText(result));
        } else if (result instanceof Result.Failure<?, ?> failure) {
            LOGGER.warning(() -> "Undo von '%s' fehlgeschlagen: %s".formatted(command.description(), failure.error()));
        }

        logTargetState("undo abgeschlossen", target);
        return result;
    }

    public Result<ZooError, String> redo(T target) {
        LOGGER.log(Level.INFO, () -> "redo(target=%s)".formatted(target));

        if (target == null) {
            return logAndReturnFailure(ZooError.NULL_TARGET, target, "Ziel fehlt; Redo abgebrochen.");
        }
        if (redoStack.isEmpty()) {
            return logAndReturnFailure(ZooError.NO_COMMAND_TO_REDO, target, "Redo nicht möglich: redoStack ist leer.");
        }

        Command<? super T, ZooError, String> command = redoStack.peek();
        Result<ZooError, String> result = command.execute(target);
        if (result instanceof Result.Success<?, ?>) {
            redoStack.pop();
            undoStack.push(command);
            LOGGER.info(() -> successText(result));
        } else if (result instanceof Result.Failure<?, ?> failure) {
            LOGGER.warning(() -> "Redo von '%s' fehlgeschlagen: %s".formatted(command.description(), failure.error()));
        }

        logTargetState("redo abgeschlossen", target);
        return result;
    }

    public int undoStackSize() {
        return undoStack.size();
    }

    public int redoStackSize() {
        return redoStack.size();
    }

    private Result<ZooError, String> logAndReturnFailure(ZooError error, T target, String message) {
        LOGGER.warning(message);
        logTargetState("Operation ohne Änderung beendet", target);
        return Result.failure(error);
    }

    private void logTargetState(String action, T target) {
        LOGGER.fine(() -> "%s; Zielzustand: %s; undoStack=%d; redoStack=%d"
                .formatted(action, target, undoStack.size(), redoStack.size()));
    }

    private static String commandDescription(Command<?, ?, ?> command) {
        return command == null ? "<null>" : command.description();
    }

    private static String successText(Result<ZooError, String> result) {
        if (result instanceof Result.Success<?, ?> success) {
            return String.valueOf(success.value());
        }
        return "kein Erfolg";
    }
}
