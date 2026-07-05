package zoo.animal;

public record DomesticCat(String name) implements Cat {
    public DomesticCat {
        AnimalValidation.requireName(name);
    }
}
