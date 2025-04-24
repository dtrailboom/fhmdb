package at.ac.fhcampuswien.fhmdb.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist")
public class WatchlistMovieEntity {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false)
    private String apiId;  // Verweist auf den API-ID des Movies

    public WatchlistMovieEntity() {}

    public WatchlistMovieEntity(String apiId) {
        this.apiId = apiId;
    }

    // Getter und Setter
}

