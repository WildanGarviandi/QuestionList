
package kellinreaver.com.questionlist.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Question {

    @SerializedName("key")
    private String Key;
    @SerializedName("label")
    private String Label;
    @SerializedName("questions")
    private List<Question> Questions;
    @SerializedName("typeField")
    private String TypeField;
    @SerializedName("validation")
    private Validation Validation;
    @SerializedName("version")
    private String Version;
    @SerializedName("options")
    private List<Options> Options;


    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public List<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(List<Question> questions) {
        Questions = questions;
    }

    public String getTypeField() {
        return TypeField;
    }

    public void setTypeField(String typeField) {
        TypeField = typeField;
    }

    public kellinreaver.com.questionlist.models.Validation getValidation() {
        return Validation;
    }

    public void setValidation(kellinreaver.com.questionlist.models.Validation validation) {
        Validation = validation;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public List<kellinreaver.com.questionlist.models.Options> getOptions() {
        return Options;
    }

    public void setOptions(List<kellinreaver.com.questionlist.models.Options> options) {
        Options = options;
    }
}
