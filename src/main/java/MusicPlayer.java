import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
public class MusicPlayer implements Runnable {
    Thread myThread;
    String waveFile;
    boolean isPlaying = false;
    boolean loopPlay = false;
    public MusicPlayer(String fileName, boolean loopPlay) {
        waveFile = fileName;
        this.loopPlay = loopPlay;
        myThread = new Thread(this, "MusicPlayer");
        System.out.println("New thread: " + myThread.getName());
        myThread.start(); // Starting the thread
    }
    @Override
    public void run() {
        isPlaying=true;
        while(isPlaying) {
            playSound(waveFile);
            if (loopPlay == false)
                break;
        }
    }
    public void stop() {
        isPlaying = false;
    }
    private static void playSound(String fileName) {
        File soundFile = new File(fileName);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        AudioFormat audioFormat = audioInputStream.getFormat();
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        line.start();
        int nBytesRead = 0;
        byte[] abData = new byte[128000];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                int nBytesWritten = line.write(abData, 0, nBytesRead);
            }
        }
        line.drain();
        line.close();
    }

}