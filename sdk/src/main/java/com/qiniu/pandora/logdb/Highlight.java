package com.qiniu.pandora.logdb;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Highlight{


    private ArrayList<String> pre_tags;
    private ArrayList<String> post_tags;
    private HashMap<String,HashMap<String,String>> fields;
    private Boolean require_field_match;
    private int fragment_size;
    public Highlight() {
    }
    public Highlight(ArrayList<String> pre_tags,ArrayList<String> post_tags,HashMap<String,HashMap<String,String>> fields,Boolean require_field_match, int fragment_size) {
        this.pre_tags = pre_tags;
        this.post_tags = post_tags;
        this.fields = fields;
        this.require_field_match = require_field_match;
        this.fragment_size = fragment_size;
    }
}