import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Julian on 12/25/2016.
 */
public class Flower_Season implements Tile {
    // For the id,
    // 1 = 一花: 梅 (flower_season_10.png)
    // 2 = 二花: 蘭花 (flower_season_20.png)
    // 3 = 三花: 菊花 (flower_season_30.png)
    // 4 = 四花: 竹 (flower_season_40.png)
    // 5 = 春天 (flower_season_11.png)
    // 6 = 夏天 (flower_season_21.png)
    // 7 = 秋天 (flower_season_31.png)
    // 8 = 冬天 (flower_season_41.png)
    private int id;
    private String category;
    private String fileName;
    private ImageView image;
    private int num_selected;
    private static final boolean startSheung = false;

    public Flower_Season(int name, String file) {
        id = name;
        category = "flower_season";
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
