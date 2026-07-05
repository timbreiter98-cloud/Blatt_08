package zoo.animal;

public record Trout(String name) implements Fish {
    public Trout {
        AnimalValidation.requireName(name);
    }
}
