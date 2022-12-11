package com.abdurashidov.imgwrite

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.abdurashidov.imgwrite.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private  val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.save.setOnClickListener {
            binding.tv.text=binding.edt.text.toString()
            val bitmap = loadBitmapFromView(binding.main)!!
            val dir = File(filesDir, "folderName")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "capture.jpg")
            try {
                val fos = FileOutputStream(file)
                bitmap .compress(Bitmap.CompressFormat.PNG, 100, fos)
                Log.d(TAG, "onCreate: success")
                MyData.bitmap=bitmap
                startActivity(Intent(this, MainActivity2::class.java))

            } catch (e: Exception) {
                Log.d(TAG, "onCreate: ${e.message}")
            }
        }

    }

    fun loadBitmapFromView(v: View): Bitmap? {
        val b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        val paint=Paint()
        paint.setColor(Color.BLACK)
        c.drawText("Assalomu alaykum", c.width/2.toFloat(), c.height/2.toFloat(), paint)
//        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom())
        v.draw(c)
        return b
    }


    suspend fun saveImageToGallery(bitmap: Bitmap) {

        // Create a PdfDocument with a page of the same size as the image
        val document: PdfDocument = PdfDocument()
        val pageInfo: PdfDocument.PageInfo  = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page: PdfDocument.Page  = document.startPage(pageInfo)

        // Draw the bitmap onto the page
        val canvas: Canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        // Write the PDF file to a file
        var mPath=""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            mPath= this.getExternalFilesDir("PDFfiles").toString();
        }
        else
        {
            mPath= Environment.getExternalStorageDirectory().toString() ;
        }
        val file=File(mPath)
        if (!file.exists()){
            withContext(Dispatchers.IO) {
                file.mkdirs()
            }
        }
        withContext(Dispatchers.IO) {
            document.writeTo(FileOutputStream(mPath+"/example.pdf"))
        }
        document.close()
    }


}