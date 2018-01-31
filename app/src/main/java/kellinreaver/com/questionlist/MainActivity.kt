package kellinreaver.com.questionlist

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kellinreaver.com.questionlist.models.Question
import kellinreaver.com.questionlist.models.Result
import kellinreaver.com.questionlist.models.Validation
import kellinreaver.com.questionlist.services.QuestionFetcher
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val fetcherQuestion = QuestionFetcher(OkHttpClient())
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetcherQuestion.fetchQuestion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result, error ->
                    if (error != null) {
                        AlertDialog.Builder(this)
                                .setTitle(getString(R.string.error_text))
                                .setMessage(getString(R.string.error_message))
                                .setCancelable(false)
                                .show()
                    }
                    renderQuestion(result)
                }

    }

    /**
     *
     * This method is to populate UI element from given question
     *
     * @param questions the type of a member in this group.
     * @property renderQuestion the name of this group.
     */
    private fun renderQuestion(questions: List<Question>) {
        val buttonSubmit = AppCompatButton(this@MainActivity)
        buttonSubmit.setText(R.string.submit)
        buttonSubmit.setTextColor(ContextCompat.getColor(this, R.color.white))
        buttonSubmit.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        buttonSubmit.setOnClickListener({

            val results: MutableList<Pair<Boolean, Result>> = mutableListOf()

            for (i in 0..(main_container.childCount - 2)) {
                val child = main_container.getChildAt(i)
                val question = child.tag as Question

                val result = if (question.typeField != "select") {
                    val editText = child as EditText
                    val isValid = validate(question.validation, editText)
                    if (!isValid) {
                        editText.error = generateErrorText(question.validation)
                    }
                    Pair(isValid, Result(question.label, editText.text.toString()))
                } else {
                    val radioGroup = child as RadioGroup
                    var label = ""

                    (0..radioGroup.childCount)
                            .map { radioGroup.getChildAt(it) }
                            .filterIsInstance<RadioButton>()
                            .forEach {
                                if (it.isChecked) {
                                    label = it.text.toString()
                                }
                            }

                    Pair(true, Result(question.label, label))
                }

                results.add(result)
            }

            if (results.all { it.first }) {
                DetailActivity.getStartIntent(this@MainActivity, results.map { it.second })
                // to finish is optional but just to make it more clean i'll make if finish
                finish()
            }
        })

        questions.forEach { q ->
            when (q.typeField) {
                "select" -> {
                    val view = layoutInflater.inflate(R.layout.radio, main_container, false) as RadioGroup
                    view.tag = q
                    q.options.forEach { opt ->
                        val radioButton = RadioButton(this)
                        radioButton.text = opt.label
                        radioButton.isChecked = opt.value
                        radioButton.id = View.generateViewId()
                        view.addView(radioButton)
                    }
                    main_container.addView(view)
                }
                else -> generateTextField(q)
            }
        }

        main_container.addView(buttonSubmit)
    }

    private fun generateTextField(question: Question) {
        val view: EditText = layoutInflater.inflate(R.layout.text, main_container, false) as EditText
        view.tag = question
        when (question.typeField) {
            "email" -> {
                view.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                view.hint = question.label
            }
            "password" -> {
                view.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                view.transformationMethod = PasswordTransformationMethod.getInstance()
                view.hint = question.label
            }
            "text" -> {
                view.inputType = InputType.TYPE_CLASS_TEXT
                view.hint = question.label
            }
            "date" -> {
                view.inputType = InputType.TYPE_CLASS_TEXT
                view.isFocusable = false
                view.isCursorVisible = false
                view.keyListener = null
                view.hint = question.label

                val datePicker = DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateField(view)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

                view.setOnClickListener {
                    datePicker.show()
                }
            }
        }

        main_container.addView(view)
    }

    // Utils method
    private fun updateField(editText: EditText) {
        val myFormat = "dd/MM/YYYY"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        editText.setText(simpleDateFormat.format(calendar.time))
    }

    private fun generateErrorText(validation: Validation): String {
        val stringBuilder = StringBuilder()
        if (validation.required) {
            stringBuilder.append(getString(R.string.error_message_field_required))
        }
        if (validation.minLength != null) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(" and ")
            }
            stringBuilder.append("min length is ${validation.minLength}")
        }
        if (validation.exactLength != null) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(" and ")
            }
            stringBuilder.append("exact length must be ${validation.exactLength}")
        }

        return stringBuilder.toString()
    }

    // Validation Method
    private fun validate(validation: Validation, view: EditText): Boolean = validateRequired(validation, view) && validateMinLength(validation, view) && validateExactLength(validation, view)

    private fun validateRequired(validation: Validation, view: EditText): Boolean = validation.required.not() || (validation.required && view.text.isNotBlank())

    private fun validateMinLength(validation: Validation, view: EditText): Boolean = validation.minLength == null
            || (validation.minLength != null && view.text.trim().length > validation.minLength)


    private fun validateExactLength(validation: Validation, view: EditText): Boolean = validation.exactLength == null
            || (validation.exactLength != null && view.text.trim().length == validation.exactLength)
}
