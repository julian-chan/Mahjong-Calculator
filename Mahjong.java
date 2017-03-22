import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

/**
 * Created by Julian on 12/23/2016.
 */
public class Mahjong extends Application {
    // Set the bounds for the application window
    private static int windowHeight = 640;
    private static int windowWidth = 1200;

    // Number of tiles in a valid hand
    private int max_tiles = 14;
    // Keep track of number of tiles that were selected
    private int num_tiles = 0;

    // Keep track of the total number of tiles selected
    private int total_tiles = 0;

    // Keep track of current positions in the selected hand.
    // Start the y position below the buffer of the bottom-most tile.
    private int selected_x = 10;
    private int selected_y = 470;

    // Keep track of the current round for computing the score
    // East = 1, South = 2, West = 3, North = 4
    private int round = 0;

    // Keep track of winning player's seat position for computing the score
    private int seat = 0;

    // Keep track of whether or not the winning tile was self-drawn
    private int self_drawn;

    // For scoring output purposes
    private Text output_label;
    private Text output_message;

    /** Data Structures */
    // Create a HashMap of file names to tile name
    private HashMap<String, Tile> tileNames = new HashMap<>();

    // Create a Stack to remember previous tiles added
    Stack<String> prevTiles = new Stack<>();

    // Create a LinkedList for the tiles in the selected hand
    LinkedList<String> selectedHand = new LinkedList<>();

    // Create a LinkedList to store the sources of points awarded in the hand
    LinkedList<String> points_sources = new LinkedList<>();
    /** -------------- */

    Group root = new Group();

    private void populateWithImageNames() {
        tileNames.put("b1.png", new Tong(1, "b1.png"));
        tileNames.put("b2.png", new Tong(2, "b2.png"));
        tileNames.put("b3.png", new Tong(3, "b3.png"));
        tileNames.put("b4.png", new Tong(4, "b4.png"));
        tileNames.put("b5.png", new Tong(5, "b5.png"));
        tileNames.put("b6.png", new Tong(6, "b6.png"));
        tileNames.put("b7.png", new Tong(7, "b7.png"));
        tileNames.put("b8.png", new Tong(8, "b8.png"));
        tileNames.put("b9.png", new Tong(9, "b9.png"));
        tileNames.put("c1.png", new Man(1, "c1.png"));
        tileNames.put("c2.png", new Man(2, "c2.png"));
        tileNames.put("c3.png", new Man(3, "c3.png"));
        tileNames.put("c4.png", new Man(4, "c4.png"));
        tileNames.put("c5.png", new Man(5, "c5.png"));
        tileNames.put("c6.png", new Man(6, "c6.png"));
        tileNames.put("c7.png", new Man(7, "c7.png"));
        tileNames.put("c8.png", new Man(8, "c8.png"));
        tileNames.put("c9.png", new Man(9, "c9.png"));
        tileNames.put("d1.png", new Sok(1, "d1.png"));
        tileNames.put("d2.png", new Sok(2, "d2.png"));
        tileNames.put("d3.png", new Sok(3, "d3.png"));
        tileNames.put("d4.png", new Sok(4, "d4.png"));
        tileNames.put("d5.png", new Sok(5, "d5.png"));
        tileNames.put("d6.png", new Sok(6, "d6.png"));
        tileNames.put("d7.png", new Sok(7, "d7.png"));
        tileNames.put("d8.png", new Sok(8, "d8.png"));
        tileNames.put("d9.png", new Sok(9, "d9.png"));
        tileNames.put("dir1.png", new Direction(1, "dir1.png"));
        tileNames.put("dir2.png", new Direction(2, "dir2.png"));
        tileNames.put("dir3.png", new Direction(3, "dir3.png"));
        tileNames.put("dir4.png", new Direction(4, "dir4.png"));
        tileNames.put("flower_season_10.png", new Flower_Season(1, "flower_season_10.png"));
        tileNames.put("flower_season_20.png", new Flower_Season(2, "flower_season_20.png"));
        tileNames.put("flower_season_30.png", new Flower_Season(3, "flower_season_30.png"));
        tileNames.put("flower_season_40.png", new Flower_Season(4, "flower_season_40.png"));
        tileNames.put("flower_season_11.png", new Flower_Season(5, "flower_season_11.png"));
        tileNames.put("flower_season_21.png", new Flower_Season(6, "flower_season_21.png"));
        tileNames.put("flower_season_31.png", new Flower_Season(7, "flower_season_31.png"));
        tileNames.put("flower_season_41.png", new Flower_Season(8, "flower_season_41.png"));
        tileNames.put("special1.png", new Honor(1, "special1.png"));
        tileNames.put("special2.png", new Honor(2, "special2.png"));
        tileNames.put("special3.png", new Honor(3, "special3.png"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.LIGHTBLUE);
        stage.setTitle("Mahjong Assistant");

        // Template for the output score and message
        output_label = new Text(580, 300, "");
        output_label.setFont(new Font("Arial Black", 16));
        output_message = new Text(580, 360, "");
        root.getChildren().add(output_label);
        root.getChildren().add(output_message);

        // Gather all image file names into an array. Make sure all image files present.
        File images_folder = new File("src/images");
        File[] images = images_folder.listFiles();
        if (images == null) {
            System.out.println("Could not locate image files. Aborting...");
            System.exit(0);
        } else if (images.length < 42) {
            System.out.println(images.length + " images detected but 42 files needed. Aborting...");
            System.exit(0);
        } else if (images.length > 42) {
            System.out.println(images.length + " images detected but only 42 files needed. Aborting...");
            System.exit(0);
        }

        // Populate the HashMap with Tile objects and the name of the tile's image file
        populateWithImageNames();

        // Obtain the image from the list of image file names. Add buttons to the scene and set positions appropriately.
        // Each image is 44 pixels wide and 53 pixels high. Add a 16 pixel buffer horizontally between each image and
        // 17 pixel buffer vertically between each image. (Each button should be 60px by 70px)
        int cur_x = 10;
        int cur_y = 25;
        Object[] keys = tileNames.keySet().toArray();
        Arrays.sort(keys);
        for (Object s : keys) {
            Tile tile = tileNames.get(s);
            ImageView image = tile.getImage();
            if (tile.getId() == 1 && (cur_x != 10 || cur_y != 25)) {
                cur_x = 10;
                cur_y += 70;
            }
            Button button = new Button("", image);
            button.setId(tile.getFileName());
            button.setLayoutX(cur_x);
            button.setLayoutY(cur_y);
            cur_x += 60;
            root.getChildren().add(button);
            // When selecting tiles, also add to the selected hand
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Remove old results from the GUI
                    root.getChildren().remove(output_label);
                    root.getChildren().remove(output_message);
                    // Check if the number of tiles in a player's hand is valid
                    if (num_tiles >= max_tiles && !button.getId().startsWith("flower_season")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Selection Error");
                        alert.setHeaderText(null);
                        alert.setContentText("You can only select " + Integer.toString(max_tiles) + " tiles!");
                        alert.showAndWait();
                        return;
                    }
                    if (button.getId().startsWith("flower_season") && tileNames.get(button.getId()).getCount() >= 1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Selection Error");
                        alert.setHeaderText(null);
                        alert.setContentText("You can only select at most 1 of this flower!");
                        alert.showAndWait();
                        return;
                    }
                    if (tileNames.get(button.getId()).getCount() >= 4 && !button.getId().startsWith("flower_season")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Selection Error");
                        alert.setHeaderText(null);
                        alert.setContentText("You can only select at most 4 of this tile!");
                        alert.showAndWait();
                        return;
                    }
                    // If not a flower/season tile, increment tile count by 1
                    if (!button.getId().startsWith("flower_season")) {
                        num_tiles += 1;
                    }
                    // Add to the stack for undo operation
                    prevTiles.add(button.getId());
                    // Add to selected hand
                    ImageView iv_selected = new ImageView(new Image("images/" + button.getId()));
                    if (selected_x >= 550) {
                        selected_x = 10;
                        selected_y += 70;
                    }
                    iv_selected.setX(selected_x);
                    iv_selected.setY(selected_y);
                    selected_x += 60;
                    total_tiles += 1;
                    // Add to the LinkedList selectedHand to compute the score later
                    selectedHand.addLast(button.getId());
                    // Increment the number of times the tile was selected
                    tileNames.get(button.getId()).incrementCount();
                    // If there are 4 of a tile, the maximum hand size increases by 1
                    if (tileNames.get(button.getId()).getCount() == 4) {
                        max_tiles += 1;
                    }
                    root.getChildren().add(iv_selected);
                }
            });
        }

        // Add an undo button for mistakenly adding tiles
        Button undo_button = new Button("Undo Adding Last Tile");
        undo_button.setLayoutX(350);
        undo_button.setLayoutY(605);
        // When undo-ing, also remove from the selected hand
        undo_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                // Make sure stack is not empty
                if (prevTiles.size() > 0) {
                    // If not a flower/season tile, decrement tile count by 1
                    if (!tileNames.get(prevTiles.peek()).getCategory().equals("flower_season")) {
                        num_tiles -= 1;
                    }
                    // Remove from the stack
                    String removed = prevTiles.pop();
                    // Remove from selected hand
                    if (selected_x <= 10) {
                        selected_x = 550;
                        selected_y -= 70;
                    }
                    if (selected_y < 450) {
                        selected_y = 450;
                    }
                    selected_x -= 60;
                    total_tiles -= 1;
                    // Remove from the LinkedList selectedHand
                    selectedHand.removeLast();
                    // If there are 4 of a tile and the undo button is pressed, the maximum hand size decreases by 1
                    if (tileNames.get(removed).getCount() == 4) {
                        max_tiles -= 1;
                    }
                    // Decrement the number of times the tile was selected
                    tileNames.get(removed).decrementCount();
                    // 63 nodes rendered upon initialization, total_tiles is the number of tiles added since then
                    // 42 tiles
                    // 4 instructions
                    // 1 Selected Hand label
                    // 2 Buttons
                    // 10 Toggle Buttons
                    // 3 Dividers
                    // 1 Credits
                    root.getChildren().remove(63 + total_tiles);
                }
            }
        });
        root.getChildren().add(undo_button);

        /** --------------------------------------------------------------------------------------------------------- */
        // Add instruction for user to select round
        Text instruction2 = new Text(580, 15, "Please select the current round.");
        instruction2.setFont(new Font("Arial Black", 14));
        root.getChildren().add(instruction2);

        // Add ToggleButtons for user to select round
        ToggleGroup rounds = new ToggleGroup();

        ToggleButton east_round = new ToggleButton("東圜");
        east_round.setToggleGroup(rounds);
        east_round.setId("east round");
        east_round.setLayoutX(580);
        east_round.setLayoutY(25);
        east_round.setFont(new Font(24));
        east_round.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                round = 1;
            }
        });
        root.getChildren().add(east_round);

        ToggleButton south_round = new ToggleButton("南圜");
        south_round.setToggleGroup(rounds);
        south_round.setId("south round");
        south_round.setLayoutX(660);
        south_round.setLayoutY(25);
        south_round.setFont(new Font(24));
        south_round.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                round = 2;
            }
        });
        root.getChildren().add(south_round);

        ToggleButton west_round = new ToggleButton("西圜");
        west_round.setToggleGroup(rounds);
        west_round.setId("west round");
        west_round.setLayoutX(740);
        west_round.setLayoutY(25);
        west_round.setFont(new Font(24));
        west_round.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                round = 3;
            }
        });
        root.getChildren().add(west_round);

        ToggleButton north_round = new ToggleButton("北圜");
        north_round.setToggleGroup(rounds);
        north_round.setId("north round");
        north_round.setLayoutX(820);
        north_round.setLayoutY(25);
        north_round.setFont(new Font(24));
        north_round.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                round = 4;
            }
        });
        root.getChildren().add(north_round);
        /** --------------------------------------------------------------------------------------------------------- */

        /** --------------------------------------------------------------------------------------------------------- */
        // Add instruction for user to select seat position
        Text instruction3 = new Text(580, 100, "Please select the winner's seat position.");
        instruction3.setFont(new Font("Arial Black", 14));
        root.getChildren().add(instruction3);

        // Add ToggleButtons for user to select seat position
        ToggleGroup seats = new ToggleGroup();

        ToggleButton east_seat = new ToggleButton("東位");
        east_seat.setToggleGroup(seats);
        east_seat.setId("east seat");
        east_seat.setLayoutX(580);
        east_seat.setLayoutY(110);
        east_seat.setFont(new Font(24));
        east_seat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                seat = 1;
            }
        });
        root.getChildren().add(east_seat);

        ToggleButton south_seat = new ToggleButton("南位");
        south_seat.setToggleGroup(seats);
        south_seat.setId("south seat");
        south_seat.setLayoutX(660);
        south_seat.setLayoutY(110);
        south_seat.setFont(new Font(24));
        south_seat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                seat = 2;
            }
        });
        root.getChildren().add(south_seat);

        ToggleButton west_seat = new ToggleButton("西位");
        west_seat.setToggleGroup(seats);
        west_seat.setId("west seat");
        west_seat.setLayoutX(740);
        west_seat.setLayoutY(110);
        west_seat.setFont(new Font(24));
        west_seat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                seat = 3;
            }
        });
        root.getChildren().add(west_seat);

        ToggleButton north_seat = new ToggleButton("北位");
        north_seat.setToggleGroup(seats);
        north_seat.setId("north seat");
        north_seat.setLayoutX(820);
        north_seat.setLayoutY(110);
        north_seat.setFont(new Font(24));
        north_seat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                seat = 4;
            }
        });
        root.getChildren().add(north_seat);
        /** --------------------------------------------------------------------------------------------------------- */

        /** --------------------------------------------------------------------------------------------------------- */
        // Add instruction for user to select whether or not the winning tile was self-drawn
        Text instruction4 = new Text(580, 185, "Was the winning tile self drawn?");
        instruction4.setFont(new Font("Arial Black", 14));
        root.getChildren().add(instruction4);

        // Add ToggleButtons for user to select round
        ToggleGroup selfDrawn = new ToggleGroup();

        ToggleButton yes = new ToggleButton("Yes");
        yes.setToggleGroup(selfDrawn);
        yes.setId("yes");
        yes.setLayoutX(580);
        yes.setLayoutY(195);
        yes.setFont(new Font(24));
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                self_drawn = 1;
            }
        });
        root.getChildren().add(yes);

        ToggleButton no = new ToggleButton("No");
        no.setToggleGroup(selfDrawn);
        no.setId("no");
        no.setLayoutX(648);
        no.setLayoutY(195);
        no.setFont(new Font(24));
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                self_drawn = 0;
            }
        });
        root.getChildren().add(no);
        /** --------------------------------------------------------------------------------------------------------- */

        /** --------------------------------------------------------------------------------------------------------- */
        // Add Button to compute score
        Button compute_score = new Button("Compute Score");
        compute_score.setLayoutX(230);
        compute_score.setLayoutY(605);
        compute_score.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove old results from the GUI
                root.getChildren().remove(output_label);
                root.getChildren().remove(output_message);
                if (round == 0 && seat == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Selection Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select the current round and the seat of the winning player.");
                    alert.showAndWait();
                } else if (round == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Selection Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select the current round.");
                    alert.showAndWait();
                } else if (seat == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Selection Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select the seat of the winning player.");
                    alert.showAndWait();
                } else {
                    HashSet<Tile> unique_tiles = new HashSet<>();
                    for (String s : selectedHand) {
                        unique_tiles.add(tileNames.get(s));
                    }
                    for (Tile t1 : unique_tiles) {
                        if ((t1.getCategory().equals("man") || t1.getCategory().equals("sok") || t1.getCategory().equals("tong")) && t1.getId() <= 7) {
                            for (Tile t2 : unique_tiles) {
                                if (t2.getCategory().equals(t1.getCategory()) && (t2.getId() == t1.getId() + 1)) {
                                    for (Tile t3 : unique_tiles) {
                                        if (t3.getCategory().equals(t2.getCategory()) && (t3.getId() == t2.getId() + 1)) {
                                            t1.setStartSheung(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ScoreCalculator calculator = new ScoreCalculator(round, seat, self_drawn, unique_tiles, points_sources);
                    int score = calculator.computeScore();
                    if (score >= 3) {
                        String output_string = "";
                        for (String s : points_sources) {
                            output_string += s;
                        }
                        output_label = new Text(580, 300, "Score: " + Integer.toString(score));
                        output_label.setFont(new Font("Arial Black", 16));
                        output_message = new Text(580, 360, "Score Breakdown: \n" + output_string);
                        output_message.setFont(new Font("Arial Black", 16));
                        root.getChildren().add(output_label);
                        root.getChildren().add(output_message);
                        calculator.resetScore();
                        points_sources.clear();
                        round = 0;
                        seat = 0;
                    }
                }
            }
        });
        root.getChildren().add(compute_score);

        // Add instruction for user to select tiles
        Text instruction1 = new Text(10, 15, "Please select tiles for the winning hand from the choices below.");
        instruction1.setFont(new Font("Arial Black", 14));
        root.getChildren().add(instruction1);

        // Display the hand currently picked
        Text display = new Text(10, 460, "Selected Hand");
        display.setFont(new Font("Arial Black", 14));
        root.getChildren().add(display);

        // Add rectangle to separate sections
        Rectangle splitter1 = new Rectangle(10, 640, Color.BLACK);
        splitter1.setX(560);
        splitter1.setY(0);
        root.getChildren().add(splitter1);

        // Add rectangle to separate sections
        Rectangle splitter2 = new Rectangle(350, 10, Color.BLACK);
        splitter2.setX(560);
        splitter2.setY(250);
        root.getChildren().add(splitter2);

        // Add rectangle to separate sections
        Rectangle splitter3 = new Rectangle(10, 640, Color.BLACK);
        splitter3.setX(910);
        splitter3.setY(0);
        root.getChildren().add(splitter3);

        // Add Credits
        Text credit = new Text(10, 630, "Created By Julian Chan");
        credit.setFont(new Font("Courier New", 14));
        root.getChildren().add(credit);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
