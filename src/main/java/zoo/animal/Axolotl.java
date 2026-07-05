package zoo.animal;

public record Axolotl(String name) implements Animal {
    public Axolotl {
        AnimalValidation.requireName(name);
    }
}
