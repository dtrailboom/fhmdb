package at.ac.fhcampuswien.fhmdb.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;

@Getter
@DatabaseTable(tableName = "WATCHLIST")
public class WatchlistMovieEntity {

    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String apiId;

    public WatchlistMovieEntity() {
    }

    public WatchlistMovieEntity(String apiId) {
        this.apiId = apiId;
    }
}

