package http.program.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Rate_Limit {
    private Integer coreLimit;
    private Integer searchLimit;

    public Rate_Limit(Integer coreLimit, Integer searchLimit){
        this.coreLimit = coreLimit;
        this.searchLimit = searchLimit;
    }

    public Integer getCoreLimit() {
        return coreLimit;
    }

    public Integer getSearchLimit() {
        return searchLimit;
    }
}
