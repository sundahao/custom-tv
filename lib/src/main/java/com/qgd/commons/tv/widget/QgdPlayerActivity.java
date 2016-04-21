package com.qgd.commons.tv.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.FrameworkSampleSource;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.VideoSurfaceView;
import com.google.android.exoplayer.util.PlayerControl;
import com.qgd.commons.tv.R;
import com.qgd.commons.tv.util.ToastUtils;

/**
 * Created by caiyuxing on 2016/4/21.
 */
public class QgdPlayerActivity extends Activity implements SurfaceHolder.Callback, ExoPlayer.Listener, MediaCodecVideoTrackRenderer.EventListener{
    private VideoSurfaceView surfaceView;
    private ExoPlayer player;
    private MyMediaController mediaController;
    private MediaCodecVideoTrackRenderer videoRenderer;
    private MediaCodecAudioTrackRenderer audioTrackRenderer;
    private long position = 0;//记录播放位置
    private Boolean isPlayWhenReady = true;//是否在缓冲好时自动播放
    private Boolean hasRenderToSurface = false;//是否已经渲染到surface
    private PlayerControl playerControl;
    private String url;
    private String TAG = "[PlayerSDKActivityTAG]";
    private TvDialog mDialog;
    private RelativeLayout rlReplay;
    private TvTextView tvReplay;
    private TvTextView tvBack;
    private ProgressBar loadingIcon;
    private Intent intent;
    private boolean isPlay = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (mediaController.isShowing()) {
                if (isPlay) {
                    releasePlayer();
                    isPlayWhenReady = false;
                    isPlay = false;
                } else {
                    isPlayWhenReady = true;
                    play(isPlayWhenReady);
                    isPlay = true;
                }
            } else {
                mediaController.show(5000);
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (rlReplay.getVisibility() != View.VISIBLE) {
                if (mediaController.isShowing()) {
                    return true;
                } else {
                    mediaController.show(5000);
                    return true;
                }
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (rlReplay.getVisibility() != View.VISIBLE) {
                if (mediaController.isShowing()) {
                    return true;
                } else {
                    mediaController.show(5000);
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyMediaController extends MediaController {

        public MyMediaController(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyMediaController(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                int keyCode = event.getKeyCode();
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (rlReplay.getVisibility() != View.VISIBLE) {
                        playerControl.seekTo(playerControl.getCurrentPosition() + 15000);
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (rlReplay.getVisibility() != View.VISIBLE) {
                        playerControl.seekTo(playerControl.getCurrentPosition() - 15000);
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    if (rlReplay.getVisibility() != VISIBLE) {
                        if (mediaController.isShowing()) {
                            if (isPlay) {
                                if (playerControl != null) {
                                    hasRenderToSurface = false;
                                    isPlayWhenReady = false;
                                    playerControl.pause();
                                    isPlay = false;
                                }
                            } else {
                                isPlayWhenReady = true;
                                play(isPlayWhenReady);
                                isPlay = true;
                            }
                        } else {
                            mediaController.show(5000);
                        }
                    }
                    return true;
                }
            }
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qgd_player);
        loadingIcon = (ProgressBar) findViewById(R.id.loadingIcon);
        intent = getIntent();
        url = intent.getStringExtra("video_url");
        if (url == null || url.length() == 0) {
            ToastUtils.showMessage(this, "video url is null!!!");
            return;
        }
        surfaceView = (VideoSurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);

        mediaController = new MyMediaController(this);
        mediaController.setAnchorView(surfaceView);

        rlReplay = (RelativeLayout) findViewById(R.id.rl_replay);
        tvReplay = (TvTextView) findViewById(R.id.tv_replay);
        tvReplay.setFocusable(false);
        tvBack = (TvTextView) findViewById(R.id.tv_back);
        tvReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlReplay.setVisibility(View.INVISIBLE);
                tvReplay.setFocusable(false);
                preparePlayer();
                isPlayWhenReady = true;
                play(isPlayWhenReady);
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QgdPlayerActivity.this.finish();
            }
        });
        rlReplay.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        //rlReplay.setVisibility(View.INVISIBLE);
        tvReplay.setFocusable(false);
        preparePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        isPlayWhenReady = false;
    }

    /**
     * 创建ExoPlayer
     */
    private void preparePlayer() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loadingIcon.setVisibility(View.VISIBLE);
                loadingIcon.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
                loadingIcon.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                loadingIcon.requestLayout();
                loadingIcon.postInvalidate();
            }
        });
        if (url == null || url.length() == 0) {
            ToastUtils.showMessage(this, "video url is null!!!");
            return;
        }

        if (player == null) {
            player = ExoPlayer.Factory.newInstance(2, 1000, 5000);
            player.addListener(QgdPlayerActivity.this);
            player.seekTo(position);
            player.setPlayWhenReady(true);
            Log.d(TAG, "seekTo:" + position);
            playerControl = new PlayerControl(player);
            mediaController.setMediaPlayer(playerControl);
            mediaController.setEnabled(true);
        }
        buildRenders();
    }

    /**
     * 释放player
     */
    private void releasePlayer() {
        hasRenderToSurface = false;
        mediaController.hide();
        if (player != null) {
            position = player.getCurrentPosition();
            player.release();
            player = null;
            playerControl = null;
        }
        videoRenderer = null;
    }

    /**
     * 创建videoRender和audioRender
     */
    private void buildRenders() {
        FrameworkSampleSource sampleSource = new FrameworkSampleSource(QgdPlayerActivity.this, Uri.parse(url), null, 2);
        videoRenderer = new MediaCodecVideoTrackRenderer(sampleSource,
                MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT,
                0,
                new Handler(getMainLooper()),
                QgdPlayerActivity.this, 50);
        audioTrackRenderer = new MediaCodecAudioTrackRenderer(sampleSource);

        player.prepare(videoRenderer, audioTrackRenderer);
        player.sendMessage(audioTrackRenderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, 0f);
    }

    /**
     * 尝试播放视频
     *
     * @param isPlayWhenReady
     */
    private void play(Boolean isPlayWhenReady) {
        Surface surface = surfaceView.getHolder().getSurface();
        if (surface == null || !surface.isValid()) {
            Log.d(TAG, "surface not ready");
            return;
        }
        hasRenderToSurface = false;
        player.sendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
        player.setPlayWhenReady(isPlayWhenReady);
    }

    /**
     * 重新播放
     */
    private void showReplay() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loadingIcon.setVisibility(View.INVISIBLE);
                loadingIcon.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
                loadingIcon.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                loadingIcon.requestLayout();
                loadingIcon.postInvalidate();
            }
        });
        mediaController.hide();
        Log.e(TAG, "showReplay");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                rlReplay.setVisibility(View.VISIBLE);
                rlReplay.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth();
                rlReplay.getLayoutParams().height = getWindowManager().getDefaultDisplay().getHeight();
                rlReplay.requestLayout();
                rlReplay.postInvalidate();
                tvReplay.setFocusable(true);
            }
        });

        releasePlayer();
        position = 0;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // Do nothing.
        Log.d(TAG, "onPlayerStateChanged, " + playWhenReady + ", " + playbackState);
        isPlayWhenReady = playWhenReady;
        //渲染到surface loading状态
        if (isPlayWhenReady && !hasRenderToSurface) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    loadingIcon.setVisibility(View.VISIBLE);
                    loadingIcon.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
                    loadingIcon.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                    loadingIcon.requestLayout();
                    loadingIcon.postInvalidate();
                }
            });
        }
        switch (playbackState) {
            case ExoPlayer.STATE_ENDED:
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        loadingIcon.setVisibility(View.INVISIBLE);
                        loadingIcon.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
                        loadingIcon.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                        loadingIcon.requestLayout();
                        loadingIcon.postInvalidate();
                    }
                });
                player.seekTo(0);
                showReplay();
                break;
            case ExoPlayer.STATE_BUFFERING:
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        loadingIcon.setVisibility(View.VISIBLE);
                        loadingIcon.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
                        loadingIcon.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                        loadingIcon.requestLayout();
                        loadingIcon.postInvalidate();
                    }
                });
                hasRenderToSurface = false;
                break;
            case ExoPlayer.STATE_READY:
                hasRenderToSurface = true;
                if (!isPlayWhenReady) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            loadingIcon.setVisibility(View.INVISIBLE);
                            loadingIcon.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
                            loadingIcon.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                            loadingIcon.requestLayout();
                            loadingIcon.postInvalidate();
                        }
                    });
                    mediaController.show(5000);
                }
                if (isPlayWhenReady && hasRenderToSurface) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            loadingIcon.setVisibility(View.INVISIBLE);
                            loadingIcon.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
                            loadingIcon.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                            loadingIcon.requestLayout();
                            loadingIcon.postInvalidate();
                        }
                    });
                }

                break;
            default:
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        loadingIcon.setVisibility(View.INVISIBLE);
                        loadingIcon.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
                        loadingIcon.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                        loadingIcon.requestLayout();
                        loadingIcon.postInvalidate();
                    }
                });
                break;
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {
        // Do nothing.
        Log.d(TAG, "onPlayWhenReadyCommitted");
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        Log.d(TAG, "onPlayerError" + e.getMessage());
        ToastUtils.showMessage(QgdPlayerActivity.this, "发生错误, 请检验网络或者文件是否可用");
        showReplay();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio) {
        Log.d(TAG, "onVideoSizeChanged");
        surfaceView.setVideoWidthHeightRatio(height == 0 ? 1 : (pixelWidthHeightRatio * width) / height);
    }

    @Override
    public void onDrawnToSurface(Surface surface) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loadingIcon.setVisibility(View.INVISIBLE);
                loadingIcon.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
                loadingIcon.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
                loadingIcon.requestLayout();
                loadingIcon.postInvalidate();
            }
        });
        hasRenderToSurface = true;
        player.sendMessage(audioTrackRenderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, 1f);
    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {
        Log.d(TAG, "Dropped frames: " + count);

    }

    @Override
    public void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e) {
        Log.d(TAG, "onDecoderInitializationError " + e.getMessage());
        ToastUtils.showMessage(this, "decoder initialization error " + e.getMessage());

        showReplay();
    }

    @Override
    public void onCryptoError(MediaCodec.CryptoException e) {
        Log.d(TAG, "onCryptoError" + e.getMessage());
        ToastUtils.showMessage(this, "onCryptoError " + e.getMessage());

        showReplay();
    }

    // SurfaceHolder.Callback implementation

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");

        play(isPlayWhenReady);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        hasRenderToSurface = false;
    }
}
