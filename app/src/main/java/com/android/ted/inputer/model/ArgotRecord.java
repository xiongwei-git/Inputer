/*
 *    Copyright 2016 Ted xiong-wei@hotmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.android.ted.inputer.model;

import java.io.Serializable;

/**
 * Created by Ted on 2015/12/14.
 * ArgotRecord 暗语对象，用作数据存储
 */
public class ArgotRecord implements Serializable {
    private long _id;
    private String shortcut;
    private String phrase;
    private String description;
    private int usage_count;
    private String labels;
    private long timestamp;
    private boolean expands_immediately;
    private boolean expands_within_word;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUsage_count() {
        return usage_count;
    }

    public void setUsage_count(int usage_count) {
        this.usage_count = usage_count;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isExpands_immediately() {
        return expands_immediately;
    }

    public void setExpands_immediately(boolean expands_immediately) {
        this.expands_immediately = expands_immediately;
    }

    public boolean isExpands_within_word() {
        return expands_within_word;
    }

    public void setExpands_within_word(boolean expands_within_word) {
        this.expands_within_word = expands_within_word;
    }
}