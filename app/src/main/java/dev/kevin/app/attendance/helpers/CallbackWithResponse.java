package dev.kevin.app.attendance.helpers;

import org.json.JSONObject;

public interface CallbackWithResponse {

    void execute(JSONObject response);
}
