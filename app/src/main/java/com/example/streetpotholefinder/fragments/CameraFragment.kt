/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.streetpotholefinder.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.example.streetpotholefinder.R
import com.example.streetpotholefinder.accident.Crack
import com.example.streetpotholefinder.accident.Porthole
import com.example.streetpotholefinder.databinding.FragmentCameraBinding
import com.example.streetpotholefinder.issue.Event
import com.google.android.gms.location.*
import org.tensorflow.lite.task.vision.detector.Detection
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class CameraFragment : Fragment(), ObjectDetectorHelper.DetectorListener {

    private val TAG = "ObjectDetection"
    private val OBJECT_POTHOLE = "bed"
    private val OBJECT_CRACK = "mouse"

    private var _fragmentCameraBinding: FragmentCameraBinding? = null

    private val fragmentCameraBinding
        get() = _fragmentCameraBinding!!

    private lateinit var objectDetectorHelper: ObjectDetectorHelper
    private lateinit var bitmapBuffer: Bitmap
    private lateinit var bitmapCaptured : Bitmap
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var cntCrack : Int = 0
    private var cntPothole : Int = 0

    // Store Info
    private var issueEvent : Event = Event.getInstance()
//    private lateinit var accident : Accident
    private lateinit var current_location : Location

    private var imageCapture: ImageCapture? = null


    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(CameraFragmentDirections.actionCameraToPermissions())
        }
    }

    override fun onDestroyView() {
        _fragmentCameraBinding = null
        super.onDestroyView()

        // Shut down our background executor
        cameraExecutor.shutdown()
        stopLocationUpdates()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false)

        return fragmentCameraBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        objectDetectorHelper = ObjectDetectorHelper(
            context = requireContext(),
            objectDetectorListener = this)

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Wait for the views to be properly laid out
        fragmentCameraBinding.viewFinder.post {
            // Set up the camera and its use cases
            setUpCamera()
        }

        // Attach listeners to UI control widgets
//        initBottomSheetControls()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                Log.d(TAG, "onViewCreated: Current GPS Info :" + location.toString())
                if (location != null) {
                    current_location = location
                }
            }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    var tvGPS: TextView? = null
                    tvGPS = requireActivity().findViewById(R.id.tvGpsInfo)
                    tvGPS.text = "위도 "+location.latitude.toString()+" 경도 "+location.longitude.toString()
                    current_location = location
                }
            }
        }
        startLocationUpdates()

        issueEvent.accident?.recStartTime = LocalDateTime.now()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

//    private fun initBottomSheetControls() {
//        // When clicked, lower detection score threshold floor
//        fragmentCameraBinding.bottomSheetLayout.thresholdMinus.setOnClickListener {
//            if (objectDetectorHelper.threshold >= 0.1) {
//                objectDetectorHelper.threshold -= 0.1f
//                updateControlsUi()
//            }
//        }
//
//        // When clicked, raise detection score threshold floor
//        fragmentCameraBinding.bottomSheetLayout.thresholdPlus.setOnClickListener {
//            if (objectDetectorHelper.threshold <= 0.8) {
//                objectDetectorHelper.threshold += 0.1f
//                updateControlsUi()
//            }
//        }
//
//        // When clicked, reduce the number of objects that can be detected at a time
//        fragmentCameraBinding.bottomSheetLayout.maxResultsMinus.setOnClickListener {
//            if (objectDetectorHelper.maxResults > 1) {
//                objectDetectorHelper.maxResults--
//                updateControlsUi()
//            }
//        }
//
//        // When clicked, increase the number of objects that can be detected at a time
//        fragmentCameraBinding.bottomSheetLayout.maxResultsPlus.setOnClickListener {
//            if (objectDetectorHelper.maxResults < 5) {
//                objectDetectorHelper.maxResults++
//                updateControlsUi()
//            }
//        }
//
//        // When clicked, decrease the number of threads used for detection
//        fragmentCameraBinding.bottomSheetLayout.threadsMinus.setOnClickListener {
//            if (objectDetectorHelper.numThreads > 1) {
//                objectDetectorHelper.numThreads--
//                updateControlsUi()
//            }
//        }
//
//        // When clicked, increase the number of threads used for detection
//        fragmentCameraBinding.bottomSheetLayout.threadsPlus.setOnClickListener {
//            if (objectDetectorHelper.numThreads < 4) {
//                objectDetectorHelper.numThreads++
//                updateControlsUi()
//            }
//        }
//
//        // When clicked, change the underlying hardware used for inference. Current options are CPU
//        // GPU, and NNAPI
//        fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.setSelection(0, false)
//        fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                    objectDetectorHelper.currentDelegate = p2
//                    updateControlsUi()
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//                    /* no op */
//                }
//            }
//
//        // When clicked, change the underlying model used for object detection
//        fragmentCameraBinding.bottomSheetLayout.spinnerModel.setSelection(0, false)
//        fragmentCameraBinding.bottomSheetLayout.spinnerModel.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                    objectDetectorHelper.currentModel = p2
//                    updateControlsUi()
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//                    /* no op */
//                }
//            }
//    }
//
//    // Update the values displayed in the bottom sheet. Reset detector.
//    private fun updateControlsUi() {
//        fragmentCameraBinding.bottomSheetLayout.maxResultsValue.text =
//            objectDetectorHelper.maxResults.toString()
//        fragmentCameraBinding.bottomSheetLayout.thresholdValue.text =
//            String.format("%.2f", objectDetectorHelper.threshold)
//        fragmentCameraBinding.bottomSheetLayout.threadsValue.text =
//            objectDetectorHelper.numThreads.toString()
//
//        // Needs to be cleared instead of reinitialized because the GPU
//        // delegate needs to be initialized on the thread using it when applicable
//        objectDetectorHelper.clearObjectDetector()
//        fragmentCameraBinding.overlay.clear()
//    }

    // Initialize CameraX, and prepare to bind the camera use cases
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    // Declare and bind preview, capture and analysis use cases
    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector - makes assumption that we're only using the back camera
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        // Preview. Only using the 4:3 ratio because this is the closest to our models
        preview =
            Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
                .build()

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
            .build()

        // ImageAnalysis. Using RGBA 8888 to match how our models work
        imageAnalyzer =
            ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        if (!::bitmapBuffer.isInitialized) {
                            // The image rotation and RGB image buffer are initialized only once
                            // the analyzer has started running
                            bitmapBuffer = Bitmap.createBitmap(
                                image.width,
                                image.height,
                                Bitmap.Config.ARGB_8888
                            )
                        }

                        detectObjects(image)
                    }
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer, imageCapture)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(fragmentCameraBinding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        Log.d(TAG, "onCaptureSuccess: start")
        val imageCapture = imageCapture ?: return
        Log.d(TAG, "onCaptureSuccess: alive")

        imageCapture.takePicture(
            cameraExecutor,
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
//                    super.onCaptureSuccess(image)
//                    if (!::bitmapCaptured.isInitialized){
//                        bitmapCaptured = Bitmap.createBitmap(
//                            image.width,
//                            image.height,
//                            Bitmap.Config.ARGB_8888
//                        )
//                    }
//                    image.use { bitmapCaptured.copyPixelsFromBuffer(image.planes[0].buffer) }
                    Log.d(TAG, "onCaptureSuccess: ${image.imageInfo.toString()}")
                }

                override fun onError(exception: ImageCaptureException) {
//                    super.onError(exception)
                    Log.d(TAG, "onCaptureSuccess: error ${exception.toString()}")
                }
            }
        )
    }

    private fun detectObjects(image: ImageProxy) {
        // Copy out RGB bits to the shared bitmap buffer
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }

        val imageRotation = image.imageInfo.rotationDegrees
        // Pass Bitmap and rotation to the object detector helper for processing and detection
        objectDetectorHelper.detect(bitmapBuffer, imageRotation)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation = fragmentCameraBinding.viewFinder.display.rotation
    }

    // Update UI after objects have been detected. Extracts original image height/width
    // to scale and place bounding boxes properly through OverlayView
    override fun onResults(
        results: MutableList<Detection>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int
    ) {
        activity?.runOnUiThread {
//            fragmentCameraBinding.bottomSheetLayout.inferenceTimeVal.text =
//                String.format("%d ms", inferenceTime)

            // Pass necessary information to OverlayView for drawing on the canvas
            try {
                if (fragmentCameraBinding != null)
                {
//                    Log.d(TAG, "DetectObject: "+results.toString())
                    fragmentCameraBinding.overlay.setResults(
                        results ?: LinkedList<Detection>(),
                        imageHeight,
                        imageWidth
                    )

                    // Force a redraw
                    fragmentCameraBinding.overlay.invalidate()

                    // draw Image
                    var curLocation = LocationServices.getFusedLocationProviderClient(requireContext())
                    if(results?.get(0)?.categories?.get(0)?.label!! == OBJECT_POTHOLE)
                    {
                        cntPothole += 1
                        requireActivity().findViewById<TextView>(R.id.cntPothole).text = cntPothole.toString()

                        takePhoto()
                        var screenshot : Bitmap = viewToBitmap(fragmentCameraBinding.overlay)
                        var issue = Porthole(screenshot, current_location, LocalDateTime.now())
                        var result = issueEvent.accident?.portholes?.add(issue as Porthole)
                        if(issueEvent.accident == null) Log.d(TAG, "onResults: accident is null")
                        if(issueEvent.accident?.portholes == null) Log.d(TAG, "onResults: portholes is null")
                        Log.d(TAG, "onResults porthole size: ${issueEvent.accident?.portholes?.size}, ${result}")

                    }else if(results?.get(0)?.categories?.get(0)?.label!! == OBJECT_CRACK)
                    {
                        cntCrack += 1
                        requireActivity().findViewById<TextView>(R.id.cntCrack).text = cntCrack.toString()

                        // Screen shot image
                        takePhoto()
                        var screenshot : Bitmap = bitmapCaptured
                        var issue = Crack(screenshot, current_location, LocalDateTime.now())
                        issueEvent.accident?.cracks?.add(issue as Crack)
//                        issueEvent.accident?.issueTime = LocalDateTime.now()
                        var result = issueEvent.accident?.cracks?.add(issue)
                        if(issueEvent.accident == null) Log.d(TAG, "onResults: accident is null")
                        if(issueEvent.accident?.cracks == null) Log.d(TAG, "onResults: portholes is null")
                        Log.d(TAG, "onResults porthole size: ${issueEvent.accident?.portholes?.size}, ${result}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, " error: " + e.message)
            }
        }
    }

    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    // 뷰를 비트맵으로 변환
    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }
//    viewToBitmap(frameLayout)
}
