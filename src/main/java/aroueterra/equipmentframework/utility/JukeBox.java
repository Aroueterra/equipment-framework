/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.utility;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author aroue
 */
public class JukeBox {

    Map<Song, Clip> playlist = new EnumMap<Song, Clip>(Song.class);

    public JukeBox() {
        registerSong(Song.HEAL, "/sounds/heal.wav");
        registerSong(Song.PAPER, "/sounds/paper.wav");
        registerSong(Song.PAPER_CLOSE, "/sounds/paper_close.wav");
        registerSong(Song.INVENTORY, "/sounds/inventory.wav");
        registerSong(Song.INVENTORY_CLOSE, "/sounds/inventory_close.wav");
        registerSong(Song.SHOP, "/sounds/door_open.wav");
        registerSong(Song.SHOP_CLOSE, "/sounds/door_close.wav");
        registerSong(Song.EQUIP, "/sounds/equip.wav");
        registerSong(Song.GOLD, "/sounds/gold.wav");
        registerSong(Song.SELECT, "/sounds/select.wav");
        registerSong(Song.POTION, "/sounds/potion.wav");
        registerSong(Song.SKILL, "/sounds/poison.wav");
        registerSong(Song.REFRESH, "/sounds/refresh.wav");
    }

    public JukeBox(Song song, String asset) {
        registerSong(song, asset);
    }

    public JukeBox(Song[] song, String[] asset) {
        for (int i = 0; i < song.length; i++) {
            registerSong(song[i], asset[i]);
        }
    }

    public void registerSong(Song song, String s) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(s)));
            playlist.put(song, clip);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(JukeBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Play(Song song) {
        Clip clip = null;
        try {
            clip = playlist.get(song);
            if (!clip.isRunning()) {
                clip.start();
            }

        } catch (Exception e) {
            System.out.println("Error playing audio" + e);
        } finally {
            if (clip != null) {

                clip.setFramePosition(0);
            }

        }
    }
}
