package zoo.animal;

public record Gibbon(String name) implements Primate {
    public Gibbon {
        AnimalValidation.requireName(name);
    }
}
