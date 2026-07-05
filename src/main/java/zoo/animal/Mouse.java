package zoo.animal;

public record Mouse(String name) implements Rodent {
    public Mouse {
        AnimalValidation.requireName(name);
    }
}
