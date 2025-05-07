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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:file:./db/fhmdb";
    @Getter
    private ConnectionSource connectionSource = null;
    private final Dao<WatchlistMovieEntity, Long> watchlistMovieDao;
    private final Dao<MovieEntity, Long> movieDao;
    private static DatabaseManager instance;

    public static DatabaseManager getInstance() throws DataBaseException {
        if (instance == null) {
            try
            {
                instance = new DatabaseManager();
            }
            catch (DataBaseException e){
                System.out.println("Error instantiating DatabaseManager");
            }
        }
        return instance;
    }


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

    private void createTables() throws SQLException
    {
        // 1. Versuch: ORMLite Standard-Erstellung
        try {
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        } catch (SQLException e) {
            System.err.println("ORMLite Table-Creation failed");

            // 2. Versuch: Manuelle SQL-Erstellung
            try (Connection conn = connectionSource.getReadWriteConnection(DB_URL).getUnderlyingConnection(); Statement stmt = conn.createStatement())
            {
                stmt.execute("CREATE TABLE IF NOT EXISTS WATCHLIST (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "apiId VARCHAR(36) NOT NULL UNIQUE)");

                stmt.execute("CREATE TABLE IF NOT EXISTS MOVIES (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "apiId VARCHAR(36) NOT NULL UNIQUE, " +
                        "title VARCHAR(255) NOT NULL, " +
                        "description CLOB, " +
                        "genres VARCHAR(255), " +
                        "releaseYear INT, " +
                        "imgUrl VARCHAR(255), " +
                        "lengthInMinutes INT, " +
                        "rating DOUBLE)");
            }
        }
    }

    private void dropTables() throws SQLException {
        TableUtils.dropTable(connectionSource, WatchlistMovieEntity.class, true);
        TableUtils.dropTable(connectionSource, MovieEntity.class, true);
    }
}
