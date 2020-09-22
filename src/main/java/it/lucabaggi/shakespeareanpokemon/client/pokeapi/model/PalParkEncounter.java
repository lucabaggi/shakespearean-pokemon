
package it.lucabaggi.shakespeareanpokemon.client.pokeapi.model;

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
    "area",
    "base_score",
    "rate"
})
public class PalParkEncounter {

    @JsonProperty("area")
    private Area area;
    @JsonProperty("base_score")
    private Integer baseScore;
    @JsonProperty("rate")
    private Integer rate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("area")
    public Area getArea() {
        return area;
    }

    @JsonProperty("area")
    public void setArea(Area area) {
        this.area = area;
    }

    @JsonProperty("base_score")
    public Integer getBaseScore() {
        return baseScore;
    }

    @JsonProperty("base_score")
    public void setBaseScore(Integer baseScore) {
        this.baseScore = baseScore;
    }

    @JsonProperty("rate")
    public Integer getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(Integer rate) {
        this.rate = rate;
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
