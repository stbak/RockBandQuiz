import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public Main() throws IOException {
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();

        terminal.setCursorVisible(true);

        int terminalWidth = terminal.getTerminalSize().getColumns();
        int terminalHeight = terminal.getTerminalSize().getRows();

        //some fancy presentation stuff
        String gamename = "\\m/ Guess the rockband \\m/";
        String gameinfo = "Press < to end game";
        Character[] c = new Character[gamename.length()];
        int gameNameStartingPoint = (terminalWidth/2) - (gamename.length()/2);
        int infoStartingPoint = 2;

        int infoPrintLength = 0;


        for (int i = 0; i < c.length; i++) {
            terminal.setCursorPosition(gameNameStartingPoint, 1); //gameName on first row
            //terminal.setForegroundColor(TextColor.ANSI.RED);
            terminal.putCharacter(gamename.charAt(i));
            gameNameStartingPoint++;

        }
        terminal.flush();
        for (int i = 0; i < gameinfo.length(); i++) {
            terminal.setCursorPosition(infoStartingPoint, 24); //end game info
            terminal.putCharacter(gameinfo.charAt(i));
            infoStartingPoint++;

        }
        terminal.flush();

        // Example of playing background music in new thread, just use Music class and these 2 lines:
        Thread thread = new Thread(new Music());
        thread.start();


        //get the band and write out as many _ as char's in bandname
        GetTheBand band = new GetTheBand();
        String currentBand = band.guessTheBand();
        int xStart = 2;
        int orgXStart = 2;
        int yStart = 4;
        int curBandLength = currentBand.length();
        int allowedNumberOfGuesses = curBandLength * 2;
        int guessCounter = 0;
        int hitsInBandNameString;

        Position[] positions = new Position[curBandLength];
        //Guess[] guesses = new Guess[allowedNumberOfGuesses];


        //print out the empty char slots to guess on
        for (int i = 0; i < currentBand.length(); i++) {
            terminal.setCursorPosition(xStart, yStart);
            positions[i] = new Position(xStart, yStart); //to keep track of where to place char's
            if (currentBand.charAt(i) != ' ') {
                terminal.putCharacter('_');
            } else {
                terminal.putCharacter(currentBand.charAt(i));
            }
            xStart=xStart+2;
        }
        terminal.setCursorPosition(2, yStart+2); //cursor for user input
        terminal.flush();
        System.out.println(currentBand); //FOR DEBUGGING
        //ask for user input


        boolean continueReadingInput = true;
        while (continueReadingInput) {
            KeyStroke keyStroke = null;
            do {
                Thread.sleep(5); // might throw InterruptedException
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);
            KeyType type = keyStroke.getKeyType();
            Character c2 = keyStroke.getCharacter();
            System.out.println("keyStroke.getKeyType(): " + type
                    + " keyStroke.getCharacter(): " + c2);

            if (c2 == Character.valueOf('<')) {
                continueReadingInput = false;
                terminal.close();
                System.out.println("quit");
            }

            Guess guess = new Guess(c2, currentBand);
            guess.theGuess();

            hitsInBandNameString = guess.getHits().size(); //same char can be on several places in bandname
            System.out.println("hits size: " + hitsInBandNameString);

            int noHitXPos = 2; //this is where we start putting incorrect char's on X-axis
            final int noHitYPos = yStart + 4; //this is the row we always use for incorrect guesses

            //char[] to keep all faulty guesses, must be as big as allowed number of guesses
            ArrayList<Character> faultyGuesses = new ArrayList<>();

            //PRINT OUT GUESSES
            if (hitsInBandNameString == 0) {
                faultyGuesses.add(c2);
                for (Character faulty: faultyGuesses
                     ) {
                    terminal.setCursorPosition(noHitXPos, noHitYPos);
                    terminal.putCharacter(faulty);
                    noHitXPos++;
                }
                terminal.setCursorPosition(2, yStart+2); //cursor for user input
                terminal.flush();

            }

            for (int i = 0; i < hitsInBandNameString; i++) { //place guess on correct location(s)
                terminal.setCursorPosition(orgXStart + (guess.getHits().get(i) * 2),yStart);
                terminal.putCharacter(c2);
                terminal.setCursorPosition(2, yStart+2); //cursor for user input
                terminal.flush();

                System.out.println("Guess: " + c2 + ", position: " + guess.getHits().get(i));
            }


            guessCounter++;
            if (guessCounter > allowedNumberOfGuesses) {
                continueReadingInput = false;
                System.out.println("Game over...");
                terminal.close();
            }

        }

    }




}
