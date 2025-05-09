package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.domain.MovieEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.Movie;

import java.sql.SQLException;
import java.util.*;

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
    public void getMostPopularActor_MultipleMovies_ActorAIsMostPopular() {  // Ob "Actor A" korrekt als häufigster Schauspieler zurückgegeben wird.
        Movie movie1 = new Movie().title("Movie 1").mainCast(List.of("Actor A", "Actor B", "Actor C"));
        Movie movie2 = new Movie().title("Movie 2").mainCast(List.of("Actor A", "Actor D", "Actor E"));
        Movie movie3 = new Movie().title("Movie 3").mainCast(List.of("Actor A", "Actor B", "Actor F"));
        List<Movie> movies = List.of(movie1, movie2, movie3);

        String result = homeController.getMostPopularActor(movies);
        assertEquals("Actor A", result); // Actor A kommt am häufigsten vor
    }

    @Test
    public void getMostPopularActor_TieBetweenActors_ReturnsRandomActor() {
        Movie movie1 = new Movie().title("Movie 1").mainCast(List.of("Actor A", "Actor B"));
        Movie movie2 = new Movie().title("Movie 2").mainCast(List.of("Actor A", "Actor B"));
        Movie movie3 = new Movie().title("Movie 3").mainCast(List.of("Actor C")); // Nur einmal vorkommend
        List<Movie> movies = List.of(movie1, movie2, movie3);

        String result = homeController.getMostPopularActor(movies);
        assertTrue(result.equals("Actor A") || result.equals("Actor B"));
    }

    @Test
    public void getMostPopularActor_EmptyList_ReturnsNull() {
        assertNull(homeController.getMostPopularActor(List.of()));
    }

    @Test
    public void getMostPopularActor_NullList_ReturnsNull() {
        assertNull(homeController.getMostPopularActor(null));
    }

    @Test
    public void getMostPopularActor_OneMovieOneActor_ReturnsActor() {  // Ob die Methode funktioniert, wenn nur ein Schauspieler existiert.
        Movie movie = new Movie().title("Movie").mainCast(List.of("Solo Actor"));
        List<Movie> movies = List.of(movie);

        assertEquals("Solo Actor", homeController.getMostPopularActor(movies));
    }

    @Test
    public void getMostPopularActor_MovieWithoutActors_ReturnsNull() {  // Ob die Methode robust ist, wenn ein Film keinen mainCast hat.
        Movie movie = new Movie().mainCast(List.of());
        List<Movie> movies = List.of(movie);

        assertNull(homeController.getMostPopularActor(movies));
    }

    @Test
    public void getMostPopularActor_ListWithNullMovies_ReturnsValid() { // Falls die Liste Filme enthält, aber einige null sind, sollte das keine Fehler verursachen.
        Movie movie = new Movie().mainCast(List.of("Actor A"));
        List<Movie> movies = Arrays.asList(movie, null);

        assertEquals("Actor A", homeController.getMostPopularActor(movies));
    }

    @Test
    public void getMostPopularActor_MovieNullMainCast_NoErrorAndReturnsCorrectResult() { // Falls ein Film ein null-Feld für mainCast hat, sollte das keine NullPointerException verursachen.

        Movie movie1 = new Movie().title("Movie 1").mainCast(List.of("Actor A", "Actor B"));
        Movie movie2 = new Movie().title("Movie 2");  // movie2.mainCast bleibt null
        Movie movie3 = new Movie().title("Movie 3").mainCast(List.of("Actor A"));
        List<Movie> movies = List.of(movie1, movie2, movie3);

        String result = homeController.getMostPopularActor(movies);
        assertEquals("Actor A", result); // Actor A kommt am häufigsten vor

    }

    @Test
    public void getMostPopularActor_MixedNullAndEmptyMainCasts_IgnoredCorrectly() {
        Movie movie1 = new Movie().mainCast(List.of("Actor A", "Actor B"));
        Movie movie2 = new Movie().mainCast(null); // null mainCast
        Movie movie3 = new Movie().mainCast(List.of()); // leere Liste
        Movie movie4 = null; // Film selbst ist null
        Movie movie5 = new Movie().mainCast(List.of("Actor A"));

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4, movie5);

        String result = homeController.getMostPopularActor(movies);
        assertEquals("Actor A", result); // Actor A kommt am häufigsten vor
    }

    @Test
    public void getLongestMovieTitle() {  // Ob der größte movie title zurückgegeben wird.
        assertEquals(12, homeController.getLongestMovieTitle(movies));
    }

    @Test
    public void getLongestMovieTitle_LongestExists_LongestTitle() {  // Ob der größte movie title zurückgegeben wird.
        assertEquals(12, homeController.getLongestMovieTitle(movies));
    }

    @Test
    public void getLongestMovieTitle_SameLength_LongestTitle() {
        Movie testmovie1 = new Movie().title("11").description("Beschreibung von Blade Runner").genres(List.of(ACTION)).directors(List.of("Ridley Scott")).releaseYear(1982);
        Movie testmovie2 = new Movie().title("11111").description("Beschreibung von Cool World").genres(List.of(COMEDY)).directors(List.of("Ralph Bakshi")).releaseYear(1992);
        Movie testmovie3 = new Movie().title("11111").description("Beschreibung von Inception").genres(List.of(SCIENCE_FICTION)).directors(List.of("Christopher Nolan")).releaseYear(2010);
        List<Movie> tempMovies = new ArrayList<>();
        tempMovies = new ArrayList<>(List.of(testmovie1, testmovie2, testmovie3));
        assertEquals(5, homeController.getLongestMovieTitle(tempMovies));
    }

    @Test
    public void getLongestMovieTitle_ZeroLength_LongestTitle() {
        Movie testmovie1 = new Movie().title("").description("Beschreibung von Blade Runner").genres(List.of(ACTION)).directors(List.of("Ridley Scott")).releaseYear(1982);
        Movie testmovie2 = new Movie().title("").description("Beschreibung von Cool World").genres(List.of(COMEDY)).directors(List.of("Ralph Bakshi")).releaseYear(1992);
        Movie testmovie3 = new Movie().title("").description("Beschreibung von Inception").genres(List.of(SCIENCE_FICTION)).directors(List.of("Christopher Nolan")).releaseYear(2010);
        List<Movie> tempMovies = new ArrayList<>();
        tempMovies = new ArrayList<>(List.of(testmovie1, testmovie2, testmovie3));
        assertEquals(0, homeController.getLongestMovieTitle(tempMovies));
    }

    @Test
    public void genresToString_MultipleGenres_ReturnsCommaSeparatedString() {
        List<Movie.GenresEnum> genres = List.of(
                Movie.GenresEnum.ACTION,
                Movie.GenresEnum.DRAMA,
                Movie.GenresEnum.COMEDY
        );

        String result = MovieEntity.genresToString(genres);
        assertEquals("ACTION,DRAMA,COMEDY", result);
    }

    @Test
    public void genresToString_EmptyList_ReturnsEmptyString() {
        List<Movie.GenresEnum> genres = Collections.emptyList();

        String result = MovieEntity.genresToString(genres);
        assertEquals("", result);
    }

    @Test
    public void genresToString_SingleGenre_ReturnsSingleGenreString() {
        List<Movie.GenresEnum> genres = List.of(Movie.GenresEnum.HORROR);

        String result = MovieEntity.genresToString(genres);
        assertEquals("HORROR", result);
    }

    @Test
    public void genresToString_NullInput_ReturnsEmptyString() {
        List<Movie.GenresEnum> genres = null;

        String result = MovieEntity.genresToString(genres);
        assertEquals("", result);
    }

    @Test
    void toMovies_ParsesGenresCorrectly() {
        MovieEntity entity = MovieEntity.builder()
                .apiId(UUID.randomUUID().toString())
                .genres("ACTION,SCIENCE_FICTION")
                .build();

        List<Movie> movies = MovieEntity.toMovies(Collections.singletonList(entity));
        List<Movie.GenresEnum> genres = movies.get(0).getGenres();

        assertNotNull(genres);
        assertTrue(genres.contains(Movie.GenresEnum.ACTION));
        assertTrue(genres.contains(Movie.GenresEnum.SCIENCE_FICTION));
    }


    @Test
    void insertMovieEntity_GeneratesAutoIncrementId() throws Exception {

        // Verbindung zur eingebetteten H2-In-Memory-Datenbank
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:mem:testdb");
        Dao<MovieEntity, Long> movieDao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, MovieEntity.class);

        // Tabelle erstellen
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);

        // Arrange

        MovieEntity entity = MovieEntity.builder()
                .apiId(UUID.randomUUID().toString())
                .title("Inception")
                .description("A mind-bending thriller")
                .genres("ACTION,SCIENCE_FICTION")
                .releaseYear(2010)
                .imgUrl("https://example.com/inception.jpg")
                .lengthInMinutes(148)
                .rating(8.8)
                .build();

        // Act
        movieDao.create(entity); // wird gespeichert und ID generiert

        // Assert
        assertTrue(entity.getId() > 0, "ID should be > 0 after insert");

        // Optional: Prüfe, ob der Datensatz korrekt in der DB ist
        List<MovieEntity> movies = movieDao.queryForAll();
        assertEquals(1, movies.size());
        assertEquals(entity.getTitle(), movies.get(0).getTitle());
        System.out.println("Gespeicherter Datensatz mit ID: " + entity.getId());
        connectionSource.close();
    }

    @Test
    void insertMovieEntity_IdIsGenerated() throws Exception {
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:mem:testdb1");
        Dao<MovieEntity, Long> movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);

        MovieEntity entity = MovieEntity.builder()
                .apiId(UUID.randomUUID().toString())
                .title("Inception")
                .build();

        movieDao.create(entity);

        assertTrue(entity.getId() > 0, "ID sollte > 0 sein");

        connectionSource.close();
    }

    @Test
    void insertMultipleEntities_HaveIncrementingIds() throws Exception {
        // Eigene Datenbankverbindung pro Test
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:mem:testdb2");
        Dao<MovieEntity, Long> movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        // Arrange
        MovieEntity entity1 = MovieEntity.builder()
                .apiId(UUID.randomUUID().toString())
                .title("Movie One")
                .build();

        MovieEntity entity2 = MovieEntity.builder()
                .apiId(UUID.randomUUID().toString())
                .title("Movie Two")
                .build();

        // Act
        movieDao.create(entity1);
        movieDao.create(entity2);

        // Assert
        assertTrue(entity1.getId() < entity2.getId(), "IDs sollten aufsteigend sein");

        System.out.println("ID 1: " + entity1.getId());
        System.out.println("ID 2: " + entity2.getId());

        connectionSource.close();
    }

    @Test
    void insertMovieEntity_IsPersisted() throws Exception {
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:mem:testdb2");
        Dao<MovieEntity, Long> movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);

        MovieEntity entity = MovieEntity.builder()
                .apiId(UUID.randomUUID().toString())
                .title("Inception")
                .build();
        movieDao.create(entity);

        List<MovieEntity> movies = movieDao.queryForAll();
        assertEquals(1, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());

        connectionSource.close();
    }

    @Test
    void insertMovieEntity_CompleteDataCheck() throws Exception {
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:mem:testdb3");
        Dao<MovieEntity, Long> movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);

        MovieEntity entity = MovieEntity.builder()
                .apiId(UUID.randomUUID().toString())
                .title("Inception")
                .description("A mind-bending thriller")
                .genres("ACTION,SCIENCE_FICTION")
                .releaseYear(2010)
                .imgUrl("https://example.com/inception.jpg")
                .lengthInMinutes(148)
                .rating(8.8)
                .build();

        movieDao.create(entity);

        MovieEntity saved = movieDao.queryForId(entity.getId());
        assertEquals("Inception", saved.getTitle());
        assertEquals("A mind-bending thriller", saved.getDescription());
        assertEquals("ACTION,SCIENCE_FICTION", saved.getGenres());
        assertEquals(2010, saved.getReleaseYear());
        assertEquals("https://example.com/inception.jpg", saved.getImgUrl());
        assertEquals(148, saved.getLengthInMinutes());
        assertEquals(8.8, saved.getRating());

        connectionSource.close();
    }

    @Test
    void fromMovies_ValidMovieList_ReturnsCorrectSize() {

        List<Movie> movies = List.of(
                new Movie().id(UUID.randomUUID()).title("A").description("Nice movie").genres(List.of(ACTION, ADVENTURE)).releaseYear(2000).lengthInMinutes(120).rating(8.5),
                new Movie().id(UUID.randomUUID()).title("B").description("Hard core").genres(List.of(ADVENTURE)).releaseYear(2001).lengthInMinutes(100).rating(7.5)
        );
        System.out.println("Debug: Movie-Liste: " + movies);

        List<MovieEntity> entities = MovieEntity.fromMovies(movies);
        System.out.println("Debug: Ergebnismenge von MovieEntity.fromMovies(): " + entities);

        assertThat(entities).hasSize(2);
    }

    @Test
    public void fromMovies_ValidMovieList_ReturnsCorrectMovieEntities() {
        // Arrange - Beispiel Objekte erstellen
        Movie movie1 = new Movie()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .title("Inception")
                .description("Sci-Fi Thriller")
                .genres(List.of(GenresEnum.SCIENCE_FICTION, GenresEnum.THRILLER))
                .releaseYear(2010)
                .imgUrl("url1")
                .lengthInMinutes(148)
                .rating(8.8);

        System.out.println("Debug: Erstes Movie erstellt: " + movie1);

        Movie movie2 = new Movie()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .title("Titanic")
                .description("Romantic Drama")
                .genres(List.of(GenresEnum.ROMANCE, GenresEnum.DRAMA))
                .releaseYear(1997)
                .imgUrl("url2")
                .lengthInMinutes(195)
                .rating(7.8);

        System.out.println("Debug: Zweites Movie erstellt: " + movie2);

        List<Movie> movies = Arrays.asList(movie1, movie2);
        System.out.println("Debug: Movie-Liste: " + movies);

        // Act - Methode aufrufen
        List<MovieEntity> entities = MovieEntity.fromMovies(movies);
        System.out.println("Debug: Ergebnismenge von MovieEntity.fromMovies(): " + entities);

        // Assert - Ergebnisse prüfen
        assertEquals(2, entities.size());

        MovieEntity entity1 = entities.get(0);
        System.out.println("Debug: Erste Entity: " + entity1);
        assertEquals("00000000-0000-0000-0000-000000000001", entity1.getApiId());
        assertEquals("Inception", entity1.getTitle());
        assertEquals("Sci-Fi Thriller", entity1.getDescription());
        assertEquals("SCIENCE_FICTION,THRILLER", entity1.getGenres());
        assertEquals(2010, entity1.getReleaseYear());
        assertEquals("url1", entity1.getImgUrl());
        assertEquals(148, entity1.getLengthInMinutes());
        assertEquals(8.8, entity1.getRating());

        MovieEntity entity2 = entities.get(1);
        System.out.println("Debug: Zweite Entity: " + entity2);
        assertEquals("00000000-0000-0000-0000-000000000002", entity2.getApiId());
        assertEquals("Titanic", entity2.getTitle());
        assertEquals("Romantic Drama", entity2.getDescription());
        assertEquals("ROMANCE,DRAMA", entity2.getGenres());
        assertEquals(1997, entity2.getReleaseYear());
        assertEquals("url2", entity2.getImgUrl());
        assertEquals(195, entity2.getLengthInMinutes());
        assertEquals(7.8, entity2.getRating());
    }

    @Test
    void toMovies_ValidEntity_ReturnsCorrectMovieObject() {
        MovieEntity entity = MovieEntity.builder()
                .apiId(UUID.randomUUID().toString())
                .title("Inception")
                .description("A mind-bending thriller")
                .genres("ACTION,SCIENCE_FICTION")
                .releaseYear(2010)
                .imgUrl("https://example.com/inception.jpg")
                .lengthInMinutes(148)
                .rating(8.8)
                .build();

        System.out.println("Debug: MovieEntity: " + entity);
        System.out.println("ID: " + entity.getId());

        List<MovieEntity> entities = Collections.singletonList(entity);
        System.out.println("Debug: MovieEntity-Liste: " + entities);

        // Act
        List<Movie> movies = MovieEntity.toMovies(entities);
        System.out.println("Debug: Movie-Liste: " + movies);

        // Assert
        assertEquals(1, movies.size());

        Movie movie = movies.get(0);
        assertEquals(UUID.fromString(entity.getApiId()), movie.getId());
        assertEquals(entity.getTitle(), movie.getTitle());
        assertEquals(entity.getDescription(), movie.getDescription());
        assertEquals(entity.getReleaseYear(), movie.getReleaseYear());
        assertEquals(entity.getImgUrl(), movie.getImgUrl());
        assertEquals(entity.getLengthInMinutes(), movie.getLengthInMinutes());
        assertEquals(entity.getRating(), movie.getRating());

        // Test genre conversion
        List<Movie.GenresEnum> genres = movie.getGenres();
        System.out.println("Debug: Genres: " + genres);
        assertNotNull(genres);
        assertTrue(genres.contains(Movie.GenresEnum.ACTION));
        assertTrue(genres.contains(Movie.GenresEnum.SCIENCE_FICTION));
    }

    @Test
    void toMovies_SingleEntity_ReturnsSingleMovie() {
        MovieEntity entity = MovieEntity.builder()
                .apiId(UUID.randomUUID().toString())
                .title("Inception")
                .build();

        List<MovieEntity> entities = Collections.singletonList(entity);
        List<Movie> movies = MovieEntity.toMovies(entities);

        assertEquals(1, movies.size());
    }

    @Test
    void toMovies_MapsBasicFieldsCorrectly() {
        UUID apiId = UUID.randomUUID();
        MovieEntity entity = MovieEntity.builder()
                .apiId(apiId.toString())
                .title("Inception")
                .description("A mind-bending thriller")
                .releaseYear(2010)
                .imgUrl("https://example.com/inception.jpg")
                .lengthInMinutes(148)
                .rating(8.8)
                .build();

        List<Movie> movies = MovieEntity.toMovies(Collections.singletonList(entity));
        Movie movie = movies.get(0);

        assertEquals(apiId, movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals("A mind-bending thriller", movie.getDescription());
        assertEquals(2010, movie.getReleaseYear());
        assertEquals("https://example.com/inception.jpg", movie.getImgUrl());
        assertEquals(148, movie.getLengthInMinutes());
        assertEquals(8.8, movie.getRating());
    }

    @Test
    public void toMovies_EmptyList_ReturnsEmptyList() {
        // Arrange - Leere Liste vorbereiten
        List<MovieEntity> entities = Collections.emptyList();
        // Act - Methode aufrufen
        List<Movie> movies = MovieEntity.toMovies(entities);
        assertTrue(movies.isEmpty());
    }

    @Test
    public void toMovies_NullGenres_ReturnsMovieWithEmptyGenreList() {
        // Arrange - Eine Entity mit null bei Genres
        MovieEntity entity = MovieEntity.builder()
                .apiId("00000000-0000-0000-0000-000000000003")
                .title("Null Genre Movie")
                .description("This movie has no genres set.")
                .genres(null)
                .releaseYear(2022)
                .imgUrl("url3")
                .lengthInMinutes(120)
                .rating(5.5)
                .build();

        List<MovieEntity> entities = Collections.singletonList(entity);

        // Act
        List<Movie> movies = MovieEntity.toMovies(entities);

        // Assert
        assertEquals(1, movies.size());
        Movie movie = movies.get(0);
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000003"), movie.getId());
        assertEquals("Null Genre Movie", movie.getTitle());
        assertEquals("This movie has no genres set.", movie.getDescription());
        assertNotNull(movie.getGenres());
        assertTrue(movie.getGenres().isEmpty());  // Leere Genre-Liste
        assertEquals(2022, movie.getReleaseYear());
        assertEquals("url3", movie.getImgUrl());
        assertEquals(120, movie.getLengthInMinutes());
        assertEquals(5.5, movie.getRating(), 0.01);
    }
}