import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
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
        terminal.setForegroundColor(TextColor.ANSI.YELLOW);
        terminal.setCursorVisible(true);

        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();

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

        int xStart = 2;     //staring column for where we put the "_" placeholders
        int orgXStart = 2;  //for being able to later refer starting point
        int yStart = 4;     //the starting row for "_" placeholders
        int curBandLength = currentBand.length();   //for counting later on
        int allowedNumberOfGuesses = correctedBandNameLength(currentBand) * 2; //removes spaces from being counted as part of band name
        System.out.println(curBandLength + "/" + allowedNumberOfGuesses); //DEBUGGING (remove)
        int guessCounter = 0;       //keeping track of guesses
        int hitsInBandNameString;   //the number of times a character is found in band name (Kiss = 2 x s)
        int noCorrectGuesses = 0;   //keeping track of correct guesses

        Position[] positions = new Position[curBandLength];
        //Guess[] guesses = new Guess[allowedNumberOfGuesses];


        //print out the empty char slots to guess on
        for (int i = 0; i < currentBand.length(); i++) {
            terminal.setCursorPosition(xStart, yStart);
            positions[i] = new Position(xStart, yStart); //to keep track of where to place char's
            if (currentBand.charAt(i) != ' ') {                 // if not a space, then
                terminal.putCharacter('_');                 // print "_" placeholder
            } else {
                terminal.putCharacter(currentBand.charAt(i));  // else print out the space
                noCorrectGuesses++;                             // and increase correct guesses since player won't guess on spaces
            }
            xStart=xStart+2;
        }
        terminal.setCursorPosition(2, yStart+2);           //cursor for user input
        terminal.flush();
        System.out.println(currentBand);                        //FOR DEBUGGING
        //ask for user input

        //========================================================================================
        int noHitXPos = 2;                                      //this is where we start putting incorrect char's on X-axis
        final int noHitYPos = yStart + 4;                       //this is the row we always use for incorrect guesses
        //========================================================================================

        boolean continueReadingInput = true;
        while (continueReadingInput) {
            KeyStroke keyStroke = null;
            do {
                Thread.sleep(5); // might throw InterruptedException
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);
            KeyType type = keyStroke.getKeyType();
            Character c2 = keyStroke.getCharacter();  //the char we entered as guess (if guessing, not quitting etc.)
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




            //PRINT OUT GUESSES
            
            //-faulty guesses printout
            for (int i = 0; i < guess.getFaulties().size(); i++) {
                terminal.setCursorPosition(noHitXPos, noHitYPos);
                terminal.putCharacter(guess.getFaulties().get(i));
                noHitXPos++;
            }
            terminal.flush();
            terminal.setCursorPosition(2, yStart+2); //cursor for user input
            terminal.flush();

            //-correct guesses printout
            for (int i = 0; i < hitsInBandNameString; i++) { //place guess on correct location(s)
                terminal.setCursorPosition(orgXStart + (guess.getHits().get(i) * 2),yStart);
                terminal.putCharacter(c2);
                terminal.setCursorPosition(2, yStart+2); //cursor for user input
                terminal.flush();
                noCorrectGuesses++;
                //System.out.println("Guess: " + c2 + ", position: " + guess.getHits().get(i));
            }
            System.out.println("bandname length: " + currentBand.length() + ", correctGuesses: " + noCorrectGuesses);
            //Check if we have guessed whole bandname correct

            if (curBandLength == noCorrectGuesses) {
                tg.putString(2, noHitYPos, "Correct answer!");
                Thread.sleep(100);
                terminal.flush();
                guessCounter = allowedNumberOfGuesses+1;
            }  else if (noCorrectGuesses > curBandLength) {
                guessCounter = allowedNumberOfGuesses;
            }

            //-end if number of guesses reached maximum
            guessCounter++;
            if (guessCounter > allowedNumberOfGuesses) {
                continueReadingInput = false;
                terminal.newTextGraphics();
                screen.startScreen();
                tg.setForegroundColor(TextColor.ANSI.RED);
                tg.drawRectangle(new TerminalPosition(20,9), new TerminalSize(14,3), '*');
                tg.putString(22, 10, "Game Over!");
                tg.setForegroundColor(TextColor.ANSI.WHITE);
                tg.putString( 14, 20, "<ENTER>=New Game   <ESC>=quit Game");
                screen.refresh();
                KeyStroke key = null;
                while ((key = screen.readInput()).getKeyType() != KeyType.Escape)
                    screen.stopScreen();
                terminal.close();

            }

        }


    }

    static int correctedBandNameLength(String bandName) { //do not count space's

        int len = bandName.length();
        for (int i = 0; i < bandName.length(); i++) {
            if (bandName.charAt(i) == ' ') {
                len--;
            }
        }
        return len;
    }

}
