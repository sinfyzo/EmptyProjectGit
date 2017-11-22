package com.emptyproject.webapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import com.datecoy.R;
import com.datecoy.listioner.DownloadListioner;
import com.datecoy.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by INFYZO\shailesh.pateliya on 22/10/16.
 */

public class DownloadFileAsync extends AsyncTask<String, String, String> implements WebUtil {
    ProgressDialog PD;
    Activity context;
    String fileUrl;
    DownloadListioner downloadListioner;
    boolean isProgressBarVisible,isDownloaded = false;
    int fileType;
    String filePath="";

    public DownloadFileAsync(Activity context, String url1, boolean isProgressBarVisible, DownloadListioner downloadListioner, int fileType) {
        this.context = context;
        this.fileUrl = url1;
        this.downloadListioner = downloadListioner;
        this.isProgressBarVisible = isProgressBarVisible;
        this.fileType = fileType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Util.getInstance(context).showLog(fileUrl);
        if (isProgressBarVisible) {
            PD = new ProgressDialog(context);
            PD.setCancelable(false);
            PD.show();
            PD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            PD.setContentView(R.layout.progressdialog);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        int count;
        try {
            if (isCancelled()) {
                isDownloaded = false;
                return (null); // don't forget to terminate this method
            }
            URL url11 = new URL(fileUrl);
            URLConnection conection = url11.openConnection();
            conection.connect();
            int lenghtOfFile = conection.getContentLength();
            InputStream input = new BufferedInputStream(url11.openStream(), 8192);
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
//            if (msgType == ChatActivity.TYPE_AUDIO) {

            filePath = Util.getInstance(context).getMediaStoragePath(fileType) + File.separator + fileName;
            if (!Util.getInstance(context).getMediaStoragePath(fileType).exists()) {
                if (!Util.getInstance(context).getMediaStoragePath(fileType).mkdirs()) {
                    Log.d(Util.getInstance(context).getMediaStoragePath(fileType).toString(), "Oops! Failed create "
                            +Util.getInstance(context).getMediaStoragePath(fileType).toString() + " directory");
                    isDownloaded = false;
                    return (null);
                }
            }
            Util.getInstance(context).showLog("SaveFilePath...." + filePath);
            // Output stream
            OutputStream output = new FileOutputStream(filePath);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            isDownloaded = true;
        } catch (Exception e) {
            Util.getInstance(context).showLog("Error...." + e);
        }
        return filePath;
    }

    @Override
    protected void onPostExecute(final String success) {
        if(isProgressBarVisible){
            isDismiss();
        }
        isDownloaded = true;
        downloadListioner.onDownloadSuccess(isDownloaded, success,fileType);
    }

    public void isDismiss() {
        if (PD != null) {
            if (PD.isShowing()) {
                PD.dismiss();
            }
        }
    }
}
