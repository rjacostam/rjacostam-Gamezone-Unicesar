package com.gamezone.model;

/**
 * Video game product.
 */
public class VideoGame extends Product {
    private String platform;
    private String genre;
    private String ageRating;

    /**
     * Creates a video game.
     * @param id product identifier
     * @param title product title
     * @param price unit price
     * @param stock available stock
     * @param platform platform (PS5, Xbox, Switch, PC)
     * @param genre genre
     * @param ageRating age rating (E, T, M)
     */
    public VideoGame(String id, String title, double price, int stock,
                     String platform, String genre, String ageRating) {
        super(id, title, price, stock);
        this.platform = platform;
        this.genre = genre;
        this.ageRating = ageRating;
    }

    /** @return platform */
    public String getPlatform() { return platform; }
    /** @param platform platform */
    public void setPlatform(String platform) { this.platform = platform; }
    /** @return genre */
    public String getGenre() { return genre; }
    /** @param genre genre */
    public void setGenre(String genre) { this.genre = genre; }
    /** @return age rating */
    public String getAgeRating() { return ageRating; }
    /** @param ageRating age rating */
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }

    @Override
    public String getDescription() {
        return "VideoGame [" + getId() + "] " + getTitle()
                + " | Platform: " + platform
                + " | Genre: " + genre
                + " | Rating: " + ageRating
                + " | Price: " + getPrice()
                + " | Stock: " + getStock();
    }
}
