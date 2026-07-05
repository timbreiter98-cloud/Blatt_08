package zoo.animal;

final class AnimalValidation {
    private AnimalValidation() {
    }

    static void requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Der Name eines Tieres darf nicht leer sein.");
        }
    }
}
