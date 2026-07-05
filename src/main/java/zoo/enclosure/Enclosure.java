package zoo.enclosure;

import zoo.animal.Animal;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Generisches Gehege für Tiere.
 *
 * Die Typvariable ist auf Animal beschränkt. Dadurch kann z.B. kein
 * Enclosure<String> angelegt werden. Intern wird ein LinkedHashSet genutzt:
 * Set verhindert doppelte Tiere, LinkedHashSet erhält zusätzlich die
 * Einfüge-Reihenfolge für gut lesbare Ausgaben.
 */
public class Enclosure<T extends Animal> {
    private final String name;
    private final Set<T> inhabitants = new LinkedHashSet<>();

    public Enclosure(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Der Name eines Geheges darf nicht leer sein.");
        }
        this.name = name;
    }

    public String name() {
        return name;
    }

    public boolean add(T animal) {
        return inhabitants.add(Objects.requireNonNull(animal, "animal"));
    }

    public boolean remove(T animal) {
        return inhabitants.remove(Objects.requireNonNull(animal, "animal"));
    }

    /**
     * Sucht im konkreten Gehege nach dem ersten Tier mit dem übergebenen Namen.
     *
     * Als Typargument wird Optional<T> genutzt, weil T bereits genau den Typ
     * der Tiere beschreibt, die in diesem Gehege gehalten werden dürfen.
     */
    public Optional<T> findAnimalByName(String animalName) {
        if (animalName == null || animalName.isBlank()) {
            return Optional.empty();
        }
        return inhabitants.stream()
                .filter(animal -> animal.name().equals(animalName))
                .findFirst();
    }

    public List<T> getInhabitants() {
        return List.copyOf(inhabitants);
    }

    public int size() {
        return inhabitants.size();
    }

    @Override
    public String toString() {
        return "%s{name='%s', inhabitants=%s}".formatted(getClass().getSimpleName(), name, inhabitants);
    }
}
