package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    public static int MENU_1 = 0;
	public static int LEVEL_1 = 1;
	public static int LEVEL_2 = 2;
	public static int LEVEL_5 = 3;

	public static int DIE = 0;
	public static int JUMP = 1;
	public static int GAMEOVER = 2;
	public static int LVL_COMPLETED = 3;
	public static int ATTACK_ONE = 4;
	public static int ATTACK_TWO = 5;
	public static int TOUCH = 6;

    private Clip[] songs, effects;
    private int currentSoundId;
    private float volume = 0.5f;
    private boolean songMute, effectMute;
    private Random rand = new Random();

    public AudioPlayer() {
        loadSongs();   
        loadEffects();
        playSong(MENU_1);
    }

    private void loadSongs() {
        String[] names = {"menu1", "level1", "level2", "level5"};
        songs = new Clip[names.length];

        for(int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }
    }

    private void loadEffects() {
        String[] effectNames = {"death", "jump", "gameOver", "levelComplete", "slice1", "slice2", "touch"};
        effects = new Clip[effectNames.length];

        for(int i = 0; i < effects.length; i++) {
            effects[i] = getClip(effectNames[i]);
        }

        updateEffectsVolume();
    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
		AudioInputStream audio;

		try {
			audio = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(audio);
			return c;

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

			e.printStackTrace();
		}

		return null;
    }

    public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
		updateEffectsVolume();
	}

	public void stopSong() {
		if (songs[currentSoundId].isActive())
			songs[currentSoundId].stop();
	}

	public void setLevelSong(int lvlIndex) {
		if ( lvlIndex == 0 || lvlIndex == 2 || lvlIndex == 4)
			playSong(LEVEL_1);
		else if(lvlIndex == 1 || lvlIndex == 3)
			playSong(LEVEL_2);
		else
			playSong(LEVEL_5);
	}

	public void lvlCompleted() {
		//stopSong();
		playEffect(LVL_COMPLETED);
	}

    private void updateSongVolume() {

		FloatControl gainControl = (FloatControl) songs[currentSoundId].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);

	}

    private void updateEffectsVolume() {
		for (Clip c : effects) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}

    public void toggleSongMute() {
		this.songMute = !songMute;
		for (Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
	}

	public void toggleEffectMute() {
		this.effectMute = !effectMute;
		for (Clip c : effects) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(effectMute);
		}
		if (!effectMute)
			playEffect(JUMP);
	}

    public void playEffect(int effect) {
		effects[effect].setMicrosecondPosition(0);
		effects[effect].start();
	}

    public void playAttackSound() {
		int start = 4;
		start += rand.nextInt(2);
		playEffect(start);
	}

    public void playSong(int song) {
		stopSong();

		currentSoundId = song;
		updateSongVolume();
		songs[currentSoundId].setMicrosecondPosition(0);
		songs[currentSoundId].loop(Clip.LOOP_CONTINUOUSLY);
	}
}
