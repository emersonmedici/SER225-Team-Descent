package Sound;

// Every sound effect in the game, and how each one behaves.
// fileName: the file(s) in Resources/Sounds/ -- "name.wav", and/or variations "name_1.wav", "name_2.wav", ...
// volume: how loud it is compared to the other sounds, 0..1
// hearingRange: how many tiles away it can still be heard when played with SoundManager.playAt() (0 = never positioned)
public enum SoundEffect {
    // the player's own footsteps -- quiet, so they become texture instead of noise
    PLAYER_STEP_GRASS("player_step_grass", 0.35f, 0f),
    PLAYER_STEP_DIRT("player_step_dirt", 0.35f, 0f),
    PLAYER_STEP_METAL("player_step_metal", 0.4f, 0f),

    // the monster -- heard from further away than the flashlight reaches, so you hear it before you see it
    MONSTER_STEP("monster_step", 1f, 16f),
    MONSTER_VENT_STEP("monster_vent_step", 1f, 12f),

    // chase stingers -- huge ranges, so they're loud up close but still carry when the monster is far away
    CHASE_INTRO("chase_intro", 1f, 30f),
    CHASE_OUTRO("chase_outro", 0.8f, 40f),

    // interaction feedback -- kept quiet on purpose: a loud "ding" in a horror game breaks the tension
    PICKUP("pickup", 0.5f, 0f),
    DROP("drop", 0.5f, 0f),
    PUZZLE_COMPLETE("puzzle_complete", 0.7f, 0f);

    private final String fileName;
    private final float volume;
    private final float hearingRange;

    SoundEffect(String fileName, float volume, float hearingRange) {
        this.fileName = fileName;
        this.volume = volume;
        this.hearingRange = hearingRange;
    }

    public String getFileName() { return fileName; }
    public float getVolume() { return volume; }
    public float getHearingRange() { return hearingRange; }
}