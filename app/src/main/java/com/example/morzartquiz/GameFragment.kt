package com.example.morzartquiz


import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.morzartquiz.databinding.FragmentGameBinding
import kotlinx.android.synthetic.main.fragment_game.*

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    class CorrectAnswer(
        val id: Int,
        val name: String?
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

    private var quartetList: MutableList<Int> = mutableListOf(
        1, 2, 3, 14, 15, 16, 17, 18, 19, 23
    )

    //タイマーでメディアプレイヤーの再生時間を設定
    inner class IntroCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            playStop()
        }
    }

    private lateinit var player: MediaPlayer
    private lateinit var binding: FragmentGameBinding
    lateinit var answersNames: MutableList<String?>
    private lateinit var answersIndexes: MutableList<Int>
    private var questionIndex = 0
    private var playQuartet: Int? = 0
    private lateinit var correctAnswer: CorrectAnswer
    private val howManyQuiz = 3

    //再生時間の設定
    private val timer = IntroCountDownTimer(3000, 100)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_game, container, false)
        // Bind this fragment class to the layout
        binding.game = this
        //カルテットリストのシャッフルと最初の問題の設定
        randomizeQuartets()
        //正解のカルテット音源をセット
        createMediaPlayer()
        //答えをランダムに並び替え
        answersNames.shuffle()


        //再生ボタンとメディアプレイヤーの紐付け
        binding.startButton.setOnClickListener {
            start_button.visibility = View.INVISIBLE
            player.start()
            timer.start()
        }

        //提出ボタン
        binding.submitButton.setOnClickListener { view: View ->
            val checkedId = binding.radioGroup.checkedRadioButtonId
            if (-1 != checkedId) {
                var selectedAnswerName = ""
                when (checkedId) {
                    R.id.radioButton1 -> selectedAnswerName = radioButton1.text.toString()
                    R.id.radioButton2 -> selectedAnswerName = radioButton2.text.toString()
                    R.id.radioButton3 -> selectedAnswerName = radioButton3.text.toString()
                    R.id.radioButton4 -> selectedAnswerName = radioButton4.text.toString()
                }

                if (selectedAnswerName == correctAnswer.name) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < howManyQuiz) {
                        val text = "正解！ " + correctAnswer.name + "でした"
                        Toast.makeText(context, text, LENGTH_SHORT).show()
                        randomizeQuartets()
                        createMediaPlayer()
                        start_button.visibility = View.VISIBLE
                        binding.radioGroup.clearCheck()
                        answersNames.shuffle()
                        binding.invalidateAll()
                    } else {
                        // won! navigate to the gameWonFragment.
                        view.findNavController()
                            .navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(howManyQuiz, questionIndex ))
                    }
                } else {
                        view.findNavController()
                            .navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment(correctAnswer.name!!))
                }

            }
        }

        return binding.root
    }

    //media Player　再生の停止
    private fun playStop() {
        player.stop()
    }

    private fun createMediaPlayer() {
        playQuartet = quartetSources[correctAnswer.id] //再生するカルテットの番号を取得
        player = MediaPlayer.create(this.context, playQuartet!!)
    }

    //カルテットリストをシャッフルし、頭から4つを選ぶ。1つ目を正解とする。
    private fun randomizeQuartets() {
        quartetList.shuffle()
        //選ばれた4つのカルテット番号のリスト
        answersIndexes = quartetList.slice(0..3).toMutableList()
        //選ばれた4つのカルテットの名前のリスト
        answersNames = mutableListOf(
            quartetNames[answersIndexes[0]], quartetNames[answersIndexes[1]],
            quartetNames[answersIndexes[2]], quartetNames[answersIndexes[3]]
        )
        correctAnswer = CorrectAnswer(answersIndexes[0], answersNames[0])
        //ラベルの変更
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.quiz) + " " + "(${questionIndex + 1} / $howManyQuiz)"
    }

}
