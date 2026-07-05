package zoo.animal;

public record Chimpanzee(String name) implements Primate {
    public Chimpanzee {
        AnimalValidation.requireName(name);
    }
}
