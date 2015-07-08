package com.andela.uberhack.models;

import com.google.gson.annotations.SerializedName;
import com.andela.uberhack.Vars;

/**
 * Created by goodson on 5/14/15.
 */
public class User {

    @SerializedName("message")
    public String message;

    @SerializedName("response")
    public UserResponse response;
}
