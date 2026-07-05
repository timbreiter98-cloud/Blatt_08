package zoo.animal;

public record SeaUrchin(String name) implements Animal {
    public SeaUrchin {
        AnimalValidation.requireName(name);
    }
}
