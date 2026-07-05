package zoo;

import zoo.animal.*;
import zoo.command.AddAnimalCommand;
import zoo.command.CommandManager;
import zoo.command.RemoveAnimalCommand;
import zoo.enclosure.*;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Demo {
    public static void main(String[] args) {
        configureLogging(Level.FINE);

        Zoo zoo = new Zoo();

        Aquarium<Fish> aquarium = new Aquarium<>("Amazonas-Aquarium");
        Terrarium<Reptile> terrarium = new Terrarium<>("Wüsten-Terrarium");
        MammalHouse<Mammal> mammalHouse = new MammalHouse<>("Haus der Säugetiere");
        CatHouse catHouse = new CatHouse("Löwen-Haus");

        zoo.addEnclosure(aquarium);
        zoo.addEnclosure(terrarium);
        zoo.addEnclosure(mammalHouse);
        zoo.addEnclosure(catHouse);

        zoo.addAnimal(aquarium, new Trout("Frieda"));
        zoo.addAnimal(aquarium, new Shark("Shawn"));
        zoo.addAnimal(terrarium, new Snake("Sissi"));
        zoo.addAnimal(terrarium, new Turtle("Tessa"));
        zoo.addAnimal(mammalHouse, new Elephant("Ella"));
        zoo.addAnimal(mammalHouse, new Dolphin("Dolly"));
        zoo.addAnimal(catHouse, new Lion("Simba"));

        System.out.println("Suche im Gehege: " + mammalHouse.findAnimalByName("Ella"));
        System.out.println("Suche im Zoo: " + zoo.findAnimalByName("Shawn"));
        System.out.println("Suche ohne Treffer: " + zoo.findAnimalByName("Nemo"));

        CommandManager<Enclosure<Mammal>> mammalManager = new CommandManager<>();
        CommandManager<Enclosure<Lion>> lionManager = new CommandManager<>();

        AddAnimalCommand<Lion> mieze = new AddAnimalCommand<>(new Lion("Mieze"));
        AddAnimalCommand<Gibbon> kiki = new AddAnimalCommand<>(new Gibbon("Kiki"));
        AddAnimalCommand<Lion> leonAdd = new AddAnimalCommand<>(new Lion("Leon"));
        RemoveAnimalCommand<Lion> leonRemove = new RemoveAnimalCommand<>(new Lion("Leon"));

        mammalManager.executeCommand(mieze, mammalHouse);
        mammalManager.executeCommand(kiki, mammalHouse);
        mammalManager.executeCommand(leonAdd, mammalHouse);
        mammalManager.executeCommand(leonRemove, mammalHouse);
        mammalManager.undo(mammalHouse);
        mammalManager.redo(mammalHouse);

        AddAnimalCommand<Lion> felix = new AddAnimalCommand<>(new Lion("Felix"));
        lionManager.executeCommand(felix, catHouse);
        lionManager.undo(catHouse);
        lionManager.redo(catHouse);

        // Die folgenden Zeilen würden nicht kompilieren und zeigen die Typsicherheit:
        // AddAnimalCommand<Shark> nemo = new AddAnimalCommand<>(new Shark("Nemo"));
        // mammalManager.executeCommand(nemo, mammalHouse); // Shark passt nicht in ein Säugetier-Gehege.
        // lionManager.executeCommand(kiki, catHouse);      // Gibbon passt nicht in ein Löwen-Gehege.

        System.out.println(zoo.summary());
        System.out.println("Alle Säugetiere: " + zoo.getAllMammals());
        System.out.println("Zählung nach konkretem Typ: " + zoo.countAnimalsByType());
    }

    private static void configureLogging(Level level) {
        Logger.getLogger(Zoo.class.getName()).setLevel(level);
        Logger.getLogger(CommandManager.class.getName()).setLevel(level);

        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(level);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(level);
        }
    }
}
