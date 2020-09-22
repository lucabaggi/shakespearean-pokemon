
package it.lucabaggi.shakespeareanpokemon.client.funtranslator.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "translated",
    "text",
    "translation"
})
public class Contents {

    @JsonProperty("translated")
    private String translated;
    @JsonProperty("text")
    private String text;
    @JsonProperty("translation")
    private String translation;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("translated")
    public String getTranslated() {
        return translated;
    }

    @JsonProperty("translated")
    public void setTranslated(String translated) {
        this.translated = translated;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("translation")
    public String getTranslation() {
        return translation;
    }

    @JsonProperty("translation")
    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
