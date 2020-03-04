package com.example.morzartquiz


import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.morzartquiz.databinding.FragmentGameBinding
import kotlinx.android.synthetic.main.fragment_game.*

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    data class Quartet(
        val id: Int,
        val name: String?
    )

    //Quartet source id and name
    private val quartetSources: MutableMap<Int, Int> = mutableMapOf(
        1 to R.raw.quartet_1,
        2 to R.raw.quartet_2,
        3 to R.raw.quartet_3,
        14 to R.raw.quartet_14,
        15 to R.raw.quartet_15,
        16 to R.raw.quartet_16,
        17 to R.raw.quartet_17,
        18 to R.raw.quartet_18,
        19 to R.raw.quartet_19,
        23 to R.raw.quartet_23
    )

    private val quartetNames: MutableMap<Int, String> = mutableMapOf(
        1 to "String Quartet No.1 in G major, K.80/73f",
        2 to "String Quartet No.2 in D major, K.155/134a",
        3 to "String Quartet No.3 in G major, K.156/134b",
        14 to "String Quartet No.14 in G major, K.387 (\"Spring\")",
        15 to "String Quartet No.15 in D minor, K.421/417b",
        16 to "String Quartet No.16 in E-flat major, K.428/421b",
        17 to "String Quartet No.17 in B-flat major, K.458 (\"Hunt\")",
        18 to "String Quartet No.18 in A major, K.464",
        19 to "String Quartet No.19 in C major, K.465 (\"Dissonant\")",
        23 to "String Quartet No.23 in F major, K.590 (\"Prussian No.3\")"
    )

    private val quartetList: MutableList<Int> = mutableListOf(
        1, 2, 3, 14, 15, 16, 17, 18, 19, 23
        )

    private var quartetIndex = 0
    lateinit var answers: MutableList<String?>
    lateinit var currentQuartet: Quartet
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

        //カルテットリストのシャッフルと最初の問題の設定、UIへの反映
        randomizeQuartets()
        radioButton1.text = answers[0]
        radioButton2.text = answers[1]
        radioButton3.text = answers[2]
        radioButton4.text = answers[3]

        //SUBMITボタンに　onClicklistenerを実装
        binding.submitButton.setOnClickListener { view: View ->
            val checkedId = binding.radioGroup.checkedRadioButtonId
            //DO nothing if nothing is checked (id == -1)
            var answerText: String? = ""
            when (checkedId) {
                R.id.radioButton1 -> answerText = answers[0]
                R.id.radioButton2 -> answerText = answers[1]
                R.id.radioButton3 -> answerText = answers[2]
                R.id.radioButton4 -> answerText = answers[3]
            }

            if (answerText == currentQuartet.name) {
                view.findNavController().navigate(R.id.action_gameFragment_to_gameWonFragment)
            } else {
                view.findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
            }

        }
        //MediaPlayerインスタンスの初期化
        if (quartetSources[currentQuartet.id] != null) {
            player = MediaPlayer.create(this.context, quartetSources[currentQuartet.id]!!)
            val timer = IntroCountDownTimer(3000, 100)
            //再生ボタンとメディアプレイヤーの紐付け
            binding.startButton.setOnClickListener {
                player.start()
                timer.start()
            }
        }

        return binding.root
    }

    //media Player　再生の停止
    private fun playStop() {
        player.stop()
    }

    private fun randomizeQuartets() {
        quartetList.shuffle()
        quartetIndex = 0
        setQuartet()
    }

    private fun setQuartet() {
        val currentQuartetId = quartetList[quartetIndex] //正解のquartetの番号(Int)
        currentQuartet = Quartet(currentQuartetId, quartetNames[currentQuartetId])
        val incorrectAnswers: List<Int> = quartetList.filterNot { it == currentQuartetId}
        answers = mutableListOf<String?>(
            quartetNames[currentQuartetId],
            quartetNames[incorrectAnswers[0]],
            quartetNames[incorrectAnswers[1]],
            quartetNames[incorrectAnswers[2]]
        )
        answers.shuffle()
    }

}
