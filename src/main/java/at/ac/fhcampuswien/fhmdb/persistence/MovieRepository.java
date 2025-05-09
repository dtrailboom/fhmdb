package at.ac.fhcampuswien.fhmdb.persistence;

import at.ac.fhcampuswien.fhmdb.domain.MovieEntity;
import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import lombok.Singular;
import org.openapitools.client.model.Movie;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private Dao<MovieEntity, Long> dao;
    private static MovieRepository instance;

    public MovieRepository() throws DataBaseException {
        dao = DatabaseManager.getInstance().getMovieDao();
    }

    public static MovieRepository getInstance() throws DataBaseException {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    public List<MovieEntity> getAllMovies() throws DataBaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DataBaseException("");
        }
    }

    public int addAllMovies(List<Movie> moviesToAdd) throws DataBaseException {
        List<MovieEntity> movieEntityList = MovieEntity.fromMovies(moviesToAdd);
        int count = 0;
        try {
            for (MovieEntity movieEntity : movieEntityList) {
                var status = dao.createOrUpdate(movieEntity);
                if (status.isCreated() || status.isUpdated()) {
                    count++;
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException("Error while adding movie" + e.getMessage());
        }
        return count;
    }

    public int removeAll() throws DataBaseException {
        var listToDelete = getAllMovies();
        try {
            return dao.delete(listToDelete);
        } catch (SQLException e) {
            throw new DataBaseException("Error while removing movies" + e.getMessage());
        }
    }

    public MovieEntity getMovieByApiId(String apiId) throws DataBaseException {
        try {
            return dao.queryBuilder()
                    .where().eq("apiId", apiId)
                    .queryForFirst();
        } catch (SQLException e) {
            throw new DataBaseException("Error while getting movie by apiId");
        }
    }
}
