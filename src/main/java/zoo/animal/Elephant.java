package zoo.animal;

public record Elephant(String name) implements Mammal {
    public Elephant {
        AnimalValidation.requireName(name);
    }
}
