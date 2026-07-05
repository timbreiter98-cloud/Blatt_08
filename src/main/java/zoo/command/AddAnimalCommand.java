package zoo.command;

import zoo.animal.Animal;
import zoo.enclosure.Enclosure;
import zoo.result.Result;

import java.util.Objects;

/**
 * Fügt ein Tier in ein Gehege ein. Das Ziel ist Enclosure<? super T>, weil das
 * Gehege ein Consumer für T ist: Ein Lion darf z.B. in ein Enclosure<Lion>,
 * Enclosure<Cat>, Enclosure<Mammal> oder Enclosure<Animal> eingefügt werden.
 */
public final class AddAnimalCommand<T extends Animal>
        implements Command<Enclosure<? super T>, ZooError, String> {

    private final T animal;
    private boolean executed;

    public AddAnimalCommand(T animal) {
        this.animal = Objects.requireNonNull(animal, "animal");
    }

    @Override
    public Result<ZooError, String> execute(Enclosure<? super T> target) {
        if (target == null) {
            return Result.failure(ZooError.NULL_TARGET);
        }
        if (!target.add(animal)) {
            return Result.failure(ZooError.ANIMAL_ALREADY_PRESENT);
        }
        executed = true;
        return Result.success("Tier '%s' wurde zu '%s' hinzugefügt.".formatted(animal.name(), target.name()));
    }

    @Override
    public Result<ZooError, String> undo(Enclosure<? super T> target) {
        if (target == null) {
            return Result.failure(ZooError.NULL_TARGET);
        }
        if (!executed) {
            return Result.failure(ZooError.UNDO_NOT_POSSIBLE);
        }
        if (!target.remove(animal)) {
            return Result.failure(ZooError.ANIMAL_NOT_FOUND);
        }
        executed = false;
        return Result.success("Hinzufügen von '%s' wurde in '%s' rückgängig gemacht."
                .formatted(animal.name(), target.name()));
    }

    @Override
    public String description() {
        return "AddAnimalCommand[%s]".formatted(animal);
    }
}
