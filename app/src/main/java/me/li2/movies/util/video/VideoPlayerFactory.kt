package me.li2.movies.util.video

import android.content.Context
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import im.ene.toro.exoplayer.Config
import im.ene.toro.exoplayer.ExoCreator
import im.ene.toro.exoplayer.MediaSourceBuilder
import im.ene.toro.exoplayer.ToroExo
import java.io.File

/**
 * Toro player cache videos
 * https://github.com/eneim/toro/issues/290#issuecomment-411264448
 */
class VideoPlayerFactory(private val context: Context) {

    val exoCreator: ExoCreator
        get() {
            val cache = SimpleCache(File("${context.filesDir.path}/$CACHED_FILE_PATH"),
                    LeastRecentlyUsedCacheEvictor(CACHED_FILE_SIZE))
            val config = Config.Builder()
                    .setMediaSourceBuilder(MediaSourceBuilder.LOOPING)
                    .setCache(cache)
                    .build()
            return ToroExo.with(context).getCreator(config)
        }

    companion object {
        private const val FILE_SIZE_1M = 1024 * 1024L
        // size of each cached file
        private const val CACHED_FILE_SIZE = 10 * FILE_SIZE_1M
        // path /data/data/package_name/files/video_cache
        private const val CACHED_FILE_PATH = "video_cache"
    }
}
