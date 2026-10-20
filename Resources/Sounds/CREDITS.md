# Sound Effect Credits

Every sound in this folder comes from a **CC0 (public domain)** source. CC0 means anyone can use, change and share these sounds without asking and without giving credit. We list the authors anyway because it's good practice, and so anyone can find the originals.

All files here were cut and processed from the originals: sliced into single hits, trimmed, faded, mixed down to mono, normalized to -1 dBFS, and saved as 16-bit PCM WAV. Any other processing is listed per file.

| Game file(s) | Original | Author | Source | License |
|---|---|---|---|---|
| `player_step_grass_1-4.wav` | "Steps in the Grass, Slow" (#1253) | Joseph Sardin | [BigSoundBank](https://bigsoundbank.com/steps-in-the-grass-slow-s1253.html) | CC0 |
| `player_step_dirt_1-4.wav` | "Footsteps on gravels #1" (#0510) | Joseph Sardin | [BigSoundBank](https://bigsoundbank.com/footsteps-on-gravels-1-s0510.html) | CC0 |
| `player_step_metal_1-4.wav` | "Metal footsteps on concrete" (steps 07, 10, 11, 12) | Thimras | [OpenGameArt](https://opengameart.org/content/metal-footsteps-on-concrete) | CC0 |
| `monster_step_1-3.wav` | "Canvas bag on floor" (#0576, hits 1, 6, 9) layered with "Metal footsteps on concrete" (steps 01, 03, 05) | Joseph Sardin; Thimras | [BigSoundBank](https://bigsoundbank.com/canvas-bag-on-floor-s0576.html); [OpenGameArt](https://opengameart.org/content/metal-footsteps-on-concrete) | CC0 |
| `monster_vent_step_1-3.wav` | "Metal Impact Sounds" (thud2, thud3) | Brian MacIntosh (BMacZero) | [OpenGameArt](https://opengameart.org/content/metal-impact-sounds) | CC0 |
| `chase_intro.wav`, `chase_outro.wav` | "CC0 deep monster roar" | trazzz123 | [OpenGameArt](https://opengameart.org/content/cc0-deep-monster-roar) | CC0 |
| `pickup_1-2.wav` | "Synthetic Coat Rubbed" (#1104) | Joseph Sardin | [BigSoundBank](https://bigsoundbank.com/synthetic-coat-rubbed-s1104.html) | CC0 |
| `drop_1-3.wav` | "Canvas bag on floor" (#0576, hits 4, 5, 8) | Joseph Sardin | [BigSoundBank](https://bigsoundbank.com/canvas-bag-on-floor-s0576.html) | CC0 |
| `puzzle_complete.wav` | "Industrial switch #1" (#3061) + "Electric strike #1" (#3442) | Joseph Sardin | [BigSoundBank #3061](https://bigsoundbank.com/industrial-switch-1-s3061.html), [#3442](https://bigsoundbank.com/electric-strike-1-s3442.html) | CC0 |

## Extra processing

- **Footsteps (grass, dirt):** each step was found automatically in the long recording, skipping clipped or spiky hits. Each one starts right at the body of the step so it lines up with the movement, and a high-pass filter at 60 Hz removes wind rumble.
- **monster_step:** the bag thud is pitched an octave down (played at half speed) and low-passed at 700 Hz so it's all weight. A metal step pitched down 30% sits on top at about -9 dB (the facility's metal floor), plus a little room reverb.
- **monster_vent_step:** low-passed at 2.5 kHz to sound muffled inside a duct. Variation 3 is thud3 pitched 15% lower.
- **chase_intro:** the roar trimmed to 5.5 s with a 1.5 s fade-out.
- **chase_outro:** the same roar pitched 20% lower, low-passed at 1.1 kHz, with a long reverb tail so it reads as the same creature, far away.
- **puzzle_complete:** the switch clunk, then the electric-lock buzz 0.25 s later at about -4 dB, cut to 2.2 s with a fade.

## Replacing a sound

Drop in a new file with the same name. Variations are numbered `_1`, `_2`, ... with no gaps, up to 9. Any PCM WAV works (mono or stereo, 8/16/24-bit, any sample rate), but not 32-bit float.
