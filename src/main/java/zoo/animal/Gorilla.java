package zoo.animal;

public record Gorilla(String name) implements Primate {
    public Gorilla {
        AnimalValidation.requireName(name);
    }
}
