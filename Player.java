/**
 * Created by Julian on 12/28/2016.
 */
public class Player {
    private String name;
    private String seat;
    private int score;

    public Player(String n, String s) {
        name = n;
        seat = s;
        score = 0;
    }

    public void setName(String n) {
        name = n;
    }

    public void setSeat(String s) {
        seat = s;
    }

    public String getName() {
        return name;
    }

    public String getSeat() {
        return seat;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int n) {
        score += n;
    }

    public void subtractScore(int n) {
        score -= n;
    }

    public void advanceSeat() {
        if (seat.equals("東位")) {
            seat = "南位";
        } else if (seat.equals("南位")) {
            seat = "西位";
        } else if (seat.equals("西位")) {
            seat = "北位";
        } else if (seat.equals("北位")) {
            seat = "東位";
        }
    }
}
