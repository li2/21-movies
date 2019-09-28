package me.li2.movies.util

import me.li2.movies.ui.movies.MovieItem
import me.li2.movies.ui.movies.MoviesType

object Mocks {
    fun getMovies(type: MoviesType): List<MovieItem> = when (type) {
        MoviesType.NOT_SHOWING -> listOf(
                MovieItem(1, "F1", "1H 20M", "Ad", 4.2f, "https://archive.org/download/WildlifeSampleVideo/Wildlife.mp4"),
                MovieItem(2, "F1", "1H 20M", "Ad", 4.2f, "https://archive.org/download/SampleVideo_908/Bear.mp4"),
                MovieItem(3, "F1", "1H 20M", "Ad", null, "https://archive.org/download/sample_video_clip_Shark_swim_240/Shark_clip4_240_1.mp4"),
                MovieItem(4, "F1", "1H 20M", "Ad", 4.2f, "https://archive.org/download/SampleVideo1280x7205mb/SampleVideo_1280x720_5mb.mp4"),
                MovieItem(5, "F1", "1H 20M", "Ad", 4.2f, "https://archive.org/download/WildlifeSampleVideo/Wildlife.mp4"),
                MovieItem(6, "F1", "1H 20M", "Ad", 4.2f, "https://archive.org/download/SampleVideo_908/Bear.mp4"),
                MovieItem(7, "F1", "1H 20M", "Ad", null, "https://archive.org/download/sample_video_clip_Shark_swim_240/Shark_clip4_240_1.mp4"),
                MovieItem(8, "F1", "1H 20M", "Ad", 4.2f, "https://archive.org/download/SampleVideo1280x7205mb/SampleVideo_1280x720_5mb.mp4")
        )
        MoviesType.COMING_SOON -> listOf(
                MovieItem(5, "F2", "1H 20M", "Ad", 4.2f, "https://archive.org/download/FinalFantasy2_356/FinalFantasy2_356_part02_512kb.mp4")
        )
    }
}
