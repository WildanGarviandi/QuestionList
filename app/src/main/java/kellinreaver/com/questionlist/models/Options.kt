package kellinreaver.com.questionlist.models

import com.google.gson.annotations.SerializedName

class Options {

    @SerializedName("label")
    var label: String? = null
    @SerializedName("value")
    private var Value: String? = null

    val value: Boolean
        get() = Value == "1"

    fun setValue(value: String) {
        Value = value
    }

}
