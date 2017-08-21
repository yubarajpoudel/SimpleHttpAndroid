package com.yuviii.restdata;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedProgressListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.FileMultiPart;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by yubaraj on 8/21/17.
 */

public class UploadImageActivity extends AppCompatActivity {

    static final int CAMERA_PIC_REQUEST = 1337;
    private String selectedImagePath;
    int SELECT_FILE = 2;
    ImageView iv_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimage);
        iv_image = (ImageView) findViewById(R.id.returnedPic);
        Button camera = (Button) findViewById(R.id.picButton);
        camera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void uploadFile(final ArrayList<String> imgPaths) {

        String charset = "UTF-8";
        // uploading url -> url where you want to upload image
        // https://abc.com/upload/...

        String requestURL = "UPLOADING URL";

        FileMultiPart fileMultiPart = FileMultiPart.instance();
        fileMultiPart.setCharSet(charset);
        fileMultiPart.addFile("file", selectedImagePath);
        fileMultiPart.addFromField("description", "test");

        Log.w("TestApiActivity", "filemultipart :: " + fileMultiPart.toString());

        DataRequest request = DataRequest.getInstance();
        request.addFileMultiPart(fileMultiPart);
        request.addUrl(requestURL);

        // optional if needed only
        request.addHeaders(new String[]{"Authorization"}, new String[]{"Bearer YOUR_HEADER_KEY"});

        DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class).addRequestBody(request);
        requestManager.addOnDataRecieveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("TestApiActivity", "response = " + object.toString());
            }
        });
        requestManager.sync();
    }


    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String options[] = {"From Camera", "From Gallery"};
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                if (index == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File dir = new File(Environment.getExternalStorageDirectory() + "/nmefen");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, System.currentTimeMillis() + ".jpg");
                    selectedImagePath = file.getAbsolutePath();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                }
            }
        });
        builder.setNeutralButton("Cancel", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                iv_image.setImageBitmap(Utils.getDecodedBitmap(selectedImagePath));
            } else {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                selectedImagePath = Utils.getRealPathFromURI(this, selectedImageUri);
                try {
                    iv_image.setImageBitmap(Utils.getDecodedBitmap(selectedImagePath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("TestApiActivity", "selectedImagePath = " + selectedImagePath);
            }
            if (selectedImagePath.length() > 0 && !selectedImagePath.startsWith("http://")) {
                try {
                    File dir = new File(Environment.getExternalStorageDirectory() + "/nmefen");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, System.currentTimeMillis() + "_decoded.jpg");
                    FileOutputStream fout = new FileOutputStream(file);
                    Bitmap bmp = Utils.getDecodedBitmap(selectedImagePath);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                    fout.flush();
                    fout.close();
                    bmp.recycle();
                    System.gc();
                    selectedImagePath = file.getAbsolutePath();
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(selectedImagePath);
                    Log.d("TestApiActivity", "selectedImagePath2 = " + selectedImagePath);
                    uploadFile(arrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
