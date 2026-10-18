package Music;

import Engine.Config;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

// One looping piece of music, loaded fully into memory.
// Volume is set on a 0..1 scale and converted to decibels, which is what Java's mixer actually works in.
// If the file is missing or in a format Java can't play, the track stays silent instead of crashing the game.
public class MusicTrack {
    // Java's gain control usually bottoms out around -80 dB, which is effectively silent
    private static final float SILENT_DB = -80f;

    private final String fileName;
    private Clip clip;
    private FloatControl gainControl;
    private float volume = 0f;

    public MusicTrack(String fileName) {
        this.fileName = fileName;
        String path = Config.MUSIC_PATH + fileName;

        // try-with-resources closes the file stream for us; by then the clip has already copied the audio into memory
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path))) {
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                System.out.println("Volume control not supported for " + path + ", it will play at full volume");
            }
            setVolume(0f);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | IllegalArgumentException ex) {
            // a bad music file should never crash the game -- it just plays nothing
            System.out.println("Unable to load music file " + path + ": " + ex.getMessage());
            clip = null;
        }
    }

    // starts (or resumes) looping from wherever the track currently is
    public void play() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // jumps back to the start of the track (used to replay the chase track's intro)
    public void restart() {
        if (clip != null) {
            clip.setFramePosition(0);
        }
    }

    // stops playback and frees the audio memory -- the track can't be used after this
    public void close() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    // volume is 0 (silent) to 1 (the file's original loudness)
    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume));
        if (gainControl == null) {
            return;
        }

        // decibels are logarithmic: half volume is about -6 dB, a tenth is -20 dB, and zero would be -infinity
        float decibels = SILENT_DB;
        if (this.volume > 0) {
            decibels = Math.max(SILENT_DB, (float) (20 * Math.log10(this.volume)));
        }

        // the control throws an exception if given a value outside its range, so clamp to what this system supports
        decibels = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), decibels));
        gainControl.setValue(decibels);
    }

    public float getVolume() { return volume; }
    public String getFileName() { return fileName; }
    public boolean isLoaded() { return clip != null; }
}