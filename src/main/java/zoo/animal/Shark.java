package zoo.animal;

public record Shark(String name) implements Fish {
    public Shark {
        AnimalValidation.requireName(name);
    }
}
