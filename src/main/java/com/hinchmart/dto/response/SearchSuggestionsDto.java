package com.hinchmart.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchSuggestionsDto {

    private List<String> suggestions = new ArrayList<>();
    private List<Map<String, Object>> matchingCategories = new ArrayList<>();

    public SearchSuggestionsDto() {
    }

    public SearchSuggestionsDto(List<String> suggestions, List<Map<String, Object>> matchingCategories) {
        this.suggestions = suggestions;
        this.matchingCategories = matchingCategories;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public List<Map<String, Object>> getMatchingCategories() {
        return matchingCategories;
    }

    public void setMatchingCategories(List<Map<String, Object>> matchingCategories) {
        this.matchingCategories = matchingCategories;
    }
}
