package Sound;

import Engine.Config;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

// One sound effect loaded into memory, ready to play instantly.
// It holds every variation of the sound (name_1.wav, name_2.wav...), and each variation gets a couple of copies ("voices")
// so the same sound can overlap itself -- two quick footsteps, or two monsters stepping at the same moment.
class LoadedSound {
    private static final int MAX_VARIATIONS = 9;
    private static final int VOICES_PER_VARIATION = 2;
    // Java's gain control bottoms out around -80 dB, which is effectively silent
    private static final float SILENT_DB = -80f;
    private static final Random random = new Random();

    // variations.get(variation)[voice]
    private final ArrayList<Clip[]> variations = new ArrayList<>();
    // which voice each variation should use next
    private final int[] nextVoice;
    private int lastVariation = -1;

    LoadedSound(String baseName) {
        for (File file : findFiles(baseName)) {
            Clip[] voices = loadVoices(file);
            if (voices != null) {
                variations.add(voices);
            }
        }
        nextVoice = new int[variations.size()];

        if (variations.isEmpty()) {
            // a missing sound should never crash the game -- it just stays silent
            System.out.println("No sound files found for " + baseName + " (looked for " + Config.SOUNDS_PATH + baseName + ".wav and " + baseName + "_1.wav, _2.wav...)");
        }
    }

    // "name.wav" plus "name_1.wav", "name_2.wav"... stopping at the first missing number
    private static ArrayList<File> findFiles(String baseName) {
        ArrayList<File> files = new ArrayList<>();
        File single = new File(Config.SOUNDS_PATH + baseName + ".wav");
        if (single.exists()) {
            files.add(single);
        }
        for (int i = 1; i <= MAX_VARIATIONS; i++) {
            File variation = new File(Config.SOUNDS_PATH + baseName + "_" + i + ".wav");
            if (!variation.exists()) {
                break;
            }
            files.add(variation);
        }
        return files;
    }

    // reads the file once, then opens several clips that all share the same audio data
    private static Clip[] loadVoices(File file) {
        try (AudioInputStream fileStream = AudioSystem.getAudioInputStream(file)) {
            AudioInputStream stream = toStereo(fileStream);
            AudioFormat format = stream.getFormat();
            byte[] audioData = stream.readAllBytes();

            Clip[] voices = new Clip[VOICES_PER_VARIATION];
            for (int i = 0; i < voices.length; i++) {
                voices[i] = AudioSystem.getClip();
                voices[i].open(format, audioData, 0, audioData.length);
            }
            return voices;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | IllegalArgumentException ex) {
            System.out.println("Unable to load sound file " + file.getPath() + ": " + ex.getMessage());
            return null;
        }
    }

    // Java only offers left/right panning on stereo clips, so mono files get copied into both ears
    private static AudioInputStream toStereo(AudioInputStream stream) {
        AudioFormat format = stream.getFormat();
        if (format.getChannels() != 1) {
            return stream;
        }
        AudioFormat stereo = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(),
                2, format.getFrameSize() * 2, format.getFrameRate(), format.isBigEndian());
        return AudioSystem.getAudioInputStream(stereo, stream);
    }

    // volume is 0..1, pan is -1 (left) to 1 (right)
    void play(float volume, float pan) {
        if (variations.isEmpty() || volume <= 0) {
            return;
        }

        int variation = pickVariation();
        Clip[] voices = variations.get(variation);

        // voices take turns; if they're all still playing, the oldest gets cut off (it's nearly finished anyway)
        Clip clip = voices[nextVoice[variation]];
        nextVoice[variation] = (nextVoice[variation] + 1) % voices.length;

        // every play is up to 10% louder or quieter, so repeats never sound copy-pasted
        float variedVolume = volume * (0.9f + random.nextFloat() * 0.2f);

        clip.stop();
        clip.setFramePosition(0);
        setVolume(clip, variedVolume);
        setPan(clip, pan);
        clip.start();
    }

    // a random variation, but never the same one twice in a row -- exact repeats are what make footsteps sound robotic
    private int pickVariation() {
        int variation;
        do {
            variation = random.nextInt(variations.size());
        } while (variations.size() > 1 && variation == lastVariation);
        lastVariation = variation;
        return variation;
    }

    private static void setVolume(Clip clip, float volume) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }
        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        // same decibel conversion as MusicTrack: half volume is about -6 dB
        float decibels = Math.max(SILENT_DB, (float) (20 * Math.log10(Math.min(volume, 1f))));
        gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), decibels)));
    }

    private static void setPan(Clip clip, float pan) {
        // BALANCE is the right control for stereo audio; PAN is the fallback on systems that only offer that
        if (clip.isControlSupported(FloatControl.Type.BALANCE)) {
            ((FloatControl) clip.getControl(FloatControl.Type.BALANCE)).setValue(pan);
        } else if (clip.isControlSupported(FloatControl.Type.PAN)) {
            ((FloatControl) clip.getControl(FloatControl.Type.PAN)).setValue(pan);
        }
    }
}