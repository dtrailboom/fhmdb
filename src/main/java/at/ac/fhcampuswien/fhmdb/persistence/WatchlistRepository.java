package at.ac.fhcampuswien.fhmdb.persistence;

import at.ac.fhcampuswien.fhmdb.domain.MovieEntity;
import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private static final String DB_URL = "jdbc:h2:file:./db/fhmdb";     //besser irgendwo public
    private ConnectionSource connectionSource = null;
    private Dao<WatchlistMovieEntity, Integer> dao;

    public WatchlistRepository() throws DataBaseException, SQLException {
        createConnectionSource();
        dao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
    }

    private void createConnectionSource() throws DataBaseException {
        try {
            connectionSource = new JdbcConnectionSource(DB_URL);
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    //get all watchlist movies from db
    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return dao.queryForAll();
    }

    //insert or update
    public void addToWatchlist(WatchlistMovieEntity entityToAdd) throws SQLException {
        dao.createOrUpdate(entityToAdd);
    }

    //delete all WatchlistMovieEntity from list
    public int removefromWatchlist(String apiId) throws SQLException {
        //load entry
        List<WatchlistMovieEntity> foundList = dao.queryForEq("apiId", apiId);

        //if not found then return 0
        if (foundList.isEmpty() == true){
            return 0;
        } else {
            WatchlistMovieEntity first = foundList.getFirst();     //there can only be one because of id

            //first delete
            return dao.delete(first);
        }
    }
}
