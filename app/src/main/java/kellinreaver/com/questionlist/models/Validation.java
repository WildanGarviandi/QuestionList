
package kellinreaver.com.questionlist.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Validation {

    @SerializedName("required")
    private Boolean Required;
    @SerializedName("minLength")
    private Integer MinLength;
    @SerializedName("exactLength")
    private Integer ExactLength;

    public Boolean getRequired() {
        return Required;
    }

    public void setRequired(Boolean required) {
        Required = required;
    }

    public Integer getMinLength() {
        return MinLength;
    }

    public void setMinLength(int minLength) {
        MinLength = minLength;
    }

    public Integer getExactLength() {
        return ExactLength;
    }

    public void setExactLength(int exactLength) {
        ExactLength = exactLength;
    }
}
