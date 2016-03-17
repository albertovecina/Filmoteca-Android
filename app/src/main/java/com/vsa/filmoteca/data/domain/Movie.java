package com.vsa.filmoteca.data.domain;

import com.vsa.paperknife.CellElement;

import java.io.Serializable;

/**
 * Created by seldon on 26/03/15.
 */
public class Movie implements CellElement, Serializable {

    private String mTitle;
    private String mSubtitle;
    private String mDate;
    private String mUrl;

    public Movie() {
        mTitle = "";
        mSubtitle = "";
        mDate = "";
        mUrl = "";
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

}