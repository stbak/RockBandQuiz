import java.util.ArrayList;
import java.util.List;

public class Guess {
    private char guess;
    private Position position;
    private String band;
    private List<Integer> hits = new ArrayList<>();         //here we keep the good guesses per round, e.g. guesses 's' and there are three of them in bandname
    private List<Character> faulties = new ArrayList<>();   //here we keep the bad guesses

    private int[] correctGuessesLength;

    public Guess(char guess, String band) {
        this.guess = guess;
        this.band = band.toLowerCase();
    }

    public List<Integer> getHits() { //what's this for????
        return hits;
    }

    public List<Character> getFaulties() {
        return faulties;
    }



    public void theGuess() {
        int cbLength = band.length();
        boolean hit = false;

        for (int i = 0; i < cbLength; i++) {
            if (guess == band.charAt(i)) { //you found one
                hits.add(i);
                hit = true;
            }
        }

        if (!hit) {
            faulties.add(guess);
        }

    }
}
