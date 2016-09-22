package com.tcl.entity;

/**
 * Created by tengxue on 16-9-8.
 */
public class CountryEntity {
    private String countryCode;
    private String languageCode;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return "CountryEntity{" + "countryCode='" + countryCode + '\'' + ", languageCode='"
                + languageCode + '\'' + '}';
    }
}
