package com.yuviii.restdata;

import org.json.JSONObject;

/**
 * Created by yubraj on 2/11/17.
 */

// Sample class
public class Profile {
    public String name, address, profession;

    public Profile(JSONObject jsonObject) {
        this.name = jsonObject.optString("name");
        this.address = jsonObject.optString("address");
        this.profession = jsonObject.optString("profession");
    }
    public Profile(){
        
    }
}
