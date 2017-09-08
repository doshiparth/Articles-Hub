package com.example.parthdoshi.articleshub;

/**
 * Created by Parth on 30-07-2017.
 */

public class SelectPageModel {
    private String TagName;
    private int TagValue;

    public SelectPageModel() {
    }

    public SelectPageModel(String TagName, int TagValue){
        this.TagName = TagName;
        this.TagValue = TagValue;
    }

    public String getTagName(){
        return this.TagName;
    }

    public int getTagValue(){
        return this.TagValue;
    }

    public void setTagValue(int tagValue) {
        TagValue = tagValue;
    }

    public void setTagName(String tagName) {
        TagName = tagName;
    }
}
