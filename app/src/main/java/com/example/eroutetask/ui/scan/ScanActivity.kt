package com.example.eroutetask.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eroutetask.R
import com.example.eroutetask.util.createFactory
import com.example.eroutetask.util.threads.PostExecutionThread
import com.example.eroutetask.util.threads.SchedulersThread
import com.example.eroutetask.util.threads.ThreadExecutor
import com.example.eroutetask.util.threads.UiThread
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

class ScanActivity : AppCompatActivity() {

    private lateinit var  cameraSource: CameraSource
    private var  RequestCameraPermissionID =100
    private lateinit var surface_view: SurfaceView
    private lateinit var scanViewmodel: ScanViewmodel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        init()

        setUpOcr()

        setObserver()





    }

    private fun setUpOcr() {
        val textRecognizer = TextRecognizer.Builder(applicationContext).build()

        if (!textRecognizer.isOperational) {
            Log.w("MainActivity", "Detector dependencies are not yet available")
        } else {
            cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build()

            surface_view.getHolder().addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this@ScanActivity, arrayOf(Manifest.permission.CAMERA),
                                RequestCameraPermissionID)
                            return
                        }
                        cameraSource.start(surface_view.getHolder())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}
                override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                    cameraSource.stop()
                }
            })
            textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
                override fun release() {}
                override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                    val items = detections.detectedItems
                    if (items.size() != 0) {
                        text_view.post(Runnable {
                            val stringBuilder = StringBuffer()
                            for (i in 0 until items.size()) {
                                val item = items.valueAt(i)
                                stringBuilder.append(item.value)
                                stringBuilder.append("\n")
                            }

                            scanViewmodel.scan(stringBuilder)

                        })
                    }
                }
            })
        }
    }

    private fun setObserver() {
        scanViewmodel.mutableLiveData.observe(this, Observer {
            when(it){
                is ScanState.ScanSuccess -> text_view.setText("${it.data}")
                ScanState.NoData -> text_view.setText("No Data")
            }
        })
    }

    private fun init(){
        surface_view =  findViewById(R.id.surface_view) as SurfaceView
        val factory = ScanViewmodel(UiThread(),SchedulersThread(),ScanHelper()).createFactory()
        scanViewmodel = ViewModelProvider(this, factory).get(ScanViewmodel::class.java)

    }
}
