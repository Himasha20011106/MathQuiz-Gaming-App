package com.example.mathiq

import android.media.MediaPlayer

object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null

    fun initializeMediaPlayer(mediaPlayer: MediaPlayer) {
        this.mediaPlayer = mediaPlayer
    }

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
