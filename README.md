\# Blatt 08



Das ist meine Lösung für Blatt 08 im Modul Programmiermethoden 2.



\## Inhalt



Das Projekt erweitert das Zoo-Beispiel aus Blatt 07.



Umgesetzt wurden:



\- Suche nach Tieren mit Optional

\- Command-Pattern für das Hinzufügen und Entfernen von Tieren

\- Undo und Redo mit einem CommandManager

\- Result-Typ für Erfolg und Fehler

\- zentrales Logging im CommandManager



\## Starten



Das Projekt kann mit Maven kompiliert werden:



```bash

mvn compile

```



Die Demo-Klasse ist:



```text

zoo.Demo

```



Alternativ kann das Skript benutzt werden:



```bash

./compile-and-run.sh

```



\## Kurze Begründungen



In `Enclosure<T>` gibt die Methode `findAnimalByName` ein `Optional<T>` zurück, weil ein Gehege nur Tiere von seinem eigenen Typ enthält.



In `Zoo` gibt die Methode `findAnimalByName` ein `Optional<Animal>` zurück, weil der Zoo mehrere Gehege mit verschiedenen Tierarten verwaltet.



Die Commands geben ein `Result` zurück, damit Erfolg und Fehler sauber unterschieden werden können.



Das Logging befindet sich im `CommandManager`, weil dort die Ausführung der Commands zentral verwaltet wird.



Für normale erfolgreiche Aktionen verwende ich `INFO`. Für Fehler verwende ich `WARNING`. Genauere Informationen zum Zustand von Undo und Redo können mit `FINE` geloggt werden.

