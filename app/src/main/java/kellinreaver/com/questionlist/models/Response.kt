package kellinreaver.com.questionlist.models

data class Response(
        val version:String,
        val questions: List<Question>
)