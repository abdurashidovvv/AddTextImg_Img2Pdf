package com.abdurashidov.imgwrite

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abdurashidov.imgwrite.databinding.ActivityMain2Binding
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    private  val TAG = "MainActivity2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.image.setImageBitmap(MyData.bitmap)

        binding.download.setOnClickListener {

//            addImage()
            GlobalScope.launch(Dispatchers.IO) {
                imgToPdf(MyData.bitmap!!)
            }
        }
    }


    suspend fun imgToPdf(bitmap: Bitmap) {
        // Create a PdfDocument with a page of the same size as the image
        val document: PdfDocument = PdfDocument()
        val pageInfo: PdfDocument.PageInfo =
            PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)
        // Draw the bitmap onto the page
        val canvas: Canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)
        // Write the PDF file to a file
        var mPath = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            mPath = this.getExternalFilesDir("Saydullo").toString();
        } else {
            mPath = Environment.getExternalStorageDirectory().toString() + "Saydullo";
        }
//        val folder = File(mPath)
//        if (!folder.exists()) {
//            withContext(Dispatchers.IO) {
//                folder.mkdirs()
//                Log.d(TAG, "imgToPdf: ${folder.mkdirs()}")
//            }
//        }
        withContext(Dispatchers.IO) {
            document.writeTo(FileOutputStream(mPath +"/"+ "Saydullo.pdf"))
            Log.d(TAG, "imgToPdf: ${document.pages}")
        }
        document.close()
    }



}