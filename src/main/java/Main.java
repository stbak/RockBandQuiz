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

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public Main() throws IOException {
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        //terminal.setForegroundColor(TextColor.ANSI.YELLOW); //obsolete
        terminal.setCursorVisible(true);


        int terminalWidth = terminal.getTerminalSize().getColumns();
        int terminalHeight = terminal.getTerminalSize().getRows();

        //some fancy presentation stuff ;)
        String gamename = "\\m/ Guess the rockband \\m/";
        String gameinfo = "Press < to end game";
        Character[] c = new Character[gamename.length()];
        int gameNameStartingPoint = startingPoints(terminalWidth, gamename.length());
        int gameInfoStartingPoint = startingPoints(terminalWidth, gameinfo.length());


        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();
        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        terminal.newTextGraphics();
        screen.startScreen();
        tg.putString(gameNameStartingPoint, 0, gamename);
        tg.putString(gameInfoStartingPoint, 23, gameinfo);
        screen.refresh();

        // Initiate background music player
        String waveFile = "bgsound_guitar2.wav";
        MusicPlayer myPlayer = new MusicPlayer(waveFile, true);
        myPlayer.run();
        // Inititate btn press sound
        String btnSound = "btnsound1.wav";
        MusicPlayer btnPlayer = new MusicPlayer(btnSound, false);
        // fail or success sounds
        String failSound = "failSound.wav";
        String successSound = "successSound.wav";



        // get the band and write out as many _ as char's in bandname, with a space in between each for better user experience
        GetTheBand band = new GetTheBand();
        String currentBand = band.guessTheBand();
        currentBand = "abba";
        int curBandLength = currentBand.length();   //for counting later on

        int xStart = startingPoints(terminalWidth, (curBandLength * 2));     //staring column for where we put the "_" placeholders, curBandLength * 2 since we place spaces in between each char in bandname
        System.out.println(terminalWidth + "/" + curBandLength + "/" + startingPoints(terminalWidth, curBandLength));
        final int orgXStart = xStart;  //for being able to later refer starting point
        final int yStart = 2;         //the starting row for "_" placeholders

        int allowedNumberOfGuesses = correctedBandNameLength(currentBand) * 2;  //removes spaces from being counted as part of band name, * 2 = twice as many guesses as bandname i long
        System.out.println(curBandLength + "/" + allowedNumberOfGuesses);       //DEBUGGING (remove)
        int guessCounter = 0;                                                   //keeping track of guesses
        int hitsInBandNameString;                                               //the number of times a character is found in band name (Kiss = 2 x s)
        int noCorrectGuesses = 0;                                               //keeping track of correct guesses
        String strCorrect = currentBand + " is the correct answer!";
        boolean winOrLoose = false;                                                //will of course be true on correct guess

        Position[] positions = new Position[curBandLength];



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
        terminal.setCursorPosition(orgXStart, yStart+1);           //cursor for user input
        terminal.flush();
        System.out.println(currentBand);                                //FOR DEBUGGING
        //ask for user input

        //========================================================================================
        int noHitXPos = orgXStart;                              //this is where we start putting incorrect char's on X-axis
        final int noHitYPos = yStart + 3;                       //this is the row we always use for incorrect guesses
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
            btnPlayer.run();
            System.out.println("keyStroke.getKeyType(): " + type
                    + " keyStroke.getCharacter(): " + c2);

            if (c2 == Character.valueOf('<')) {
                //stop play background music and btnSounds
                stopBackgroundMusic(myPlayer);
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
            System.out.println("bandname length: " + currentBand.length() + ", correctGuesses: " + noCorrectGuesses + ", guessCounter: " + guessCounter);
            //Check if we have guessed whole bandname correct

            if (curBandLength == noCorrectGuesses) {
                //stop play background music
                winOrLoose = true;
                stopBackgroundMusic(myPlayer);
                Thread.sleep(2000); //give background music time to stop before playing success sound
                MusicPlayer success = new MusicPlayer(successSound, false);
                success.run();
                tg.putString(startingPoints(terminalWidth, strCorrect.length()), noHitYPos, currentBand + " is the correct answer!");
                Thread.sleep(100);
                terminal.flush();
                guessCounter = allowedNumberOfGuesses+1;
            }  else if (noCorrectGuesses > curBandLength) {
                guessCounter = allowedNumberOfGuesses;
                winOrLoose = false;
            }

            //-end if number of guesses reached maximum
            guessCounter++;
            if (guessCounter > allowedNumberOfGuesses) {
                stopBackgroundMusic(myPlayer);

                if (!winOrLoose) {
                    Thread.sleep(2000); //give background music time to stop before playing failure sound
                    MusicPlayer loose = new MusicPlayer(failSound, false);
                    loose.run();
                }
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

    static String winnerPrintOut(String bandName, MusicPlayer successSound) {
        MusicPlayer winner = new MusicPlayer(successSound.waveFile, false);
        return "Yes, you did it! Correct answer is " + bandName + ", well done!!";
    }

    static String looserPrintOut(String bandName, MusicPlayer failSound) {
        MusicPlayer looser = new MusicPlayer(failSound.waveFile, false);
        return "Ohhh no, you lost! Correct answer was " + bandName + "...";
    }

    static int startingPoints(int screenWidth, int strLength) {
        int middle = screenWidth/2;
        int strL = strLength/2;

        return middle - strL;
    }

    static void stopBackgroundMusic(MusicPlayer myPlayer) {
        try {
            Thread.sleep(300);
            myPlayer.stop();
        } catch (InterruptedException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

}
