import java.util.ArrayList;
import java.util.List;

public class Guess {
    private char guess;
    private Position position;
    private String band;
    private List<Integer> hits = new ArrayList<>();

    public Guess(char guess, String band) {
        this.guess = guess;
        this.band = band.toLowerCase();
    }

    public List<Integer> getHits() {
        return hits;
    }

    public void theGuess() {
        int cbLength = band.length();
        int counter = 0;

        for (int i = 0; i < cbLength; i++) {
            if (guess == band.charAt(i)) { //you found one
                hits.add(i);
            }
        }

    }
}
