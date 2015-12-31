package com.android.ted.inputer.model;

import java.io.Serializable;

/**
 * Created by Ted on 2015/12/28.
 *
 * @ com.android.ted.inputer.model
 */
public class Tag implements Serializable {
    private String tag;
    private String tagValue;
    private int tagNameRes;
    private int tagBgRes;


    public Tag(String tag, int tagNameRes, int tagBgRes, String tagValue) {
        this.tag = tag;
        this.tagValue = tagValue;
        this.tagNameRes = tagNameRes;
        this.tagBgRes = tagBgRes;
    }

    public String getTag() {
        return tag;
    }

    public String getTagValue() {
        return tagValue;
    }

    public int getTagNameRes() {
        return tagNameRes;
    }

    public int getTagBgRes() {
        return tagBgRes;
    }
}

