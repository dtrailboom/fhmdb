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
    private DatabaseManager databaseManager;
    private static  WatchlistRepository instance= null;


    public WatchlistRepository() throws DataBaseException, SQLException {
        try {
            databaseManager = new DatabaseManager();

        } catch (DataBaseException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    //get all watchlist movies from db
    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return databaseManager.getWatchlistMovieDao().queryForAll();
    }

    //insert or update
    public void addToWatchlist(WatchlistMovieEntity entityToAdd) throws SQLException {
        databaseManager.getWatchlistMovieDao().createOrUpdate(entityToAdd);
    }

    //delete all WatchlistMovieEntity from list
    public int removefromWatchlist(String apiId) throws SQLException {
        //load entry
        List<WatchlistMovieEntity> foundList = databaseManager.getWatchlistMovieDao().queryForEq("apiId", apiId);

        //if not found then return 0
        if (foundList.isEmpty() == true){
            return 0;
        }
        else
        {
            WatchlistMovieEntity first = foundList.get(0);     //there can only be one because of id

            //first delete
            return databaseManager.getWatchlistMovieDao().delete(first);
        }
    }

    public void clearWatchlist() throws SQLException {
        try
        {
            databaseManager.getWatchlistMovieDao().deleteBuilder().delete();
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
            catch (SQLException e) {
                System.out.println("SQL-Error getting instance of WatchlistRepository: " + e.getMessage());
            } catch (DataBaseException e) {
                System.out.println("Database-Error getting instance of WatchlistRepository: " + e.getMessage());
            }
        }
        return instance;
    }

}
