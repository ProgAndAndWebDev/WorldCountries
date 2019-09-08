package com.example.worldcountries.ui.quiz


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.example.worldcountries.R
import com.example.worldcountries.app.WCApplication
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_quiz.*
import javax.inject.Inject

private const val TYPE = "TYPE"
private const val ORDER = "ORDER"

class QuizFragment : Fragment(), QuizPresentationContract.View {

    @Inject
    lateinit var presenter: QuizPresentationContract.Presenter

    private var quizComponent: QuizComponent? = null
    private var type: String? = null
    private var order: Int = 1
    private var correctAnswer: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSubcomponent()?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE)
            order = it.getInt(ORDER)
        }

        if (type != null) {
            presenter.onScreenStarted(type!!, order)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyed()
    }

    override fun setNext(
        name: String,
        correctAnswer: String,
        options: List<String>
    ) {
        this.correctAnswer = correctAnswer
        nextButton.setOnClickListener(null)
        setButtonDisabled(nextButton)
        guessItemTextView.text = name
        optionOneButton.text = options[0]
        optionTwoButton.text = options[1]
        optionThreeButton.text = options[2]
        optionFourButton.text = options[3]
        setUpListeners()
    }

    override fun setCompleted(correctAnswerPercentage: Int) {
        Toast.makeText(context,correctAnswerPercentage.toString(),Toast.LENGTH_LONG).show()
    }

    private fun setUpListeners() {
        optionOneButton.setOnClickListener {
            checkAnswer(optionOneButton)
        }
        optionTwoButton.setOnClickListener {
            checkAnswer(optionTwoButton)
        }
        optionThreeButton.setOnClickListener {
            checkAnswer(optionThreeButton)
        }
        optionFourButton.setOnClickListener {
            checkAnswer(optionFourButton)
        }
    }

    private fun checkAnswer(button: MaterialButton) {
        tearDownClickListeners()
        if (button.text == correctAnswer) {
            presenter.updateScore(true)
            setCorrect(button)
        } else {
            presenter.updateScore(false)
            setIncorrect(button)
            showCorrectAnswer()
        }

        setButtonEnabled(nextButton)
        nextButton.setOnClickListener {
            restoreButton(optionOneButton)
            restoreButton(optionTwoButton)
            restoreButton(optionThreeButton)
            restoreButton(optionFourButton)
            presenter.getNext()
        }
    }

    private fun showCorrectAnswer() {
        when (correctAnswer) {
            optionOneButton.text -> setCorrect(optionOneButton)
            optionTwoButton.text -> setCorrect(optionTwoButton)
            optionThreeButton.text -> setCorrect(optionThreeButton)
            optionFourButton.text -> setCorrect(optionFourButton)
        }
    }

    private fun tearDownClickListeners() {
        optionOneButton.setOnClickListener(null)
        optionTwoButton.setOnClickListener(null)
        optionThreeButton.setOnClickListener(null)
        optionFourButton.setOnClickListener(null)
    }

    private fun createSubcomponent(): QuizComponent? {
        quizComponent = (activity?.application as WCApplication)
            .component
            .createSubComponent(QuizModule(this))
        return quizComponent
    }

    private fun setCorrect(button: MaterialButton) {
        button.setOnClickListener(null)
        button.backgroundTintList = ContextCompat.getColorStateList(
            context!!,
            R.color.green
        )
        button.strokeColor = ContextCompat.getColorStateList(
            context!!,
            R.color.green
        )

        button.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.white
            )
        )
    }

    private fun setIncorrect(button: MaterialButton) {
        button.setOnClickListener(null)
        button.backgroundTintList = ContextCompat.getColorStateList(
            context!!,
            R.color.red
        )
        button.strokeColor = ContextCompat.getColorStateList(
            context!!,
            R.color.red
        )

        button.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.white
            )
        )
    }

    fun setButtonDisabled(button: MaterialButton) {
        button.backgroundTintList = ContextCompat.getColorStateList(
            context!!,
            R.color.light_gray
        )
        button.rippleColor = ContextCompat.getColorStateList(
            context!!,
            R.color.transparent
        )
    }

    fun setButtonEnabled(button: MaterialButton) {
        button.backgroundTintList = ContextCompat.getColorStateList(
            context!!,
            R.color.white
        )

        button.rippleColor = ContextCompat.getColorStateList(
            context!!,
            R.color.holo_blue_light
        )
    }

    private fun restoreButton(button: MaterialButton) {
        button.backgroundTintList = ContextCompat.getColorStateList(
            context!!,
            R.color.white
        )
        button.strokeColor = ContextCompat.getColorStateList(
            context!!,
            R.color.colorPrimary
        )

        button.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.colorPrimary
            )
        )
    }


}