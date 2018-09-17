package com.pdfcreator.shakil.pdfcreator;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button generate_pdf;
    private EditText content;
    LinearLayout linearLayout;
    //TAG for this activity
    private static final String TAG = "PdfCreatorMainActivity";

    //taking file to store data
    private File pdfFile;
    //request code for permission
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init the attributes
        generate_pdf=findViewById(R.id.generatePdf);
        content=findViewById(R.id.pdfContentEditext);
        linearLayout=findViewById(R.id.mainLayout);

        //setting the on click listener of button
        generate_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (content.getText().toString().isEmpty()){
                    content.setError("This field can not be empty");
                    content.requestFocus();
                    return;
                }
                try{
                    createPdfWrapper();
                }catch (FileNotFoundException e){
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }catch (DocumentException e){
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    //this method will create a pdf from the content and it throws two exception
    //one is filwnotfound exception and another one is document exception
    private void createPdfWrapper() throws FileNotFoundException,DocumentException{
        //getting the permission code
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //checking whether the permission codeis granted or not granted
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            //checking the build version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    //calling a method which will generate a dialog interface based on the message we pass in the params
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            createPdf();
        }
    }
    //overriding this method in order to ensure the permision check
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //this method will cretae a dialog interface to take permission from the user
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //this is the main method where will will be generating our pdf and has the same two exceptption that we throwed in the previous method
    private void createPdf() throws FileNotFoundException, DocumentException {
        //getting the path where we will be store our pdf file
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        //if pdf folder does not exists then create a new folder
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }
        //this part we are passing the path where the file will be stored and the file name
        pdfFile = new File(docsFolder.getAbsolutePath(),"HelloWorld.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        document.add(new Paragraph(content.getText().toString()));

        document.close();
        Snackbar.make(linearLayout,"Pdf created successfully",Snackbar.LENGTH_LONG).show();
        startActivity(new Intent(MainActivity.this,PDFActivity.class));
    }


}
