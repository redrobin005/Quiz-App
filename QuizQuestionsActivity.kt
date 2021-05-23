package com.example.quizapp

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

// notice that the class implements the interface OnClickListener to enable activity on tap
class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    // global variables to get questions
    private var mCurrentPosition = 1
    private var mQuestionList : ArrayList<Question>? = null
    private var mSelectedOptionPosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mQuestionList = Constants.getQuestions()

        setQuestion()

        // in the mainActivity we can see the alternative way of doing this is to say btn.setOnClickListener{...}
        // the below is a more effective OOP way in which we implement an OnClickListener interface
        // and then use its function to dictate what happens when we tap on a certain view
        // i.e. when tapping one of the views below, we pass a this reference of the class
        // which then passes the textView to the OCL function
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

    }

    private fun setQuestion(){
        // ? is the safecall operator which means that the value can return null
        val question = mQuestionList!![mCurrentPosition - 1]

        //ensures options are back to default appearance
        defaultOptionsView()

        // after tapping next question, we want the button to change back to submit on the next
        // question page, unless this is the final question (which we need as finish)

        if (mCurrentPosition != mQuestionList!!.size) {
            btn_submit.text = "SUBMIT"
        }else{
            btn_submit.text = "FINISH"
        }

        // this gets the current position of the progress bar and also displays this as the text
        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max

        // assign the text of the question textView to the question variable above which get the
        // current question in the arraylist
        tv_question.text = question!!.question

        // assign the image view by getting the image int from the question.kt class
        iv_image.setImageResource(question.image)

        // now set the textViews of the all the options to be the current question options
        // from the arraylist
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    // function for setting the options to be the default color
    // needed as they will switch between default and selected depending on where the user taps
    private fun defaultOptionsView(){
        val options = java.util.ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.default_option_border_bg)

        }
    }

    // a 'this' reference to a setOnClickListener will pass the view which called it to this function
    // notice how the View has to be set to nullable (?)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> {
                selectedOptionView(tv_option_one, 1)
            }
            R.id.tv_option_two -> {
                selectedOptionView(tv_option_two, 2)
            }
            R.id.tv_option_three -> {
                selectedOptionView(tv_option_three, 3)
            }
            R.id.tv_option_four -> {
                selectedOptionView(tv_option_four, 4)
            }
            R.id.btn_submit -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    // so if we have not selected an option, go to the next question (skip question)
                    // and when the next position is more than the number of questions display toast
                    // to indicate that the quiz is finished
                    when {
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // else if a question has been selected then we extract the info from it
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    // we change the selected appearance to red if incorrect option selected
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.incorrect_option_border_bg)
                    }
                    // we display the correct answer as green no matter if the correct or incorrect
                    // answer has been selected
                    answerView(question!!.correctAnswer, R.drawable.correct_option_border_bg)

                    // when at the last question, the submit button will change to finish, else next
                    if (mCurrentPosition == mQuestionList!!.size) {
                        btn_submit.text = "FINISH"
                    }else{
                        btn_submit.text = "NEXT QUESTION"
                    }
                    // this will say to move onto the next question after tapping submit/finish
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    // this function will set the correct and incorrect answer to the appropriate colour
    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
           1 ->  tv_option_one.background = ContextCompat.getDrawable(
                    this, drawableView
            )
           2 ->  tv_option_two.background = ContextCompat.getDrawable(
                    this, drawableView
            )
           3 ->  tv_option_three.background = ContextCompat.getDrawable(
                    this, drawableView
            )
           4 ->  tv_option_four.background = ContextCompat.getDrawable(
                    this, drawableView
            )
        }
    }

    // when called (option selected), this first sets all the options to have the default layout
    // and then it sets the textView passed to it to have the 'selected' appearance
    // the global variable mSelectedOptionPosition also gets set to the optionNum passed into the function as well
    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD )
        tv.background = ContextCompat.getDrawable(
                this,
                R.drawable.select_option_border_bg)
    }
}