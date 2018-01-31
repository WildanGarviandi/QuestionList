package kellinreaver.com.questionlist.services

import com.google.gson.Gson
import io.reactivex.Single
import kellinreaver.com.questionlist.models.Question
import kellinreaver.com.questionlist.models.Response
import okhttp3.OkHttpClient
import okhttp3.Request

class QuestionFetcher(private val client: OkHttpClient) {
    private val gson = Gson()

    companion object {
        const val endpoint = "https://raw.githubusercontent.com/amarthaid/test-mobile/master/Problem-1/question.json"
    }

    fun fetchQuestion(): Single<List<Question>> {
        val call = client.newCall(Request.Builder().url(endpoint).build())
        return Single.fromCallable { call.execute() }
                .flatMap { res ->
                    if (res.isSuccessful) {
                        val bodyString = res.body()!!.string()
                        Single.just(gson.fromJson(bodyString, Response::class.java).questions)
                    } else {
                        Single.error(IllegalStateException())
                    }
                }
    }
}