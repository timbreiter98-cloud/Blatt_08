package zoo.animal;

public record Penguin(String name) implements Bird {
    public Penguin {
        AnimalValidation.requireName(name);
    }
}
