package zoo.animal;

public record Eagle(String name) implements Bird {
    public Eagle {
        AnimalValidation.requireName(name);
    }
}
