package zoo.animal;

/**
 * Oberstes Interface der stark vereinfachten Zoo-Tierhierarchie.
 *
 * Alle erlaubten direkten Untertypen sind über permits festgelegt. Damit
 * bleibt die sealed-Hierarchie bewusst geschlossen und kontrollierbar.
 */
public sealed interface Animal permits Mammal, Fish, Reptile, Bird, Axolotl, SeaUrchin {
    String name();
}
