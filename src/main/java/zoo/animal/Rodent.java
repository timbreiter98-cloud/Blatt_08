package zoo.animal;

public sealed interface Rodent extends Mammal permits Mouse, Capybara {
}
