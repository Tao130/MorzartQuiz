package com.example.morzartquiz


import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
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

    private lateinit var player: MediaPlayer
    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_game, container, false)

        player = MediaPlayer.create(this.context, Uri.parse("android.resource://getPackageName()/raw/sonata_no25"))

        //再生ボタンとメディアプレイヤーの紐付け
        binding.startButton.setOnClickListener {
            player.start()
        }
        return binding.root
        }



}
