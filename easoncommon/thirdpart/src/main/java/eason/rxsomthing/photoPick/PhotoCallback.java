package eason.rxsomthing.photoPick;

import android.net.Uri;

import java.io.File;
import java.util.List;

public interface PhotoCallback {

    void callBackUris(List<Uri> uris);

    void callBackFiles(List<File> imgFiles);
}