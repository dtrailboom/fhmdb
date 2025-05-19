package at.ac.fhcampuswien.fhmdb.persistence;

import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import com.j256.ormlite.dao.Dao;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import lombok.Getter;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository implements Observable {
    private final Dao<WatchlistMovieEntity, Long> dao;
    private static WatchlistRepository instance = null;
    private InvalidationListener listener;
    @Getter
    private String message;

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
            notifyListener("Movie added to watchlist");
        } catch (SQLException e) {
            throw new DataBaseException("Movie already added to watchlist");
        }
    }

    public int removeFromWatchlist(String apiId) throws DataBaseException {
        try {
            var foundList = dao.queryForEq("apiId", apiId);

            if (foundList.isEmpty()) {
                notifyListener("Movie removed from watchlist");
                return 0;
            } else {
                WatchlistMovieEntity first = foundList.get(0);
                notifyListener("Movie removed from watchlist");
                return dao.delete(first);
            }
        } catch (SQLException e) {
            throw new DataBaseException("Could not delete entries in watchlist");
        }
    }

    public void clearWatchlist() throws DataBaseException {
        try {
           dao.deleteBuilder().delete();
            notifyListener("Watchlist cleared");
        } catch (SQLException e) {
            throw new DataBaseException("Could not delete entries in watchlist");
        }
    }

    @Override
    public void addListener(InvalidationListener listener) {
        this.listener = listener;
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        this.listener = null;
    }

    private void notifyListener(String message) {
        if (listener != null) {
            this.message = message;
            listener.invalidated(this);
        }
    }
}
