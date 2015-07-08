package com.andela.uberhack.models;

import com.google.gson.annotations.SerializedName;
import com.andela.uberhack.Vars;

/**
 * Created by goodson on 5/14/15.
 */
public class User {

    @SerializedName(Vars.KEY_ID)
    public int id;

    @SerializedName(Vars.KEY_PLT)
    public int platform;

    @SerializedName(Vars.KEY_UID)
    public String user_id;

    @SerializedName(Vars.KEY_CARD_ID)
    public String card_id;


    @SerializedName(Vars.KEY_NAME)
    public String name;

    @SerializedName(Vars.KEY_USN)
    public String username;


    @SerializedName(Vars.KEY_EMAIL)
    public String email;

    @SerializedName(Vars.KEY_IMG)
    public String picture;

    @SerializedName(Vars.KEY_TOKEN)
    public String token;

    @SerializedName(Vars.KEY_SECRET)
    public String secret;

    @SerializedName(Vars.KEY_PHONE)
    public String phone;

    @SerializedName(Vars.KEY_ENABLED)
    public boolean enabled;

    @SerializedName(Vars.KEY_CARD_DURATION)
    public int card_duration;

    @SerializedName(Vars.KEY_AIRTIME)
    public int airtime;

    @SerializedName(Vars.KEY_CARD_TYPE)
    public int card_type;

    @SerializedName(Vars.KEY_CREATED_AT)
    public int created_at;

    @SerializedName(Vars.KEY_MODIFIED_AT)
    public int modified_at;

}
