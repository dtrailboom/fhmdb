package at.ac.fhcampuswien.fhmdb.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;

@DatabaseTable(tableName = "WATCHLIST")
public class WatchlistMovieEntity {

    @DatabaseField(generatedId = true)
    public long id;

    @Getter
    @DatabaseField(canBeNull = false)
    private String apiId;  // Verweist auf den API-ID des Movies

    public WatchlistMovieEntity() {}

    public WatchlistMovieEntity(String apiId) {
        this.apiId = apiId;
    }

}

