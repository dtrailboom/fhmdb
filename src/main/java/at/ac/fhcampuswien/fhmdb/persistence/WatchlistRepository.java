package at.ac.fhcampuswien.fhmdb.persistence;

import at.ac.fhcampuswien.fhmdb.domain.MovieEntity;
import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private static final String DB_URL = "jdbc:h2:file:./db/fhmdb";     //besser irgendwo public
    private ConnectionSource connectionSource = null;
    private Dao<WatchlistMovieEntity, Integer> dao;
    private static WatchlistRepository instance;


    public int size() throws SQLException {
        return (int) dao.countOf();
    }

    public WatchlistRepository() throws DataBaseException {
        try {
            this.dao = DaoManager.createDao(
                    DatabaseManager.getInstance().getConnectionSource(), // Wiederverwendung des DB-Managers
                    WatchlistMovieEntity.class
            );
        } catch (SQLException e) {
            throw new DataBaseException("DAO creation failed", e);
        }
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
    public void addToWatchlist(WatchlistMovieEntity entity) throws SQLException {
        // check if movie already is in watchlist
        if (dao.queryForEq("apiId", entity.getApiId()).isEmpty()) {
            dao.create(entity);
        }
    }

    public WatchlistMovieEntity getMovieByApiId(String apiId) throws SQLException
    {
        return dao.queryBuilder()
                .where().eq("apiId", apiId)
                .queryForFirst();
    }

    //delete all WatchlistMovieEntity from list
    public int removeFromWatchlist(String apiId) throws SQLException {
        //load entry
        List<WatchlistMovieEntity> foundList = dao.queryForEq("apiId", apiId);

        //if not found then return 0
        if (foundList.isEmpty() == true){
            return 0;
        } else {
            WatchlistMovieEntity first = foundList.get(0);   //there can only be one because of id

            //first delete
            return dao.delete(first);
        }
    }

    public void clearWatchlist() throws SQLException {
        try
        {
            dao.deleteBuilder().delete();
        }
        catch (SQLException e)
        {
            System.out.println("Could not delete entries in watchllist: " + e.getMessage());
        }
    }


    public static WatchlistRepository getInstance()  {

        if (instance == null)
        {
            try
            {
                instance = new WatchlistRepository();
            }
            catch (DataBaseException e) {
                System.out.println("Error getting instance of WatchlistRepository: " + e.getMessage());
            }
        }
        return instance;
    }

}
