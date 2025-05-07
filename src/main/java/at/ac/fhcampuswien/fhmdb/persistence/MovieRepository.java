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

    public MovieRepository() throws DataBaseException {
        try {
            databaseManager = new DatabaseManager();

        } catch (DataBaseException e) {
            throw new DataBaseException(e.getMessage());
        }
    }


    //Get movies from db
    public List<MovieEntity> getAllMovies() throws SQLException {
        return databaseManager.movieDao.queryForAll();
    }

    //insert or update
    //return: number of changed Datasets
    public int addAllMovies(List<Movie> moviesToAdd) throws SQLException {
        List<MovieEntity> movieEntityList = MovieEntity.fromMovies(moviesToAdd);
        int count=0;
        for (MovieEntity movieEntity : movieEntityList) {
            Dao.CreateOrUpdateStatus ret = databaseManager.movieDao.createOrUpdate(movieEntity);
            count++;
        }
        return count;
    }

    public int removeAll() throws SQLException {
        List<MovieEntity> listToDelete = getAllMovies();
        return databaseManager.movieDao.delete(listToDelete);
    }
}
