package org.jshobbysoft.cameraalignng

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import android.content.res.Configuration
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import java.io.File
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import org.jshobbysoft.cameraalignng.ui.theme.CameraAlignNGTheme

enum class AppScreen {
    PICTURE_VIEW,
    CAMERA_VIEW
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CameraAlignNGTheme {
                CameraAlignApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraAlignApp() {
    var currentScreen by remember { mutableStateOf(AppScreen.PICTURE_VIEW) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var onTakeStopMotionPhotoTrigger by remember { mutableStateOf<(() -> Unit)?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentScreen) {
                            AppScreen.PICTURE_VIEW -> "Picture View"
                            AppScreen.CAMERA_VIEW -> "Semi-Transparent Camera"
                        }
                    )
                },
                actions = {
                    if (currentScreen == AppScreen.CAMERA_VIEW && onTakeStopMotionPhotoTrigger != null) {
                        Button(
                            onClick = { onTakeStopMotionPhotoTrigger?.invoke() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Stop motion")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.PICTURE_VIEW,
                    onClick = { currentScreen = AppScreen.PICTURE_VIEW },
                    icon = { Icon(Icons.Default.Image, contentDescription = "Picture View") },
                    label = { Text("Picture") }
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.CAMERA_VIEW,
                    onClick = { currentScreen = AppScreen.CAMERA_VIEW },
                    icon = { Icon(Icons.Default.CameraAlt, contentDescription = "Camera View") },
                    label = { Text("Camera") }
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            AnimatedContent(
                targetState = currentScreen,
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    AppScreen.PICTURE_VIEW -> PictureView(
                        imageUri = selectedImageUri,
                        onPickImage = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onGoToCamera = { currentScreen = AppScreen.CAMERA_VIEW }
                    )

                    AppScreen.CAMERA_VIEW -> CameraView(
                        backgroundImageUri = selectedImageUri,
                        onPhotoCaptured = { newUri ->
                            selectedImageUri = newUri
                        },
                        onRegisterStopMotionTrigger = { trigger ->
                            onTakeStopMotionPhotoTrigger = trigger
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PictureView(
    imageUri: Uri?,
    onPickImage: () -> Unit,
    onGoToCamera: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageUri != null) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "User Chosen Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onPickImage) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change Picture")
                }
                Button(onClick = onGoToCamera) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Camera Preview")
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Picture Selected",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Choose a picture from your gallery to display it here and use it in the camera overlay view.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = onPickImage) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select Picture")
                    }
                }
            }
        }
    }
}

@Composable
fun CameraView(
    backgroundImageUri: Uri?,
    onPhotoCaptured: (Uri) -> Unit = {},
    onRegisterStopMotionTrigger: (((() -> Unit)?) -> Unit) = {}
) {
    val context = LocalContext.current
    val imageCapture = remember { ImageCapture.Builder().build() }
    var showSaveSnackbar by remember { mutableStateOf(false) }
    var pendingSaveUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(imageCapture) {
        onRegisterStopMotionTrigger {
            val photoFile = File(
                context.cacheDir,
                "stop_motion_${System.currentTimeMillis()}.jpg"
            )
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            val executor = ContextCompat.getMainExecutor(context)

            imageCapture.takePicture(
                outputOptions,
                executor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                        onPhotoCaptured(savedUri)
                        pendingSaveUri = savedUri
                        showSaveSnackbar = true
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("StopMotion", "Photo capture failed", exception)
                    }
                }
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onRegisterStopMotionTrigger(null)
        }
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasCameraPermission) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Camera Permission Required",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please grant camera permission to display the semi-transparent camera preview.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("Grant Permission")
                }
            }
        }
    } else {
        var alphaValue by remember { mutableFloatStateOf(0.5f) }
        var horizontalFlip by remember { mutableStateOf(false) }
        var verticalFlip by remember { mutableStateOf(false) }
        var rotationDegrees by remember { mutableIntStateOf(0) }
        var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

        // Independent scaling modes for front and back cameras
        var frontScaleType by remember { mutableStateOf(PreviewView.ScaleType.FILL_CENTER) }
        var backScaleType by remember { mutableStateOf(PreviewView.ScaleType.FILL_CENTER) }

        var isPickingColor by remember { mutableStateOf(false) }
        var sourceBitmap by remember(backgroundImageUri) { mutableStateOf<Bitmap?>(null) }
        var processedBitmap by remember(backgroundImageUri) { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(backgroundImageUri) {
            if (backgroundImageUri != null) {
                val bmp = loadBitmapFromUri(context, backgroundImageUri)
                sourceBitmap = bmp
                processedBitmap = bmp
            } else {
                sourceBitmap = null
                processedBitmap = null
            }
        }

        val isFrontCamera = (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA)
        val activeScaleType = if (isFrontCamera) frontScaleType else backScaleType
        val contentScale = if (activeScaleType == PreviewView.ScaleType.FILL_CENTER) ContentScale.Crop else ContentScale.Fit

        // Automatic front camera horizontal mirroring combined with user flip
        val effectiveScaleX = (if (horizontalFlip) -1f else 1f) * (if (isFrontCamera) -1f else 1f)
        val effectiveScaleY = if (verticalFlip) -1f else 1f

        Box(modifier = Modifier.fillMaxSize()) {
            // Background View: User chosen picture (if available) with transformations, scaling & color picking
            if (backgroundImageUri != null && processedBitmap != null) {
                val currentBmp = processedBitmap!!
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = effectiveScaleX,
                            scaleY = effectiveScaleY,
                            rotationZ = rotationDegrees.toFloat()
                        )
                        .pointerInput(isPickingColor, sourceBitmap) {
                            if (isPickingColor) {
                                detectTapGestures { offset ->
                                    val bmp = sourceBitmap
                                    if (bmp != null) {
                                        val viewWidth = size.width.toFloat()
                                        val viewHeight = size.height.toFloat()
                                        val bmpWidth = bmp.width.toFloat()
                                        val bmpHeight = bmp.height.toFloat()

                                        val scale = if (contentScale == ContentScale.Crop) {
                                            maxOf(viewWidth / bmpWidth, viewHeight / bmpHeight)
                                        } else {
                                            minOf(viewWidth / bmpWidth, viewHeight / bmpHeight)
                                        }
                                        val scaledW = bmpWidth * scale
                                        val scaledH = bmpHeight * scale
                                        val offsetX = (viewWidth - scaledW) / 2f
                                        val offsetY = (viewHeight - scaledH) / 2f

                                        val imgX = offset.x - offsetX
                                        val imgY = offset.y - offsetY

                                        val bmpX = (imgX / scale).toInt()
                                        val bmpY = (imgY / scale).toInt()

                                        if (bmpX in 0 until bmp.width && bmpY in 0 until bmp.height) {
                                            val pickedColor = bmp.getPixel(bmpX, bmpY)
                                            processedBitmap = makeColorTransparent(bmp, pickedColor, tolerance = 40)
                                        }
                                    }
                                    isPickingColor = false
                                }
                            }
                        }
                ) {
                    Image(
                        bitmap = currentBmp.asImageBitmap(),
                        contentDescription = "Background Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = contentScale
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No image selected\n(Camera preview shown with semi-transparency)",
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Foreground View: Camera preview with 0.5 alpha, camera selector, and active scaleType
            CameraPreview(
                alpha = alphaValue,
                cameraSelector = cameraSelector,
                scaleType = activeScaleType,
                imageCapture = imageCapture,
                modifier = Modifier.fillMaxSize()
            )

            // "Save photo?" Snackbar prompt
            if (showSaveSnackbar && pendingSaveUri != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .zIndex(100f),
                    action = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    showSaveSnackbar = false
                                }
                            ) {
                                Text("No")
                            }
                            Button(
                                onClick = {
                                    pendingSaveUri?.let { uri ->
                                        saveImageToCameraFolder(context, uri)
                                    }
                                    showSaveSnackbar = false
                                }
                            ) {
                                Text("Yes")
                            }
                        }
                    }
                ) {
                    Text("Save photo?")
                }
            }

            // Prompt banner when picking color
            if (isPickingColor) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                        .align(Alignment.TopCenter),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Touch a color on the photo to make it transparent",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Controls Overlay (Alpha Slider + Action Buttons)
            val configuration = LocalConfiguration.current
            val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

            val actionButtons: @Composable () -> Unit = {
                IconButton(onClick = { horizontalFlip = !horizontalFlip }) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "Horizontal Flip")
                }
                IconButton(onClick = { verticalFlip = !verticalFlip }) {
                    Icon(Icons.Default.SwapVert, contentDescription = "Vertical Flip")
                }
                IconButton(onClick = { rotationDegrees = (rotationDegrees + 90) % 360 }) {
                    Icon(Icons.Default.RotateRight, contentDescription = "Rotate 90°")
                }
                IconButton(onClick = {
                    cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
                }) {
                    Icon(Icons.Default.Cameraswitch, contentDescription = "Switch Camera")
                }
                IconButton(onClick = {
                    val newScale = if (activeScaleType == PreviewView.ScaleType.FILL_CENTER) {
                        PreviewView.ScaleType.FIT_CENTER
                    } else {
                        PreviewView.ScaleType.FILL_CENTER
                    }
                    if (isFrontCamera) {
                        frontScaleType = newScale
                    } else {
                        backScaleType = newScale
                    }
                }) {
                    Icon(Icons.Default.Crop, contentDescription = "Toggle Scale Mode")
                }
                IconButton(onClick = { isPickingColor = !isPickingColor }) {
                    Icon(
                        Icons.Default.Colorize,
                        contentDescription = "Pick Color for Transparency",
                        tint = if (isPickingColor) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    )
                }
                if (sourceBitmap != null) {
                    IconButton(onClick = { processedBitmap = sourceBitmap }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset Transparency")
                    }
                }
            }

            if (isLandscape) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .zIndex(10f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${(alphaValue * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Slider(
                                    value = alphaValue,
                                    onValueChange = { alphaValue = it },
                                    valueRange = 0f..1f,
                                    modifier = Modifier
                                        .requiredWidth(100.dp)
                                        .graphicsLayer {
                                            rotationZ = -90f
                                        }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            actionButtons()
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp)
                        .fillMaxWidth()
                        .zIndex(10f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Camera Preview Alpha: ${(alphaValue * 100).toInt()}% (${if (isFrontCamera) "Front" else "Back"} - ${activeScaleType.name})",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Slider(
                                value = alphaValue,
                                onValueChange = { alphaValue = it },
                                valueRange = 0f..1f
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            actionButtons()
                        }
                    }
                }
            }
        }
    }
}

fun loadBitmapFromUri(context: android.content.Context, uri: Uri): Bitmap? {
    return try {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.isMutableRequired = true
            }
        } else {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)?.copy(Bitmap.Config.ARGB_8888, true)
        }
    } catch (e: Exception) {
        null
    }
}

fun makeColorTransparent(sourceBitmap: Bitmap, targetColor: Int, tolerance: Int = 40): Bitmap {
    val mutableBitmap = sourceBitmap.copy(Bitmap.Config.ARGB_8888, true)
    val width = mutableBitmap.width
    val height = mutableBitmap.height

    val targetR = android.graphics.Color.red(targetColor)
    val targetG = android.graphics.Color.green(targetColor)
    val targetB = android.graphics.Color.blue(targetColor)

    val pixels = IntArray(width * height)
    mutableBitmap.getPixels(pixels, 0, width, 0, 0, width, height)

    for (i in pixels.indices) {
        val pixel = pixels[i]
        val r = android.graphics.Color.red(pixel)
        val g = android.graphics.Color.green(pixel)
        val b = android.graphics.Color.blue(pixel)

        if (Math.abs(r - targetR) <= tolerance &&
            Math.abs(g - targetG) <= tolerance &&
            Math.abs(b - targetB) <= tolerance) {
            pixels[i] = android.graphics.Color.TRANSPARENT
        }
    }

    mutableBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return mutableBitmap
}

fun saveImageToCameraFolder(context: android.content.Context, sourceUri: Uri) {
    val timeStamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
    val filename = "IMG_$timeStamp.jpg"

    val contentValues = android.content.ContentValues().apply {
        put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, android.os.Environment.DIRECTORY_DCIM + "/Camera")
            put(android.provider.MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val resolver = context.contentResolver
    val imageUri = resolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    if (imageUri != null) {
        try {
            resolver.openOutputStream(imageUri)?.use { outputStream ->
                resolver.openInputStream(sourceUri)?.use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(android.provider.MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)
            }
        } catch (e: Exception) {
            Log.e("SavePhoto", "Failed to save photo to MediaStore", e)
        }
    }
}

@Composable
fun CameraPreview(
    alpha: Float,
    cameraSelector: CameraSelector,
    scaleType: PreviewView.ScaleType,
    imageCapture: ImageCapture,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                this.scaleType = scaleType
            }
        },
        update = { previewView ->
            previewView.scaleType = scaleType
            val ctx = previewView.context
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", e)
                }
            }, ContextCompat.getMainExecutor(ctx))
        },
        modifier = modifier.alpha(alpha)
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PictureViewPreview() {
    CameraAlignNGTheme {
        PictureView(
            imageUri = null,
            onPickImage = {},
            onGoToCamera = {}
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun CameraAlignAppPreview() {
    CameraAlignNGTheme {
        CameraAlignApp()
    }
}
