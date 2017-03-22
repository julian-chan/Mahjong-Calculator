import javafx.scene.image.ImageView;

/**
 * Created by Julian on 12/25/2016.
 */
public interface Tile {
    // Returns the id of the tile ("一索" returns 1, "二花: 蘭花" returns 2, etc.)
    int getId();

    // Returns category of the tile ("一索" returns "sok", "二花: 蘭花" returns "flower", etc.)
    String getCategory();

    // Returns the fileName of the image ("一索" returns "images/b1.png", "二花: 蘭花" returns "images/flower_21.png", etc.)
    String getFileName();

    // Returns the ImageView object of the tile to display on the GUI
    ImageView getImage();

    // Returns the number of times the tile was selected
    int getCount();

    // Increments the number of times the tile was selected
    void incrementCount();

    // Decrements the number of times the tile was selected
    void decrementCount();

    // Indicates whether or not this tile is the first in a series of 3 consecutive tiles
    boolean startSheung();

    // Sets the startSheung attribute of the tile
    void setStartSheung(boolean start);
}
