package me.li2.movies.ui.moviedetail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.exoplayer2.ui.PlayerView
import com.jakewharton.rxbinding3.view.clicks
import im.ene.toro.exoplayer.Playable
import im.ene.toro.media.PlaybackInfo
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.FragmentMovieDetailBinding
import me.li2.movies.util.*
import me.li2.movies.util.video.VideoPlayerAware

class MovieDetailFragment : BaseFragment(), VideoPlayerAware {

    private lateinit var binding: FragmentMovieDetailBinding
    private val args by navArgs<MovieDetailFragmentArgs>()

    override val videoUri: Uri?
        get() = args.movieItem?.trailerUri
    override val videoPlayerView: PlayerView
        get() = video_player
    override val initialPlaybackInfo: PlaybackInfo = PlaybackInfo()
    override var videoPlayerHelper: Playable? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(this)

        ifSupportLollipop {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
            ViewCompat.setTransitionName(video_player, getString(R.string.transition_name_movie) + args.movieItem?.id)
        }

        if (isTablet()) {
            toolbar.hide()
            binding.isTablet = true
        } else {
            activity?.setToolbar(toolbar)
            activity?.hideStatusBar()
            binding.isTablet = false
        }

        binding.movieItem = args.movieItem

        compositeDisposable += btn_rate_movie.clicks().throttleFirstShort().subscribe {
            // todo
            toast("todo: rate movie clicks")
        }
    }

    override fun onDestroyView() {
        activity?.showStatusBar()
        super.onDestroyView()
    }

    override fun onVideoLoading(isLoading: Boolean) {
        binding.buffering = isLoading
    }
}
