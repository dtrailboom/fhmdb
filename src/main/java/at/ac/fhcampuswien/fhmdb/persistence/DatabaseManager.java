package at.ac.fhcampuswien.fhmdb.persistence;

import at.ac.fhcampuswien.fhmdb.domain.MovieEntity;
import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;

import java.sql.SQLException;

@Getter
public class DatabaseManager {
    public static final String DB_URL = "jdbc:h2:file:./db/fhmdb";
    private ConnectionSource connectionSource = null;
    public final Dao<WatchlistMovieEntity, Long> watchlistMovieDao;
    public final Dao<MovieEntity, Long> movieDao;

    //private Instanzpointer zeigt auf sich selbst
    //private static DatabaseManager myInstance;


//    public static DatabaseManager getInstance() throws DataBaseException {
//        if (myInstance == null) {
//            myInstance= new DatabaseManager();
//        }
//        return myInstance;
//    }

    //private DatabaseManager() throws DataBaseException {
    public DatabaseManager() throws DataBaseException {
        try {
            createConnectionSource();
            watchlistMovieDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            createTables();
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    private void createConnectionSource() throws DataBaseException {
        try {
            connectionSource = new JdbcConnectionSource(DB_URL);
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
    }

    private void dropTables() throws SQLException {
        TableUtils.dropTable(connectionSource, WatchlistMovieEntity.class, true);
        TableUtils.dropTable(connectionSource, MovieEntity.class, true);
    }
}
