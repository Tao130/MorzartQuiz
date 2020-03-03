package com.example.morzartquiz


import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.morzartquiz.databinding.FragmentGameBinding

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    inner class IntroCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            playStop()
        }
    }
    private lateinit var player: MediaPlayer
    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_game, container, false)

        player = MediaPlayer.create(this.context, R.raw.sonata_no25)
        val timer = IntroCountDownTimer(3000, 100)
        //再生ボタンとメディアプレイヤーの紐付け
        binding.startButton.setOnClickListener {
            player.start()
            timer.start()
        }
        return binding.root
        }

    private fun playStop() {
        player.stop()
    }

}
