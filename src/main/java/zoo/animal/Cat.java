package zoo.animal;

public sealed interface Cat extends Mammal permits DomesticCat, Tiger, Lion {
}
