package soundbgm;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {

    private Clip clip;
    private FloatControl volumeControl;
    private float volume = -10f; // ค่าเริ่มต้น

    public void setFile(String path) {
        try {
            URL url = getClass().getResource(path);
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);

            clip = AudioSystem.getClip();
            clip.open(ais);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    // ⭐ เพิ่มเสียง
    public void volumeUp() {
        volume += 2f;
        if (volume > 6f) volume = 6f;
        volumeControl.setValue(volume);
    }

    // ⭐ ลดเสียง
    public void volumeDown() {
        volume -= 2f;
        if (volume < -80f) volume = -80f;
        volumeControl.setValue(volume);
    }
}