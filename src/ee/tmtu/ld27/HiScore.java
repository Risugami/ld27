package ee.tmtu.ld27;

public class HiScore implements Comparable<HiScore> {

    public int round;
    public String name;
    public int score;

    public HiScore(int round, String name, int score) {
        this.round = round;
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(HiScore o) {
        return o.score - this.score;
    }

}
