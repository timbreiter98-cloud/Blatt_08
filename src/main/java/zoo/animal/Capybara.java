package zoo.animal;

public record Capybara(String name) implements Rodent {
    public Capybara {
        AnimalValidation.requireName(name);
    }
}
