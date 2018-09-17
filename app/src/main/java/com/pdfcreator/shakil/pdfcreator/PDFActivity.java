package com.pdfcreator.shakil.pdfcreator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.List;

public class PDFActivity extends AppCompatActivity {

    File pdfFile;

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfView=findViewById(R.id.pdfView);
        //this method will be used to preview the pdf we generate
        //getting the path where we will be store our pdf file
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        //if pdf folder does not exists then create a new folder

        //this part we are passing the path where the file will be stored and the file name
        pdfFile = new File(docsFolder.getAbsolutePath(),"HelloWorld.pdf");
        pdfView.fromFile(pdfFile).load();

    }
}
