import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Julian on 12/25/2016.
 */
public class Tong implements Tile {
    private int id;
    private String category;
    private String fileName;
    private ImageView image;
    private int num_selected;
    private boolean startSheung;

    public Tong(int name, String file) {
        id = name;
        category = "tong";
        fileName = file;
        image = new ImageView(new Image("images/" + file));
        num_selected = 0;
        startSheung = false;
    }
    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getFileName() {
        return fileName;
    }

    public ImageView getImage() {
        return image;
    }

    public int getCount() {
        return num_selected;
    }

    public void incrementCount() {
        num_selected += 1;
    }

    public void decrementCount() {
        num_selected -= 1;
    }

    public boolean startSheung() {
        return startSheung;
    }

    public void setStartSheung(boolean start) {
        startSheung = start;
    }
}
