import javafx.scene.control.Alert;

import java.util.*;

/**
 * Created by Julian on 12/23/2016.
 */
public class ScoreCalculator {
    private int round;
    private int seat;
    private int self_drawn;
    private HashSet<Tile> hand;
    private LinkedList<String> points_sources;
    private int score;
    private int num_pong_kong;
    private int num_sheung;
    private int num_flowers;
    private int num_seasons;
    private int num_eye;
    private List<Boolean> little_three_dragons;
    private int little_three_dragons_index_pong_kong;
    private int little_three_dragons_index_pair;
    private Set<String> suits_seen;
    private int num_pairs;

    public ScoreCalculator(int r, int s, int sd, HashSet<Tile> hs, LinkedList<String> ps) {
        round = r;
        seat = s;
        self_drawn = sd;
        hand = hs;
        points_sources = ps;
        score = 0;
        num_pong_kong = 0;
        num_sheung = 0;
        num_flowers = 0;
        num_seasons = 0;
        num_eye = 0;
        little_three_dragons = new ArrayList<>(Arrays.asList(false, false, false)); // [Pong/Kong, Pong/Kong, Pair]
        little_three_dragons_index_pong_kong = 0;
        little_three_dragons_index_pair = 2;
        suits_seen = new HashSet<>();
        num_pairs = 0;
    }

    public void resetScore() {
        score = 0;
        num_pong_kong = 0;
        num_sheung = 0;
        num_flowers = 0;
        num_seasons = 0;
        num_eye = 0;
        little_three_dragons = new ArrayList<>(Arrays.asList(false, false, false)); // [Pong/Kong, Pong/Kong, Pair]
        little_three_dragons_index_pong_kong = 0;
        little_three_dragons_index_pair = 2;
        suits_seen = new HashSet<>();
        num_pairs = 0;
    }

    /** The score is computed using the Traditional Hong Kong scoring system detailed here:
     * http://www.mahjongtime.com/hong-kong-mahjong-scoring.html
     */
    public int computeScore() {
        for (Tile t : hand) {
            // Check if eye is present (one and only one needed to win except with 7 pairs)
            if (t.getCount() == 2) {
                num_eye += 1;
                num_pairs += 1;
            }

            // Keep track of the tile's suit
            suits_seen.add(t.getCategory());

            // Flowers and Seasons (1 point each if matching with seat)
            if (t.getCategory().equals("flower_season") && t.getId() == 1 && seat == 1) {
                score += 1;
                num_flowers += 1;
                points_sources.addLast("東位, 一花: " + Integer.toString(1) + "\n");
            }
            if (t.getCategory().equals("flower_season") && t.getId() == 2 && seat == 2) {
                score += 1;
                num_flowers += 1;
                points_sources.addLast("南位, 二花: " + Integer.toString(1) + "\n");
            }
            if (t.getCategory().equals("flower_season") && t.getId() == 3 && seat == 3) {
                score += 1;
                num_flowers += 1;
                points_sources.addLast("西位, 三花: " + Integer.toString(1) + "\n");
            }
            if (t.getCategory().equals("flower_season") && t.getId() == 4 && seat == 4) {
                score += 1;
                num_flowers += 1;
                points_sources.addLast("北位, 四花: " + Integer.toString(1) + "\n");
            }
            if (t.getCategory().equals("flower_season") && t.getId() == 5 && seat == 1) {
                score += 1;
                num_seasons += 1;
                points_sources.addLast("東位, 春天: " + Integer.toString(1) + "\n");
            }
            if (t.getCategory().equals("flower_season") && t.getId() == 6 && seat == 2) {
                score += 1;
                num_seasons += 1;
                points_sources.addLast("南位, 夏天: " + Integer.toString(1) + "\n");
            }
            if (t.getCategory().equals("flower_season") && t.getId() == 7 && seat == 3) {
                score += 1;
                num_seasons += 1;
                points_sources.addLast("西位, 秋天: " + Integer.toString(1) + "\n");
            }
            if (t.getCategory().equals("flower_season") && t.getId() == 8 && seat == 4) {
                score += 1;
                num_seasons += 1;
                points_sources.addLast("北位, 冬天: " + Integer.toString(1) + "\n");
            }

            // Pong/Kong of 紅中, 發財, or 白板 (1 point each)
            // Also keeps track of the Little Three Dragons
            if (t.getCategory().equals("honor") && t.getId() == 1 && t.getCount() >= 3) {
                score += 1;
                num_pong_kong += 1;
                little_three_dragons.add(little_three_dragons_index_pong_kong, true);
                little_three_dragons_index_pong_kong += 1;
                points_sources.addLast("碰紅中: " + Integer.toString(1) + "\n");
            } else if (t.getCategory().equals("honor") && t.getId() == 2 && t.getCount() >= 3) {
                score += 1;
                num_pong_kong += 1;
                little_three_dragons.add(little_three_dragons_index_pong_kong, true);
                little_three_dragons_index_pong_kong += 1;
                points_sources.addLast("碰發財: " + Integer.toString(1) + "\n");
            } else if (t.getCategory().equals("honor") && t.getId() == 3 && t.getCount() >= 3) {
                score += 1;
                num_pong_kong += 1;
                little_three_dragons.add(little_three_dragons_index_pong_kong, true);
                little_three_dragons_index_pong_kong += 1;
                points_sources.addLast("碰白板: " + Integer.toString(1) + "\n");
            } else if (t.getCategory().equals("honor") && t.getCount() == 2) {
                little_three_dragons.add(little_three_dragons_index_pair, true);
            }

            // Pong/Kong of 2 of {紅中, 發財, 白板} and a pair of the other (4 points)
            if (!little_three_dragons.contains(false)) {
                score += 4;
                points_sources.addLast("小三元: " + Integer.toString(4) + "\n");
            }

            // Pong/Kong of 東, 南, 西, or 北 (1 point each if matching with round or seat)
            if (t.getCategory().equals("direction") && t.getId() == 1 && t.getCount() >= 3) {
                if (round == 1) {
                    score += 1;
                    num_pong_kong += 1;
                    points_sources.addLast("東圜, 碰東: " + Integer.toString(1) + "\n");
                }
                if (seat == 1) {
                    score += 1;
                    num_pong_kong += 1;
                    points_sources.addLast("東位, 碰東: " + Integer.toString(1) + "\n");
                }
            } else if (t.getCategory().equals("direction") && t.getId() == 2 && t.getCount() >= 3) {
                if (round == 2) {
                    score += 1;
                    num_pong_kong += 1;
                    points_sources.addLast("南圜, 碰南: " + Integer.toString(1) + "\n");
                }
                if (seat == 2) {
                    score += 1;
                    num_pong_kong += 1;
                    points_sources.addLast("南位, 碰南: " + Integer.toString(1) + "\n");
                }
            } else if (t.getCategory().equals("direction") && t.getId() == 3 && t.getCount() >= 3) {
                if (round == 3) {
                    score += 1;
                    num_pong_kong += 1;
                    points_sources.addLast("西圜, 碰西: " + Integer.toString(1) + "\n");
                }
                if (seat == 3) {
                    score += 1;
                    num_pong_kong += 1;
                    points_sources.addLast("西位, 碰西: " + Integer.toString(1) + "\n");
                }
            } else if (t.getCategory().equals("direction") && t.getId() == 4 && t.getCount() >= 3) {
                if (round == 4) {
                    score += 1;
                    num_pong_kong += 1;
                    points_sources.addLast("北圜, 碰北: " + Integer.toString(1) + "\n");
                }
                if (seat == 4) {
                    score += 1;
                    num_pong_kong += 1;
                    points_sources.addLast("北位, 碰北: " + Integer.toString(1) + "\n");
                }
            } else {
                if (t.getCount() >= 3) {
                    num_pong_kong += 1;
                }
            }

            // Keep track of how many sheung's there are
            if (t.startSheung()) {
                num_sheung += 1;
            }
        }

        if (suits_seen.contains("flower_season")) {
            suits_seen.remove("flower_season");
        }

        // All flowers (2 points)
        if (num_flowers == 4) {
            score += 2;
            points_sources.addLast("四朵花: " + Integer.toString(2) + "\n");
        }

        // All Seasons (2 points)
        if (num_seasons == 4) {
            score += 2;
            points_sources.addLast("四季節: " + Integer.toString(2) + "\n");
        }

        // No Flowers or Seasons (1 point)
        if (num_flowers == 0 && num_seasons == 0) {
            score += 1;
            points_sources.addLast("沒花: " + Integer.toString(1) + "\n");
        }

        // All Sheung's (1 point)
        if (num_sheung == 4) {
            score += 1;
            points_sources.addLast("平糊: " + Integer.toString(1) + "\n");
        }

        // All Pong's (3 points)
        if (num_pong_kong == 4) {
            score += 3;
            points_sources.addLast("對對糊: " + Integer.toString(3) + "\n");
        }

        // Make sure an eye is present in the winning hand or 7 pairs
        // Otherwise, return a score of -1 (error)
        if (num_eye != 1 && num_pairs != 7) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setHeaderText(null);
            alert.setContentText("An eye is required for the winning hand!");
            alert.showAndWait();
            return -1;
        }

        // 7 pairs (4 points)
        if (num_pairs == 7) {
            score += 4;
            points_sources.addLast("七對子/七姊妹: " + Integer.toString(4) + "\n");
        }

        // One Suit and Honors (3 points)
        if (suits_seen.size() == 2 && (suits_seen.contains("honor") || suits_seen.contains("direction"))) {
            score += 3;
            points_sources.addLast("混一色: " + Integer.toString(3) + "\n");
        }

        // One Suit only (6 points)
        if (suits_seen.size() == 1) {
            score += 6;
            points_sources.addLast("清一色: " + Integer.toString(6) + "\n");
        }

        // Self Draw the winning tile (1 point)
        if (self_drawn == 1) {
            score += self_drawn;
            points_sources.addLast("自摸: " + Integer.toString(1) + "\n");
        }

        // Make sure the score is at least 3 for the winning hand
        if (score < 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Winning Hand Error");
            alert.setHeaderText(null);
            alert.setContentText("The minimum score required for a winning hand is 3. This hand has a score of " +
                    Integer.toString(score) + " points and therefore cannot be a winning hand.");
            alert.showAndWait();
            return 0;
        }
        return score;
    }
}
