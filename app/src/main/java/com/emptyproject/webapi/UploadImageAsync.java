package com.emptyproject.webapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import com.datecoy.R;
import com.datecoy.listioner.CommonListioner;
import com.datecoy.util.Const;
import com.datecoy.util.PrefsManager;
import com.datecoy.util.Util;

import org.json.JSONObject;

/**
 * Created by INFYZO\shailesh.pateliya on 22/10/16.
 */

public class UploadImageAsync extends AsyncTask<String, String, String> implements WebUtil {
    JSONObject urlParams;
    JSONObject obj;
    int responseCode = 0;
    String response;
    Activity context;
    String[] filename, filePAth;
    ProgressDialog PD;
    CommonListioner commonListioner;
    boolean isFacebook = false;

    public UploadImageAsync(Activity context, JSONObject urlParams, String[] filePath, String[] filename, CommonListioner commonListioner) {
        try {
            if (PrefsManager.getUid().length() > 0 && !PrefsManager.getUid().equalsIgnoreCase("0")) {
                urlParams.put(KEY_UID, PrefsManager.getUid());
            }
            urlParams.put(KEY_UUID, Util.getInstance(context).getDeviceId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.urlParams = urlParams;
        this.context = context;
        this.filename = filename;
        this.filePAth = filePath;
        this.commonListioner = commonListioner;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Util.getInstance(context).showLog(urlParams.toString());
        if (filename[0].equals("chatmedia")) {
        } else {
            if (PD == null || !PD.isShowing()) {
                PD = new ProgressDialog(context);
                PD.setCancelable(false);
                PD.show();
                PD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                PD.setContentView(R.layout.progressdialog);
            }
        }

    }

    @Override
    protected String doInBackground(String... params) {
        //"profilePics"
        Util.getInstance(context).showLog("filename[0]..." + filename[0]);
        if (filename[0].equals(KEY_PROFILEPICS)) {
            response = GetWebservice.multipartRequest2(Const.USER_PROFILE_PROFILE_MEDIA_ACTION, urlParams.toString(), filePAth, filename, "image/*");
        } else if (filename[0].equals("image")) {
            response = GetWebservice.multipartRequest2(Const.USER_PROFILE_PROFILE_MEDIA_ACTION, urlParams.toString(), filePAth, new String[]{KEY_PROFILEMEDIA}, "image/*");
        } else if (filename[0].equals("audio")) {
            response = GetWebservice.multipartRequest2(Const.USER_PROFILE_PROFILE_MEDIA_ACTION, urlParams.toString(), filePAth, new String[]{KEY_PROFILEMEDIA}, "audio/mp3");
        } else if (filename[0].equals("video")) {
            response = GetWebservice.multipartRequest2(Const.USER_PROFILE_PROFILE_MEDIA_ACTION, urlParams.toString(), filePAth, new String[]{KEY_PROFILEMEDIA}, "video/*");
        }
        /*else if (filename[0].equals(KEY_PASSPORT)) {
            response = GetWebservice.multipartRequest2(Const.USER_SAVE, urlParams.toString(), filePAth, filename, "image*//*");
        } else if (filename[0].equals(KEY_KUNDLI)) {
            response = GetWebservice.multipartRequest2(Const.USER_SAVE, urlParams.toString(), filePAth, filename, "image*//*");
        } else if (filename[0].equals(KEY_UPLOADCV)) {
            response = GetWebservice.multipartRequest2(Const.USER_SAVE, urlParams.toString(), filePAth, filename, "image*//*");
        }*/
        else if (filename[0].equals("profilePicsReg")) {
            isFacebook = params[0].equalsIgnoreCase("isFacebook");
            response = GetWebservice.multipartRequest2(Const.SIGNUP, urlParams.toString(), filePAth, new String[]{"profilePics"}, "image/*");
        } else if (filename[0].equals("AudioChat")) {
            response = GetWebservice.multipartRequest2(Const.CHAT_FILE_UPLOAD, urlParams.toString(), filePAth, new String[]{"chatmedia"}, "audio/mp3");
        } else if (filename[0].equals("VideoChat")) {
            response = GetWebservice.multipartRequest2(Const.CHAT_FILE_UPLOAD, urlParams.toString(), filePAth, new String[]{"chatmedia"}, "video/*");
        } else if (filename[0].equals("ImageChat")) {
            response = GetWebservice.multipartRequest2(Const.CHAT_FILE_UPLOAD, urlParams.toString(), filePAth, new String[]{"chatmedia"}, "image/*");
        } else if (filename[0].equals("useraction")) {
            response = GetWebservice.multipartRequest2(Const.CHAT_REQUEST, urlParams.toString(), filePAth, new String[]{"chatmedia"}, "audio/mp3");
        } else {
            response = GetWebservice.multipartRequest2(Const.USER_SAVE, urlParams.toString(), filePAth, filename, "image/*");
        }

        return response;
    }

    @Override
    protected void onPostExecute(final String success) {
        if (filename[0].equals("chatmedia")) {
        } else {
            isDismiss();
        }

        try {
            obj = new JSONObject(success);
            Log.e("Response...", obj.toString());
            responseCode = obj.getInt(KEY_RESPONSE_CODE);
            if (responseCode == RESPONSE_CODE_VALUE) {
                // Util.getInstance(context).ShowToast(obj.getString(KEY_RESPONSE_MSG));
                if (filename[0].equals("chatmedia") || filename[0].equals("AudioChat") || filename[0].equals("VideoChat") || filename[0].equals("ImageChat")) {
                    commonListioner.onLoadSuccess(new JSONObject(success));
                } else if (filename[0].equals("useraction")) {
                    commonListioner.onLoadSuccess(new JSONObject(success));
                } else if (filename[0].equals(KEY_PASSPORT)) {
                    PrefsManager.setUserJson(obj.getJSONObject(KEY_DATA).toString());
                    context.finish();
                    //commonListioner.onLoadSuccess(new JSONObject(success));
                } else if (filename[0].equals(KEY_KUNDLI)) {
                    PrefsManager.setUserJson(obj.getJSONObject(KEY_DATA).toString());
                    context.finish();
                    // commonListioner.onLoadSuccess(new JSONObject(success));
                } else if (filename[0].equals(KEY_UPLOADCV)) {
                    PrefsManager.setUserJson(obj.getJSONObject(KEY_DATA).toString());
                    context.finish();
                    //commonListioner.onLoadSuccess(new JSONObject(success));
                } else if (filename[0].equals("profilePicsReg")) {
                    JSONObject user = obj.getJSONObject(KEY_DATA);
//                    PrefsManager.setUserJson(user.toString());
                   /* if (user.has(KEY_IS_ORIENTATION)) {
                        PrefsManager.setIsOrientation(user.getBoolean(KEY_IS_ORIENTATION));
                    }*/
                    Util.getInstance(context).saveUserData(user);
                    Util.getInstance(context).showOTPScreen(obj, true, isFacebook, false);
                } else if (filename[0].equals("image")) {
                    Util.getInstance(context).showLog("Profile pic upload response...");
                    commonListioner.onLoadSuccess(new JSONObject(success));
                } else if (filename[0].equals("audio")) {
                    Util.getInstance(context).showLog("audio upload response...");
                    commonListioner.onLoadSuccess(new JSONObject(success));
                } else if (filename[0].equals("video")) {
                    Util.getInstance(context).showLog("video upload response...");
                    commonListioner.onLoadSuccess(new JSONObject(success));
                } else {
                    JSONObject user = obj.getJSONObject(KEY_DATA);
                    PrefsManager.setUserJson(user.toString());
                    if (filename[0].equals("profilePics")) {
                        context.finish();
                    }
                }
            } else {
                Util.getInstance(context).ShowToast(obj.getString(KEY_RESPONSE_MSG));
                if (filename[0].equals("chatmedia")) {
                    commonListioner.onLoadFail(new JSONObject(success));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void isDismiss() {
        if (PD != null) {
            if (PD.isShowing()) {
                PD.dismiss();
            }
        }
    }
}
