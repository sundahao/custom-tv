package com.qgd.commons.tv.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import java.io.IOException;

/**
 * 作者：ethan on 2016/3/15 11:22
 * 邮箱：ethan.chen@fm2020.com
 */
public class FocusSoundUtil {
    private static final String TAG = FocusSoundUtil.class.getSimpleName();
    private int play = 0;
    public static boolean enableSound=true;
    public interface DispatchKeyEventListener{
        void dispatchKeyEvent(View view, KeyEvent event);
    }
    public static DispatchKeyEventListener listener;

    public static void dispatchKeyEvent(View view, KeyEvent event) {
        try {
            if(listener!=null){
                listener.dispatchKeyEvent(view,event);
            }
            if(!enableSound){
                return;
            }
            int direction = 0;
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_LEFT;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_RIGHT;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_UP;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_DOWN;
                    }
                    break;
                case KeyEvent.KEYCODE_TAB:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_FORWARD;
                    } else if (event.hasModifiers(KeyEvent.META_SHIFT_ON)) {
                        direction = View.FOCUS_BACKWARD;
                    }
                    break;
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (direction != 0) {
                    View focused = view.findFocus();
                    if (focused != null) {
                        View v = focused.focusSearch(direction);
                        if (v != null && v != focused) {
                            playSoundEffect(view.getContext(), SoundEffectConstants.getContantForFocusDirection(direction), soundVolume);
                        } else if (v != focused) {
                            if (v == null) {
                                try {
                                    ViewParent viewPage=null;
                                    if(focused.getParent() != null && focused.getParent() instanceof ViewPager){
                                        viewPage=focused.getParent();
                                    }else  if(focused.getParent() != null && focused.getParent().getParent() instanceof ViewPager){
                                        viewPage=focused.getParent().getParent();
                                    }else  if(focused.getParent() != null && focused.getParent().getParent()!=null&& focused.getParent().getParent().getParent() instanceof ViewPager){
                                        viewPage=focused.getParent().getParent().getParent();
                                    }
                                    if (viewPage!=null) {
                                        ViewPager vp = (ViewPager)viewPage ;
                                        int current = vp.getCurrentItem();
                                        int total = vp.getAdapter().getCount();

                                        if(direction==View.FOCUS_LEFT) {
                                            if (current != 0) {
                                                playSoundEffect(view.getContext(), SoundEffectConstants.getContantForFocusDirection(direction), soundVolume);
                                                return;
                                            }
                                        }else if(direction==View.FOCUS_RIGHT){
                                            if(current!=(total-1)){
                                                playSoundEffect(view.getContext(), SoundEffectConstants.getContantForFocusDirection(direction), soundVolume);
                                                return;
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                playSoundEffect(view.getContext(), AudioManager.FX_KEYPRESS_INVALID, soundVolume);
                            }
                        }


                    }
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int soundVolume = 3000;
    private static boolean isLoad = false;

    private static final int NUM_SOUNDPOOL_CHANNELS = 6;
    private static final int SOUND_EFFECT_VOLUME = 1000;
    private static SoundPool mSoundPool;
    private static Object mSoundEffectsLock = new Object();
    private static int[][] SOUND_EFFECT_FILES_MAP = new int[][]{{0, -1},  // FX_KEY_CLICK
            {0, -1},  // FX_FOCUS_NAVIGATION_UP
            {0, -1},  // FX_FOCUS_NAVIGATION_DOWN
            {0, -1},  // FX_FOCUS_NAVIGATION_LEFT
            {0, -1},  // FX_FOCUS_NAVIGATION_RIGHT
            {1, -1},  // FX_KEYPRESS_STANDARD
            {2, -1},  // FX_KEYPRESS_SPACEBAR
            {3, -1},  // FX_FOCUS_DELETE
            {4, -1},   // FX_FOCUS_RETURN
            {5, -1}};
    /* Sound effect file names  */
    private static final String SOUND_EFFECTS_PATH = "/media/audio/ui/";
    private static final String[] SOUND_EFFECT_FILES = new String[]{"Effect_Tick.ogg", "KeypressStandard.ogg", "KeypressSpacebar.ogg", "KeypressDelete.ogg", "KeypressReturn.ogg", "KeypressInvalid.ogg"};

    public static boolean loadSoundEffects() {

        synchronized (mSoundEffectsLock) {
            mSoundPool = new SoundPool(NUM_SOUNDPOOL_CHANNELS, AudioManager.STREAM_RING, 0);
            if (mSoundPool == null) {
                return false;
            }
            /*
             * poolId table: The value -1 in this table indicates that corresponding
             * file (same index in SOUND_EFFECT_FILES[] has not been loaded.
             * Once loaded, the value in poolId is the sample ID and the same
             * sample can be reused for another effect using the same file.
             */
            int[] poolId = new int[SOUND_EFFECT_FILES.length];
            for (int fileIdx = 0; fileIdx < SOUND_EFFECT_FILES.length; fileIdx++) {
                poolId[fileIdx] = -1;
            }
            /*
             * Effects whose value in SOUND_EFFECT_FILES_MAP[effect][1] is -1 must be loaded.
             * If load succeeds, value in SOUND_EFFECT_FILES_MAP[effect][1] is > 0:
             * this indicates we have a valid sample loaded for this effect.
             */
            for (int effect = 0; effect < 10; effect++) {
                // Do not load sample if this effect uses the MediaPlayer
                if (SOUND_EFFECT_FILES_MAP[effect][1] == 0) {
                    continue;
                }
                if (poolId[SOUND_EFFECT_FILES_MAP[effect][0]] == -1) {
                    String filePath = Environment.getRootDirectory() + SOUND_EFFECTS_PATH + SOUND_EFFECT_FILES[SOUND_EFFECT_FILES_MAP[effect][0]];
                    Log.d(TAG, "sound effect filePath:" + filePath);

                    int sampleId = mSoundPool.load(filePath, 0);
                    SOUND_EFFECT_FILES_MAP[effect][1] = sampleId;
                    poolId[SOUND_EFFECT_FILES_MAP[effect][0]] = sampleId;
                    if (sampleId <= 0) {
                        Log.w(TAG, "Soundpool could not load file: " + filePath);
                    }
                } else {
                    SOUND_EFFECT_FILES_MAP[effect][1] = poolId[SOUND_EFFECT_FILES_MAP[effect][0]];
                }
            }
        }
        return true;
    }

    static {
    }


    public static void initSoundEffect(Context context) {
        if (!isLoad) {
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            loadSoundEffects();
            isLoad = true;
        }
    }

    private static void playSoundEffect(Context context, int effectType, int volume) {
        playSoundEffect(effectType, volume);
    }

    public static void playSoundEffect(int effectType) {
        playSoundEffect(effectType, soundVolume);
    }

    private static void playSoundEffect(int effectType, int volume) {
        synchronized (mSoundEffectsLock) {
            if (mSoundPool == null) {
                return;
            }

            if (SOUND_EFFECT_FILES_MAP[effectType][1] > 0) {
                float v = (float) volume / 1000.0f;
                mSoundPool.play(SOUND_EFFECT_FILES_MAP[effectType][1], v, v, 0, 0, 1.0f);
            } else {
                MediaPlayer mediaPlayer = new MediaPlayer();
                if (mediaPlayer != null) {
                    try {
                        String filePath = Environment.getRootDirectory() + SOUND_EFFECTS_PATH + SOUND_EFFECT_FILES[SOUND_EFFECT_FILES_MAP[effectType][0]];
                        mediaPlayer.setDataSource(filePath);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.prepare();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {

                                cleanupPlayer(mp);
                            }
                        });
                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                cleanupPlayer(mp);
                                return true;
                            }
                        });
                        mediaPlayer.start();
                    } catch (IOException ex) {
                        Log.w(TAG, "MediaPlayer IOException: " + ex);
                    } catch (IllegalArgumentException ex) {
                        Log.w(TAG, "MediaPlayer IllegalArgumentException: " + ex);
                    } catch (IllegalStateException ex) {
                        Log.w(TAG, "MediaPlayer IllegalStateException: " + ex);
                    }
                }
            }
        }
    }

    private static void cleanupPlayer(MediaPlayer mp) {
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
            } catch (IllegalStateException ex) {
                Log.w(TAG, "MediaPlayer IllegalStateException: " + ex);
            }
        }
    }

}
