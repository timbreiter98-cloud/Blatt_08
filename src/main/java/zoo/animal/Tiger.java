package zoo.animal;

public record Tiger(String name) implements Cat {
    public Tiger {
        AnimalValidation.requireName(name);
    }
}
