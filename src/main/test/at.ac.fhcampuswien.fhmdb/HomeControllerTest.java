package at.ac.fhcampuswien.fhmdb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.openapitools.client.model.Movie.GenresEnum.*;
import static org.openapitools.client.model.Movie.GenresEnum;

class HomeControllerTest {
    private HomeController homeController;
    private final Movie bladeRunner = new Movie().title("Blade Runner").description("Beschreibung von Blade Runner").genres(List.of(ACTION)).directors(List.of("Ridley Scott")).releaseYear(1982);
    private final Movie coolWorld = new Movie().title("Cool World").description("Beschreibung von Cool World").genres(List.of(COMEDY)).directors(List.of("Ralph Bakshi")).releaseYear(1992);
    private final Movie inception = new Movie().title("Inception").description("Beschreibung von Inception").genres(List.of(SCIENCE_FICTION)).directors(List.of("Christopher Nolan")).releaseYear(2010);
    List<Movie> movies = new ArrayList<>();

    @BeforeEach
    void setUp() {

        homeController = new HomeController();
        movies = new ArrayList<>(List.of(bladeRunner, coolWorld, inception));
    }

    //-----SORTING------//

    @Test
    void sortMoviesAsc() {
        var expected = List.of(bladeRunner, coolWorld, inception);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void sortMoviesDesc() {
        var expected = List.of(inception, coolWorld, bladeRunner);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_EmptyList() {
        assertDoesNotThrow(() -> homeController.sortMovies(List.of(), false));
    }

    @Test
    void sortMovies_CaseInsensitive_asc() {
        Movie movie1 = new Movie().title("Apple").description("An Apple is tasty!").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("banana").description("banana").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_CaseInsensitive_desc() {
        Movie movie1 = new Movie().title("Apple").description("An Apple is tasty!").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("banana").description("banana").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_StableSort_asc() {
        Movie movie1 = new Movie().title("Inception").description("Test Description").genres(List.of(SCIENCE_FICTION));
        Movie movie2 = new Movie().title("Inception").description("Test Description").genres(List.of(SCIENCE_FICTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);
        assertEquals(expected, result);
    }

    @Test
    void sortMovies_StableSort_desc() {
        Movie movie1 = new Movie().title("Inception").description("Test Description").genres(List.of(SCIENCE_FICTION));
        Movie movie2 = new Movie().title("Inception").description("Test Description").genres(List.of(SCIENCE_FICTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_EmptyTitles_asc() {
        Movie movie1 = new Movie().title("").description("Test Description").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("Blade Runner").description("Test Description").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_EmptyTitles_desc() {
        Movie movie1 = new Movie().title("").description("Test Description").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("Blade Runner").description("Test Description").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_NonAlphanumericCharacters_asc() {
        Movie movie1 = new Movie().title("@Movie").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("!Movie").description("Description 2").genres(List.of(ACTION));
        Movie movie3 = new Movie().title("?Movie").description("Description 3").genres(List.of(ACTION));
        movies = List.of(movie2, movie3, movie1);
        var expected = List.of(movie2, movie3, movie1);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_NonAlphanumericCharacters_desc() {
        Movie movie1 = new Movie().title("@Movie").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("!Movie").description("Description 2").genres(List.of(ACTION));
        Movie movie3 = new Movie().title("?Movie").description("Description 3").genres(List.of(ACTION));
        movies = List.of(movie2, movie3, movie1);
        var expected = List.of(movie1, movie3, movie2);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_LongTitles_asc() {
        Movie movie1 = new Movie().title("A Very Long Movie Title That Tests Sorting").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("Another Long Movie Title").description("Description 2").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, false);
        var expected = List.of(movie1, movie2);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_LongTitles_desc() {
        Movie movie1 = new Movie().title("A Very Long Movie Title That Tests Sorting").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("Another Long Movie Title").description("Description 2").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);
        var expected = List.of(movie2, movie1);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_SpecialCharacters_asc() {
        Movie movie1 = new Movie().title("#Special Movie").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("123 Movie").description("Description 2").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, false);
        var expected = List.of(movie1, movie2);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_SpecialCharacters_desc() {
        Movie movie1 = new Movie().title("#Special Movie").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("123 Movie").description("Description 2").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);
        var expected = List.of(movie2, movie1);

        assertEquals(expected, result);
    }
//--END--//

    //--FILTERING--//
    @Test
    void filterMovies_FilterBySearchText_True() {
        var result = homeController.filterMovies("inception", null, "", "");
        assertEquals("Inception", result.get(0).getTitle());
    }

    @Test
    void filterMovies_FilterByGenre_True() {
        var result = homeController.filterMovies("", GenresEnum.SCIENCE_FICTION, "", "");

        assertEquals(4, result.size());
    }

    @Test
    void filterMovies_FilterByYear_True() {
        var result = homeController.filterMovies("", null, "1995", "");

        assertEquals(3, result.size());
    }

    @Test
    void filterMovies_FilterByRating_True() {
        var result = homeController.filterMovies("", null, "", "9");
        assertEquals(3, result.size());
    }

    @Test
    void filterMovies_FilterBySearchAndGenre_True() {
        var result = homeController.filterMovies("The lion", FAMILY, "", "");
        assertEquals("The Lion King", result.get(0).getTitle());
    }

    @Test
    void filterMovies_FilterByAll_True() {
        var result = homeController.filterMovies("toy", GenresEnum.COMEDY, "1995", "8");

        assertEquals(1, result.size());
        assertEquals("Toy Story", result.get(0).getTitle());
    }

    @Test
    void filterMovies_FilterByWrongSearch_isEmpty() {
        var result = homeController.filterMovies("nonexistent", null, "", "");

        assertTrue(result.isEmpty());
    }

    @Test
    void filterMovies_FilterByNothing_getAll() {
        var result = homeController.filterMovies("", null, "", "");

        assertEquals(31, result.size());
    }

    @Test
    void filterMovies_FilterByWInvalidYear_getAll() {
        var result = homeController.filterMovies("", null, "abcd", "");
        assertEquals(31, result.size());
    }

    @Test
    void filterMovies_FilterByInvalidRating_getAll() {
        var result = homeController.filterMovies("", null, "", "notanumber");
        assertEquals(31, result.size());
    }

    @Test
    void filterMovies_FilterByUnknownYear_getAll() {
        var result = homeController.filterMovies("", null, "1800", "");

        assertEquals(31, result.size());
    }

    @Test
    void filterMovies_FilterByOutOfRangeRating_getAll() {
        var result = homeController.filterMovies("", null, "", "15");

        assertEquals(31, result.size());
    }
    //--END--//

    @Test
    void countMoviesFrom_oneDirector_isOne() {
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = 1;

        assertEquals(expected, homeController.countMoviesFrom(movies, "Christopher Nolan"));
    }

    @Test
    void countMoviesFrom_noDirector_isZero() {
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = 0;

        assertEquals(expected, homeController.countMoviesFrom(movies, ""));
    }

    @Test
    void countMoviesFrom_directorWithoutMovie_isZero() {
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = 0;

        assertEquals(expected, homeController.countMoviesFrom(movies, "Steve Jobs"));
    }

    @Test
    void countMoviesFrom_emptyMovieList_isZero() {
        var movies = new ArrayList<Movie>();
        var expected = 0;

        assertEquals(expected, homeController.countMoviesFrom(movies, "Christopher Nolan"));
    }

    @Test
    void getMoviesBetweenYears_emptyMovieList_isEmpty() {
        var movies = new ArrayList<Movie>();
        var expected = List.of();

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1990, 2004));
    }

    @Test
    void getMoviesBetweenYears_noMatch_isEmpty() {
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = List.of();

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1312, 1529));
    }

    @Test
    void getMoviesBetweenYears_allMatch_movieListIsSame() {
        var movies = List.of(bladeRunner, inception, coolWorld);

        assertEquals(movies, homeController.getMoviesBetweenYears(movies, 1900, 2100));
    }

    @Test
    void getMoviesBetweenYears_singleMatch_matchesMovie() {
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = List.of(coolWorld);

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1992, 1993));
    }

    @Test
    void getMoviesBetweenYears_startYearBiggerThanEndYear_noMatch() {
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = List.of();

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1993, 1992));
    }

    @Test
    void getMoviesBetweenYears_startYearEqualToEndYear_matchesMovie() {
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = List.of(coolWorld);

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1992, 1992));
    }

    //Year-Validation-Tests
    @Test
    void isValidYear_InboundYear_True() {
        var result = homeController.isValidYear("2000");
        assertEquals(2000, result);
    }

    @Test
    void isValidYear_OutboundYear_False() {
        var result = homeController.isValidYear("1800");
        assertNull(result);
    }

    @Test
    void isValidYear_WrongFormat_False() {
        var result = homeController.isValidYear("abc");
        assertNull(result);
    }


    //Rating-Validation-Tests
    @Test
    void isValidRating_InboundRating_True() {
        var result = homeController.isValidRating("7");
        assertEquals(7.0, result);
    }

    @Test
    void isValidYear_OutboundRating_False() {
        var result = homeController.isValidRating("14");
        assertNull(result);
    }

    @Test
    void isValidRating_WrongFormat_False() {
        var result = homeController.isValidRating("abc");
        assertNull(result);
    }

    //Most-Popular-Actor-Tests
    @Test
    public void getMostPopularActor() {  // Ob "Actor A" korrekt als häufigster Schauspieler zurückgegeben wird.
        Movie movie1 = new Movie().title("Movie 1").mainCast(List.of("Actor A", "Actor B", "Actor C"));
        Movie movie2 = new Movie().title("Movie 2").mainCast(List.of("Actor A", "Actor D", "Actor E"));
        Movie movie3 = new Movie().title("Movie 3").mainCast(List.of("Actor A", "Actor B", "Actor F"));
        List<Movie> movies = List.of(movie1, movie2, movie3);

        String result = homeController.getMostPopularActor(movies);
        assertEquals("Actor A", result); // Actor A kommt am häufigsten vor
    }

    @Test
    public void getLongestMovieTitle() {  // Ob der größte movie title zurückgegeben wird.
        assertEquals(12, homeController.getLongestMovieTitle(movies));
    }

    @Test
    public void getMostPopularActor_TieBetweenActors() {
        Movie movie1 = new Movie();
        movie1.setMainCast(Arrays.asList("Actor A", "Actor B"));

        Movie movie2 = new Movie();
        movie2.setMainCast(Arrays.asList("Actor A", "Actor B"));

        Movie movie3 = new Movie();
        movie3.setMainCast(Arrays.asList("Actor C")); // Nur einmal vorkommend

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3);

        String mostPopularActor = homeController.getMostPopularActor(movies);
        assertTrue(mostPopularActor.equals("Actor A") || mostPopularActor.equals("Actor B"));
        System.out.println(mostPopularActor);
    }

    @Test
    public void getMostPopularActor_EmptyList() {

        assertNull(homeController.getMostPopularActor(List.of()));
    }

    @Test
    public void getMostPopularActor_NullList() {

        assertNull(homeController.getMostPopularActor(null));
    }

    @Test

    public void getMostPopularActor_OneMovieOneActor() {  // Ob die Methode funktioniert, wenn nur ein Schauspieler existiert.
        Movie movie = new Movie();
        movie.setMainCast(Arrays.asList("Solo Actor"));

        List<Movie> movies = List.of(movie);

        assertEquals("Solo Actor", homeController.getMostPopularActor(movies));
    }

    @Test
    public void getMostPopularActor_MovieWithoutActors() {  // Ob die Methode robust ist, wenn ein Film keinen mainCast hat.
        Movie movie = new Movie();
        movie.setMainCast(List.of());

        List<Movie> movies = List.of(movie);

        assertNull(homeController.getMostPopularActor(movies));
    }

    @Test
    public void getMostPopularActor_ListWithNullMovies() { // Falls die Liste Filme enthält, aber einige null sind, sollte das keine Fehler verursachen.
        Movie movie = new Movie();
        movie.setMainCast(Arrays.asList("Actor A"));

        List<Movie> movies = Arrays.asList(movie, null);

        assertEquals("Actor A", homeController.getMostPopularActor(movies));
    }

    @Test
    public void getMostPopularActor_NullMainCast() { // Falls ein Film ein null-Feld für mainCast hat, sollte das keine NullPointerException verursachen.
        // Film 1 mit Schauspielern
        Movie movie1 = new Movie();
        movie1.setMainCast(Arrays.asList("Actor A", "Actor B"));

        // Film 2 mit keinem Schauspieler (null mainCast)
        Movie movie2 = new Movie();
        movie2.setMainCast(null);

        // Film 3 mit einem Schauspieler
        Movie movie3 = new Movie();
        movie3.setMainCast(Arrays.asList("Actor A")); // Schauspieler A auch hier

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3);

        String mostPopularActor = homeController.getMostPopularActor(movies);

        Assertions.assertEquals("Actor A", mostPopularActor); // Jetzt sollte Actor A der beliebteste Schauspieler sein

    }


}