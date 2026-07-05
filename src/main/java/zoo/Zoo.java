package zoo;

import zoo.animal.Animal;
import zoo.animal.Bird;
import zoo.animal.Fish;
import zoo.animal.Mammal;
import zoo.animal.Reptile;
import zoo.enclosure.Enclosure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Zoo {
    private static final Logger LOGGER = Logger.getLogger(Zoo.class.getName());

    private final List<Enclosure<? extends Animal>> enclosures = new ArrayList<>();

    public boolean addEnclosure(Enclosure<? extends Animal> enclosure) {
        LOGGER.log(Level.INFO, () -> "addEnclosure(enclosure=%s)".formatted(enclosure));

        if (enclosure == null) {
            LOGGER.severe("Es wurde versucht, ein null-Gehege hinzuzufügen.");
            throw new IllegalArgumentException("enclosure darf nicht null sein");
        }

        if (findEnclosureByNameInternal(enclosure.name()) != null) {
            LOGGER.warning(() -> "Ein Gehege mit dem Namen '%s' existiert bereits.".formatted(enclosure.name()));
            logStateAfter("addEnclosure abgebrochen");
            return false;
        }

        boolean added = enclosures.add(enclosure);
        logStateAfter("addEnclosure erfolgreich");
        return added;
    }

    public List<Enclosure<? extends Animal>> getEnclosures() {
        LOGGER.info("getEnclosures()");
        List<Enclosure<? extends Animal>> result = List.copyOf(enclosures);
        logStateAfter("getEnclosures erfolgreich");
        return result;
    }

    public Enclosure<? extends Animal> findEnclosureByName(String name) {
        LOGGER.log(Level.INFO, () -> "findEnclosureByName(name=%s)".formatted(name));

        if (name == null || name.isBlank()) {
            LOGGER.warning("Es wurde kein gültiger Gehegename übergeben.");
            logStateAfter("findEnclosureByName ohne Treffer");
            return null;
        }

        Enclosure<? extends Animal> result = findEnclosureByNameInternal(name);
        if (result == null) {
            LOGGER.warning(() -> "Gehege '%s' wurde nicht gefunden.".formatted(name));
        }
        logStateAfter("findEnclosureByName abgeschlossen");
        return result;
    }

    /**
     * Sucht in allen Gehegen nach dem ersten Tier mit dem gesuchten Namen.
     *
     * Der Rückgabetyp ist Optional<Animal>, weil der Zoo verschiedene Gehege
     * mit verschiedenen konkreten Tier-Typen verwaltet. Der einzige gemeinsame
     * fachliche Obertyp ist deshalb Animal.
     */
    public Optional<Animal> findAnimalByName(String animalName) {
        LOGGER.log(Level.INFO, () -> "findAnimalByName(animalName=%s)".formatted(animalName));

        if (animalName == null || animalName.isBlank()) {
            LOGGER.warning("Es wurde kein gültiger Tiername übergeben.");
            logStateAfter("findAnimalByName ohne Treffer");
            return Optional.empty();
        }

        Optional<Animal> result = enclosures.stream()
                .flatMap(enclosure -> enclosure.findAnimalByName(animalName).stream())
                .map(Animal.class::cast)
                .findFirst();

        if (result.isEmpty()) {
            LOGGER.info(() -> "Kein Tier mit dem Namen '%s' gefunden.".formatted(animalName));
        }
        logStateAfter("findAnimalByName abgeschlossen");
        return result;
    }

    public List<Animal> getAllAnimals() {
        LOGGER.info("getAllAnimals()");
        List<Animal> result = allAnimalsInternal();
        logStateAfter("getAllAnimals erfolgreich");
        return result;
    }

    public List<Mammal> getAllMammals() {
        LOGGER.info("getAllMammals()");
        List<Mammal> result = allAnimalsInternal().stream()
                .filter(Mammal.class::isInstance)
                .map(Mammal.class::cast)
                .toList();
        logStateAfter("getAllMammals erfolgreich");
        return result;
    }

    public List<Animal> getAnimalsByPredicate(Predicate<Animal> predicate) {
        LOGGER.log(Level.INFO, () -> "getAnimalsByPredicate(predicate=%s)".formatted(predicate));

        if (predicate == null) {
            LOGGER.severe("Es wurde ein null-Predicate übergeben.");
            throw new IllegalArgumentException("predicate darf nicht null sein");
        }

        List<Animal> result = allAnimalsInternal().stream()
                .filter(predicate)
                .toList();
        logStateAfter("getAnimalsByPredicate erfolgreich");
        return result;
    }

    public Map<Class<? extends Animal>, Long> countAnimalsByType() {
        LOGGER.info("countAnimalsByType()");
        Map<Class<? extends Animal>, Long> result = allAnimalsInternal().stream()
                .collect(Collectors.groupingBy(
                        Animal::getClass,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
        logStateAfter("countAnimalsByType erfolgreich");
        return result;
    }

    public List<Enclosure<? extends Animal>> getOvercrowdedEnclosures(int maxAnimals) {
        LOGGER.log(Level.INFO, () -> "getOvercrowdedEnclosures(maxAnimals=%d)".formatted(maxAnimals));

        if (maxAnimals < 0) {
            LOGGER.warning("maxAnimals ist negativ; alle nicht-leeren Gehege gelten dadurch als überfüllt.");
        }

        List<Enclosure<? extends Animal>> result = enclosures.stream()
                .filter(enclosure -> enclosure.size() > maxAnimals)
                .toList();
        logStateAfter("getOvercrowdedEnclosures erfolgreich");
        return result;
    }

    public String summary() {
        LOGGER.info("summary()");
        List<Animal> animals = allAnimalsInternal();
        Map<String, Long> countsByGroup = animals.stream()
                .collect(Collectors.groupingBy(
                        Zoo::mainAnimalGroup,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        String details = countsByGroup.entrySet().stream()
                .map(entry -> "%d %s".formatted(entry.getValue(), entry.getKey()))
                .collect(Collectors.joining(", "));

        String result = "Zoo mit %d Gehegen und %d Tieren%s".formatted(
                enclosures.size(),
                animals.size(),
                details.isBlank() ? "." : ": " + details
        );
        logStateAfter("summary erfolgreich");
        return result;
    }

    public <T extends Animal> boolean addAnimal(Enclosure<T> enclosure, T animal) {
        LOGGER.log(Level.INFO, () -> "addAnimal(enclosure=%s, animal=%s)".formatted(enclosure, animal));

        validateManagedEnclosure(enclosure);
        Objects.requireNonNull(animal, "animal");

        boolean added = enclosure.add(animal);
        if (!added) {
            LOGGER.warning(() -> "Tier '%s' ist bereits in Gehege '%s'.".formatted(animal.name(), enclosure.name()));
        }
        logStateAfter("addAnimal abgeschlossen");
        return added;
    }

    public <T extends Animal> boolean removeAnimal(Enclosure<T> enclosure, T animal) {
        LOGGER.log(Level.INFO, () -> "removeAnimal(enclosure=%s, animal=%s)".formatted(enclosure, animal));

        validateManagedEnclosure(enclosure);
        Objects.requireNonNull(animal, "animal");

        boolean removed = enclosure.remove(animal);
        if (!removed) {
            LOGGER.warning(() -> "Tier '%s' wurde im Gehege '%s' nicht gefunden.".formatted(animal.name(), enclosure.name()));
        }
        logStateAfter("removeAnimal abgeschlossen");
        return removed;
    }

    public <T extends Animal> boolean transferAnimal(Enclosure<T> source, Enclosure<T> target, T animal) {
        LOGGER.log(Level.INFO, () -> "transferAnimal(source=%s, target=%s, animal=%s)".formatted(source, target, animal));

        validateManagedEnclosure(source);
        validateManagedEnclosure(target);
        Objects.requireNonNull(animal, "animal");

        if (!source.remove(animal)) {
            LOGGER.warning(() -> "Tier '%s' wurde im Ausgangsgehege '%s' nicht gefunden.".formatted(animal.name(), source.name()));
            logStateAfter("transferAnimal abgebrochen");
            return false;
        }

        if (!target.add(animal)) {
            LOGGER.warning(() -> "Tier '%s' war im Zielgehege '%s' bereits vorhanden; Transfer wird zurückgerollt."
                    .formatted(animal.name(), target.name()));
            source.add(animal);
            logStateAfter("transferAnimal zurückgerollt");
            return false;
        }

        logStateAfter("transferAnimal erfolgreich");
        return true;
    }

    private Enclosure<? extends Animal> findEnclosureByNameInternal(String name) {
        return enclosures.stream()
                .filter(enclosure -> enclosure.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    private List<Animal> allAnimalsInternal() {
        return enclosures.stream()
                .flatMap(enclosure -> enclosure.getInhabitants().stream())
                .map(Animal.class::cast)
                .toList();
    }

    private void validateManagedEnclosure(Enclosure<? extends Animal> enclosure) {
        if (enclosure == null) {
            LOGGER.severe("Eine Zoo-Aktion wurde mit null als Gehege aufgerufen.");
            throw new IllegalArgumentException("enclosure darf nicht null sein");
        }
        if (!enclosures.contains(enclosure)) {
            LOGGER.warning(() -> "Gehege '%s' wird von diesem Zoo nicht verwaltet.".formatted(enclosure.name()));
            throw new IllegalArgumentException("Gehege wird von diesem Zoo nicht verwaltet: " + enclosure.name());
        }
    }

    private void logStateAfter(String action) {
        checkForInconsistentState();
        LOGGER.fine(() -> "%s: %s".formatted(action, stateSummaryForLog()));
    }

    private String stateSummaryForLog() {
        return "%d Gehege, %d Tiere".formatted(enclosures.size(), allAnimalsInternal().size());
    }

    private void checkForInconsistentState() {
        long distinctNames = enclosures.stream()
                .map(Enclosure::name)
                .distinct()
                .count();
        if (distinctNames != enclosures.size()) {
            LOGGER.severe("Inkonsistenter Zoo-Zustand: Es gibt doppelte Gehegenamen.");
        }
    }

    private static String mainAnimalGroup(Animal animal) {
        if (animal instanceof Mammal) {
            return "Mammals";
        }
        if (animal instanceof Bird) {
            return "Birds";
        }
        if (animal instanceof Fish) {
            return "Fish";
        }
        if (animal instanceof Reptile) {
            return "Reptiles";
        }
        return "Other Animals";
    }
}
