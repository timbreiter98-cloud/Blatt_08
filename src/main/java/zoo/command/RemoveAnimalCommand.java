package zoo.command;

import zoo.animal.Animal;
import zoo.enclosure.Enclosure;
import zoo.result.Result;

import java.util.Objects;

/**
 * Entfernt ein Tier aus einem Gehege. Auch hier ist das Ziel Enclosure<? super T>,
 * damit z.B. ein Lion aus einem Enclosure<Mammal> entfernt werden kann.
 */
public final class RemoveAnimalCommand<T extends Animal>
        implements Command<Enclosure<? super T>, ZooError, String> {

    private final T animal;
    private boolean executed;

    public RemoveAnimalCommand(T animal) {
        this.animal = Objects.requireNonNull(animal, "animal");
    }

    @Override
    public Result<ZooError, String> execute(Enclosure<? super T> target) {
        if (target == null) {
            return Result.failure(ZooError.NULL_TARGET);
        }
        if (!target.remove(animal)) {
            return Result.failure(ZooError.ANIMAL_NOT_FOUND);
        }
        executed = true;
        return Result.success("Tier '%s' wurde aus '%s' entfernt.".formatted(animal.name(), target.name()));
    }

    @Override
    public Result<ZooError, String> undo(Enclosure<? super T> target) {
        if (target == null) {
            return Result.failure(ZooError.NULL_TARGET);
        }
        if (!executed) {
            return Result.failure(ZooError.UNDO_NOT_POSSIBLE);
        }
        if (!target.add(animal)) {
            return Result.failure(ZooError.ANIMAL_ALREADY_PRESENT);
        }
        executed = false;
        return Result.success("Entfernen von '%s' wurde in '%s' rückgängig gemacht."
                .formatted(animal.name(), target.name()));
    }

    @Override
    public String description() {
        return "RemoveAnimalCommand[%s]".formatted(animal);
    }
}
