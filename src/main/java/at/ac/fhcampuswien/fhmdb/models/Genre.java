package at.ac.fhcampuswien.fhmdb.models;

public enum Genre {
    NO_FILTER("No Filter"),ACTION("Action"), ADVENTURE("Adventure"), ANIMATION("Animation"), BIOGRAPHY("Biography"),
    COMEDY("Comedy"), CRIME("Crime"), DRAMA("Drama"), DOCUMENTARY("Documentary"),
    FAMILY("Family"), FANTASY("Fantasy"), HISTORY("History"), HORROR("Horror"),
    MUSICAL("Musical"), MYSTERY("Mystery"), ROMANCE("Romance"), SCIENCE_FICTION("Science Fiction"),
    SPORT("Sport"), THRILLER("Thriller"), WAR("War"), WESTERN("Western");

    public final String label;

    Genre(String label) {
        this.label = label;
    }
}
