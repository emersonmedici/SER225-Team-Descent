package Music;

import Engine.Config;
import Level.Map;
import Level.Player;
import Level.Threat;
import Sound.SoundEffect;
import Sound.SoundManager;
import Utils.Point;

// Decides what the player hears: the current map's ambience, or its chase track when a threat gets close.
// Both tracks loop at the same time and this class blends their volumes, so switching never stops or skips the music.
// PlayLevelScreen calls update() once per frame.
public class MusicDirector {
    // distances are in tiles so they still make sense if the tile scale changes
    // the flashlight reaches 6 tiles, so a 7 tile trigger means you HEAR the monster before you can SEE it
    private float chaseStartDistance = 7f;
    // the chase only ends once every threat is further than this (the gap stops the music flickering at the edge)
    private float chaseEndDistance = 11f;
    // after you get away, the chase music refuses to let go for this long
    private float chaseLingerSeconds = 3f;

    // how long each fade takes, in seconds
    private float chaseFadeInSeconds = 0.75f; // fast: the threat should hit like a jolt
    private float chaseFadeOutSeconds = 5f;   // slow: relief should come late and reluctantly
    private float mapFadeInSeconds = 3f;      // every new map rises out of silence

    // overall loudness of all music, 0..1
    private float masterVolume = 0.8f;

    private Map currentMap;
    private MusicTrack ambientTrack;
    private MusicTrack chaseTrack;

    private boolean isChasing = false;
    private int lingerFramesLeft = 0;

    // the lerp amount between the two tracks: 0 = all ambience, 1 = all chase
    private float chaseMix = 0f;
    // fades the whole mix in from silence when a map loads: 0 = silent, 1 = full
    private float mapFade = 0f;

    // where the nearest hunting threat was last seen -- the chase roar and outro come from here
    private Point lastThreatCenter;

    public void update(Map map, Player player) {
        if (map != currentMap) {
            onMapChanged(map);
        }

        updateChaseState(map, player);
        updateMix();
        applyVolumes();
    }

    // silences and frees all music (call this when leaving the level entirely)
    public void stop() {
        if (ambientTrack != null) {
            ambientTrack.close();
        }
        if (chaseTrack != null) {
            chaseTrack.close();
        }
        ambientTrack = null;
        chaseTrack = null;
        currentMap = null;
        isChasing = false;
        chaseMix = 0f;
        mapFade = 0f;
        lastThreatCenter = null;
    }

    private void onMapChanged(Map map) {
        currentMap = map;

        // only reload a track if the file is different -- after dying, the same song keeps going instead of restarting
        ambientTrack = swapTrack(ambientTrack, map.getAmbientTrack());
        chaseTrack = swapTrack(chaseTrack, map.getChaseTrack());

        // every map starts calm and rises out of silence (on a death, this is a hard cut to quiet)
        // isChasing is reset directly, NOT through the chase-ending code, so dying never plays the chase outro
        isChasing = false;
        lingerFramesLeft = 0;
        chaseMix = 0f;
        mapFade = 0f;
        lastThreatCenter = null;
    }

    // returns the track that should be playing for newFileName, reusing oldTrack if it's the same file
    private MusicTrack swapTrack(MusicTrack oldTrack, String newFileName) {
        if (oldTrack != null && oldTrack.getFileName().equals(newFileName)) {
            return oldTrack;
        }
        if (oldTrack != null) {
            oldTrack.close();
        }
        if (newFileName == null) {
            return null;
        }
        MusicTrack newTrack = new MusicTrack(newFileName);
        newTrack.play();
        return newTrack;
    }

    private void updateChaseState(Map map, Player player) {
        float distance = getNearestThreatDistance(map, player);

        if (distance <= chaseStartDistance && !isChasing) {
            // a brand new chase starts the chase track from its beginning so its opening hits every time,
            // but if the old chase was still fading out, carry on from where it is instead of jumping
            if (chaseMix == 0f) {
                if (chaseTrack != null) {
                    chaseTrack.restart();
                }
                // the roar comes from the monster's position, so it tells you which way NOT to run
                // only fresh chases roar: being re-spotted mid-fade-out just picks the chase back up
                SoundManager.playAt(SoundEffect.CHASE_INTRO, lastThreatCenter.x, lastThreatCenter.y);
            }
            isChasing = true;
        }

        if (isChasing) {
            if (distance <= chaseEndDistance) {
                // still close: keep the linger timer full
                lingerFramesLeft = Math.round(chaseLingerSeconds * Config.TARGET_FPS);
            } else {
                // far enough away: count down before letting the player feel safe
                lingerFramesLeft--;
                if (lingerFramesLeft <= 0) {
                    isChasing = false;
                    playChaseOutro();
                }
            }
        }
    }

    // distance in tiles from the player to the closest hunting threat (or infinity if there are none)
    private float getNearestThreatDistance(Map map, Player player) {
        float playerCenterX = player.getX() + player.getWidth() / 2f;
        float playerCenterY = player.getY() + player.getHeight() / 2f;
        float tileSize = map.getTileset().getScaledSpriteWidth();

        float nearest = Float.POSITIVE_INFINITY;
        for (Threat threat : map.getThreats()) {
            if (!threat.isHunting()) {
                continue;
            }
            Point threatCenter = threat.getThreatCenter();
            float dx = threatCenter.x - playerCenterX;
            float dy = threatCenter.y - playerCenterY;
            float distanceInTiles = (float) Math.sqrt(dx * dx + dy * dy) / tileSize;
            if (distanceInTiles < nearest) {
                nearest = distanceInTiles;
                lastThreatCenter = threatCenter;
            }
        }
        return nearest;
    }

    // the chase is over: a far-off roar from wherever the monster was last heard -- it lost you, but it's still out there
    private void playChaseOutro() {
        if (lastThreatCenter != null) {
            SoundManager.playAt(SoundEffect.CHASE_OUTRO, lastThreatCenter.x, lastThreatCenter.y);
        } else {
            SoundManager.play(SoundEffect.CHASE_OUTRO);
        }
    }

    // slides chaseMix and mapFade toward where they should be, a little bit each frame
    private void updateMix() {
        float chaseTarget = isChasing ? 1f : 0f;
        float chaseFadeSeconds = isChasing ? chaseFadeInSeconds : chaseFadeOutSeconds;
        chaseMix = moveToward(chaseMix, chaseTarget, 1f / (chaseFadeSeconds * Config.TARGET_FPS));

        mapFade = moveToward(mapFade, 1f, 1f / (mapFadeInSeconds * Config.TARGET_FPS));
    }

    // turns chaseMix into a volume for each track
    private void applyVolumes() {
        // equal-power crossfade: a plain lerp (1 - t and t) sounds quieter in the middle of the fade,
        // so we lerp along a quarter circle instead -- the total loudness stays the same the whole way through
        float angle = chaseMix * (float) (Math.PI / 2);
        float ambientVolume = (float) Math.cos(angle);
        float chaseVolume = (float) Math.sin(angle);

        float overall = masterVolume * mapFade;
        if (ambientTrack != null) {
            ambientTrack.setVolume(ambientVolume * overall);
        }
        if (chaseTrack != null) {
            chaseTrack.setVolume(chaseVolume * overall);
        }
    }

    // moves value toward target by at most maxStep, without overshooting
    private static float moveToward(float value, float target, float maxStep) {
        if (value < target) {
            return Math.min(value + maxStep, target);
        }
        return Math.max(value - maxStep, target);
    }

    public boolean isChasing() { return isChasing; }
    public float getChaseMix() { return chaseMix; }
}