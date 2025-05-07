package at.ac.fhcampuswien.fhmdb.persistence;

import at.ac.fhcampuswien.fhmdb.domain.MovieEntity;
import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.openapitools.client.model.Movie;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private DatabaseManager databaseManager;
    private static final String DB_URL = "jdbc:h2:file:./db/fhmdb";     //besser irgendwo public
    private ConnectionSource connectionSource = null;
    private Dao<MovieEntity, Integer> dao;
    private static MovieRepository instance;


    public MovieRepository() throws DataBaseException, SQLException {

        createConnectionSource();
        dao = DaoManager.createDao(connectionSource, MovieEntity.class);
    }

    public static MovieRepository getInstance()  {

        if (instance == null)
        {
            try
            {
                instance = new MovieRepository();
            }
            catch (SQLException | DataBaseException e) {
                System.out.println("Error getting instance of MovieRepository: " + e.getMessage());
            }
        }
        return instance;
    }

    private void createConnectionSource() throws DataBaseException {
        try {
            databaseManager = DatabaseManager.getInstance();
            connectionSource= databaseManager.getConnectionSource();

        } catch (DataBaseException e) {
            throw new DataBaseException(e.getMessage());
        }
    }


    //Get movies from db
    public List<MovieEntity> getAllMovies() throws SQLException {
        return databaseManager.getMovieDao().queryForAll();
    }

    //insert or update
    //return: number of changed Datasets
    public int addAllMovies(List<Movie> moviesToAdd) throws SQLException {
        List<MovieEntity> movieEntityList = MovieEntity.fromMovies(moviesToAdd);
        int count=0;
        for (MovieEntity movieEntity : movieEntityList) {
            Dao.CreateOrUpdateStatus ret = databaseManager.getMovieDao().createOrUpdate(movieEntity);
            count++;
        }
        return count;
    }

    public int removeAll() throws SQLException {
        List<MovieEntity> listToDelete = getAllMovies();
        return databaseManager.getMovieDao().delete(listToDelete);
    }

    public MovieEntity getMovieByApiId(String apiId) throws SQLException {
        return dao.queryBuilder()
                .where().eq("apiId", apiId)
                .queryForFirst();
    }
}
