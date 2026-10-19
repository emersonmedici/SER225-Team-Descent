package Sound;

import java.util.EnumMap;

// The one place the rest of the game goes to make a sound.
//   play()   -- a sound with no position: the player's own footsteps, picking things up, puzzle feedback
//   playAt() -- a sound that comes from somewhere in the world: it gets quieter with distance and pans left/right
// Every method is synchronized because the minigame windows run on Swing's thread, not the game loop's thread,
// and two threads must never load or start the same clips at the same time.
public class SoundManager {
    // how far to the side (in tiles) a sound has to be before it's panned as far as it goes
    private static final float PAN_WIDTH_TILES = 8f;
    // never pan 100% into one ear -- fully one-sided sound feels like a broken headphone, not like space
    private static final float MAX_PAN = 0.8f;

    private static final EnumMap<SoundEffect, LoadedSound> loadedSounds = new EnumMap<>(SoundEffect.class);
    private static float masterVolume = 1f;

    // where the "ears" are -- the player sets this every frame
    private static float listenerX, listenerY;
    private static float tileSize = 48f;

    // prevents SoundManager from being instantiated, same as Keyboard and Config
    private SoundManager() { }

    public static synchronized void play(SoundEffect effect) {
        play(effect, 1f);
    }

    // volumeScale makes this one play louder or quieter (running footsteps are louder than walking ones, for example)
    public static synchronized void play(SoundEffect effect, float volumeScale) {
        getLoadedSound(effect).play(effect.getVolume() * volumeScale * masterVolume, 0f);
    }

    // x and y are world pixels, the same coordinates entities use for their positions
    public static synchronized void playAt(SoundEffect effect, float x, float y) {
        // a sound with no hearing range isn't meant to be positioned, so just play it normally
        if (effect.getHearingRange() <= 0) {
            play(effect);
            return;
        }

        float dx = x - listenerX;
        float dy = y - listenerY;
        float distanceInTiles = (float) Math.sqrt(dx * dx + dy * dy) / tileSize;
        if (distanceInTiles >= effect.getHearingRange()) {
            return; // too far away to hear at all, so don't bother starting a clip
        }

        // 1 right on top of you, 0 at the edge of hearing -- squared so it drops off quickly at first, like real sound
        float closeness = 1f - distanceInTiles / effect.getHearingRange();
        float distanceVolume = closeness * closeness;

        // only left/right matters for panning: a sound straight above or below you comes from the middle
        float pan = Math.max(-1f, Math.min(1f, dx / (PAN_WIDTH_TILES * tileSize))) * MAX_PAN;

        getLoadedSound(effect).play(effect.getVolume() * distanceVolume * masterVolume, pan);
    }

    // the player calls this every frame with their center and the map's tile size
    public static synchronized void setListener(float x, float y, float tileSize) {
        listenerX = x;
        listenerY = y;
        SoundManager.tileSize = tileSize;
    }

    // loads every sound up front so the first footstep doesn't hitch while its file is read
    public static synchronized void preloadAll() {
        for (SoundEffect effect : SoundEffect.values()) {
            getLoadedSound(effect);
        }
    }

    public static synchronized void setMasterVolume(float volume) {
        masterVolume = Math.max(0f, Math.min(1f, volume));
    }

    public static synchronized float getMasterVolume() {
        return masterVolume;
    }

    // loads a sound the first time it's asked for, then reuses it forever
    private static LoadedSound getLoadedSound(SoundEffect effect) {
        LoadedSound sound = loadedSounds.get(effect);
        if (sound == null) {
            sound = new LoadedSound(effect.getFileName());
            loadedSounds.put(effect, sound);
        }
        return sound;
    }
}