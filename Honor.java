import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Julian on 12/25/2016.
 */
public class Honor implements Tile {
    // For the id,
    // 1 = 紅中 (special_center.png)
    // 2 = 發財 (special_fortune.png)
    // 3 = 白板 (special_blank.png)
    private int id;
    private String category;
    private String fileName;
    private ImageView image;
    private int num_selected;
    private static final boolean startSheung = false;

    public Honor(int name, String file) {
        id = name;
        category = "honor";
        fileName = file;
        image = new ImageView(new Image("images/" + file));
        num_selected = 0;
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
    }
}
