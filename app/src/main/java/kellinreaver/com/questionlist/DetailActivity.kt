package kellinreaver.com.questionlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.view.ViewGroup
import kellinreaver.com.questionlist.models.Result
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val results = intent.getParcelableArrayListExtra<Result>(RESULT_OF_KEY_VALUE)
        results.forEach { result ->
            val view: ViewGroup = layoutInflater.inflate(R.layout.detail_container, result_container, false) as ViewGroup
            for (i in 0..view.childCount) {
                val element = view.getChildAt(i)

                if (element is AppCompatTextView) {
                    if (element.id == R.id.tv_key) {
                        element.text = result.key
                    } else {
                        element.text = result.value
                    }
                }
            }

            result_container.addView(view)
        }
    }

    companion object {
        private const val RESULT_OF_KEY_VALUE = "Results"

        fun getStartIntent(context: Context, results: List<Result>): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putParcelableArrayListExtra(RESULT_OF_KEY_VALUE, results as ArrayList<out Parcelable>)
            context.startActivity(intent)
            return intent
        }
    }
}
