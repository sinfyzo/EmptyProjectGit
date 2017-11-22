package com.emptyproject.listeners;


import android.widget.ImageView;

/**
 * Created by manish on 11/27/2015.
 */
public interface DownloadListener {
//    public void onLoadFailVolleyError(VolleyError error);
void onDownloadFail();
    void onDownloadSuccess(boolean isDownload, int messageType, String loginid, ImageView ivPlay);
    void onDownloadSuccess(boolean isDownloaded, String filePath, int fileType);
}
