package zoo.animal;

public record Dolphin(String name) implements Mammal {
    public Dolphin {
        AnimalValidation.requireName(name);
    }
}
