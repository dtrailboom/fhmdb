package at.ac.fhcampuswien.fhmdb.persistence;

import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private final Dao<WatchlistMovieEntity, Long> dao;
    private static WatchlistRepository instance = null;

    private WatchlistRepository() throws DataBaseException {
        dao = DatabaseManager.getInstance().getWatchlistMovieDao();
    }

    public static WatchlistRepository getInstance() throws DataBaseException {
        if (instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DataBaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DataBaseException("");
        }
    }

    public void addToWatchlist(WatchlistMovieEntity entityToAdd) throws DataBaseException {
        try {
            dao.createOrUpdate(entityToAdd);
        } catch (SQLException e) {
            throw new DataBaseException("");
        }
    }

    public int removeFromWatchlist(String apiId) throws DataBaseException {
        try {
            var foundList = dao.queryForEq("apiId", apiId);

            if (foundList.isEmpty()) {
                return 0;
            } else {
                WatchlistMovieEntity first = foundList.get(0);
                return dao.delete(first);
            }
        } catch (SQLException e) {
            throw new DataBaseException("Could not delete entries in watchlist");
        }
    }

    public void clearWatchlist() throws DataBaseException {
        try {
           dao.deleteBuilder().delete();
        } catch (SQLException e) {
            throw new DataBaseException("Could not delete entries in watchlist");
        }
    }
}
