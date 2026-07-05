package zoo.animal;

public record Snake(String name) implements Reptile {
    public Snake {
        AnimalValidation.requireName(name);
    }
}
