package me.li2.movies.util.video

import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.ui.PlayerView
import im.ene.toro.ToroPlayer
import im.ene.toro.exoplayer.ExoCreator
import im.ene.toro.exoplayer.Playable
import im.ene.toro.media.PlaybackInfo
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

interface VideoPlayerAware :
        Playable.EventListener,
        LifecycleObserver,
        KodeinAware {

    private val exoCreator: ExoCreator
        get() {
            val result by instance<ExoCreator>()
            return result
        }

    val videoUri: Uri?
    val videoPlayerView: PlayerView
    val initialPlaybackInfo: PlaybackInfo
    var videoPlayerHelper: Playable?

    val currentPlaybackInfo
        get() = videoPlayerHelper?.playbackInfo ?: PlaybackInfo()


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startVideoPlay() {
        if (videoUri == null) return
        if (videoPlayerHelper == null) {
            videoPlayerHelper = exoCreator.createPlayable(videoUri!!, null)
            videoPlayerHelper?.addEventListener(this)
            videoPlayerHelper?.prepare(true)
        }
        videoPlayerHelper?.run {
            playerView = videoPlayerView
            playbackInfo = initialPlaybackInfo
            play()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseVideoPlay() {
        videoPlayerHelper?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopVideoPlay() {
        videoPlayerHelper?.run {
            removeEventListener(this@VideoPlayerAware)
            playerView = null
            release()
        }
        videoPlayerHelper = null
    }

    fun onVideoLoading(isLoading: Boolean)

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        onVideoLoading(playbackState == ToroPlayer.State.STATE_BUFFERING)
    }

    override fun onCues(cues: MutableList<Cue>?) {}

    override fun onMetadata(metadata: Metadata?) {}
}
