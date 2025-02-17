package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;

import static at.ac.fhcampuswien.fhmdb.models.Genre.*;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;

    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres; //++++++++++++++++++++++++++++
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    // Getter for genres +++++++++++++++++++++++++++++++++++
    public List<Genre> getGenres() {
        return genres;
    }



    // Override toString() for debugging
    @Override
    public String toString() {
        return title + " - " + description + " - Genres: " + String.join(", ", genres.toString());
    }


    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();

        // Add dummy movies
        movies.add(new Movie("Alien 3", "Nach ihrer letzten Begegnung landet Ellen Ripley auf der Fiorina 161, einem Hochsicherheitsgefängnis. Als kurz nach ihrer Ankunft eine Reihe von seltsamen und tödlichen Ereignissen eintreten, erkennt Ripley, dass sie einen unwillkommenen Besucher mitgebracht hat.", List.of(ACTION, SCIENCE_FICTION, HORROR)));
        movies.add(new Movie("Blade Runner", "Ein Blade Runner muss vier Replikanten verfolgen und versuchen, sie zu eliminieren, weil sie ein Raumschiff gestohlen haben und zur Erde zurückgekehrt sind, um ihren Schöpfer zu finden.", List.of(SCIENCE_FICTION, ACTION, DRAMA)));
        movies.add(new Movie("Cool World", "Eine Comic-Strip-Femme-Fatale versucht, ihren Zeichner zu verführen, um in die reale Welt zu gelangen.", List.of(ANIMATION, FANTASY)));
        movies.add(new Movie("Dune", "Eine Adelsfamilie wird in einen Krieg um die Kontrolle über das wertvollste Gut der Galaxis verwickelt, während ihr Erbe von Visionen einer dunklen Zukunft geplagt wird.", List.of(SCIENCE_FICTION, DRAMA, ACTION)));
        movies.add(new Movie("E.T. - Der Außerirdische", "Ein schüchterner Junge muss seinen ganzen Mut zusammennehmen, um seinem außerirdischen Freund dabei zu helfen, die Erde zu verlassen und nach Hause zurückzukehren.", List.of(SCIENCE_FICTION, ADVENTURE)));
        movies.add(new Movie("Fast & Furious", "Brian O'Conner arbeitet wieder für das FBI in Los Angeles und verbündet sich mit Dominic Toretto, um die Organisation eines Heroinschmugglers zu infiltrieren und diesen dadurch zu Fall zu bringen.", List.of(ACTION, THRILLER)));
        movies.add(new Movie("Ghostbusters", "Drei ehemalige Professoren für Parapsychologie machen sich selbständig und bieten einen einzigartigen Geisterbeseitigungsservice an.", List.of(ACTION, COMEDY, SCIENCE_FICTION)));
        movies.add(new Movie("Heat", "Eine Gruppe professioneller Bankräuber hat die Polizei an den Fersen, als sie bei ihrem letzten Raubzug versehentlich eine heiße Spur hinterlassen.", List.of(ACTION, CRIME , DRAMA)));
        movies.add(new Movie("Inception", "Ein Dieb stiehlt Unternehmensgeheimnisse mithilfe einer Technologie für gemeinsames Träumen. Dann erhält er den Auftrag, eine Idee im Kopf eines Geschäftsführers festzusetzen.", List.of(ACTION, SCIENCE_FICTION, THRILLER)));
        movies.add(new Movie("Justice League", "Angetrieben von seinem wiederhergestellten Glauben an das Gute im Menschen und inspiriert von Supermans selbstlosen Taten, gewinnt Bruce Wayne die Unterstützung seiner neuen Verbündeten Diana Prince, um einem noch größeren Feind die Stirn zu bieten.", List.of(ACTION, FANTASY)));

        return movies;

    }
}
