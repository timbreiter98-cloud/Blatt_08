package zoo.enclosure;

import zoo.animal.Lion;

/**
 * Spezielles Katzengelände aus der Aufgabenstellung: Dieses Gehege ist
 * auf Löwen spezialisiert. Andere Säugetiere oder Fische passen deshalb
 * schon zur Compile-Zeit nicht hinein.
 */
public final class CatHouse extends Enclosure<Lion> {
    public CatHouse(String name) {
        super(name);
    }
}
