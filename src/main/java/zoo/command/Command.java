package zoo.command;

import zoo.result.Result;

/**
 * Generisches Command-Interface. T beschreibt den Typ des Ziels, auf dem das
 * Kommando arbeitet. E beschreibt den fachlichen Fehlertyp, R den Erfolgstyp.
 */
public interface Command<T, E, R> {
    Result<E, R> execute(T target);

    Result<E, R> undo(T target);

    String description();
}
