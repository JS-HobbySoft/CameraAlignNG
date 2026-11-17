package org.jshobbysoft.cameraalignng

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jshobbysoft.cameraalignng.ui.theme.CameraAlignNGTheme

/**
 * Play Store Feature Graphic (1024x500 px aspect ratio preview)
 */
@Preview(widthDp = 1024, heightDp = 500, name = "Play Store Feature Graphic")
@Composable
fun FeatureGraphicPreview() {
    CameraAlignNGTheme {
        Surface(
            modifier = Modifier
                .width(1024.dp)
                .height(500.dp),
            color = Color(0xFFE3E340)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFF5F560), Color(0xFFD4D430)),
                            radius = 800f
                        )
                    )
                    .padding(48.dp)
            ) {
                // Background decorative alignment grid lines
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(Color.Black.copy(alpha = 0.08f))
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth(0.6f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.15f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = " CAMERA ALIGNMENT & OVERLAY ",
                            color = Color(0xFF1C1C1C),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Text(
                        text = "CameraAlignNG",
                        color = Color(0xFF1C1C1C),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1).sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Align your camera with any reference photo. Semi-transparent live preview, color chroma-keying, and precise scaling.",
                        color = Color(0xFF2C2C2C),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 28.sp
                    )
                }

                // App Graphic Badge / Illustration on the right
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(280.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF1C1C1C))
                        .border(4.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(32.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE3E340)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color(0xFF1C1C1C),
                                modifier = Modifier.size(56.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.1f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = Color.White)
                                }
                            }
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFE3E340).copy(alpha = 0.2f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFFE3E340))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Play Store High-Resolution App Icon (512x512 px preview)
 */
@Preview(widthDp = 512, heightDp = 512, name = "Play Store App Icon (512x512)")
@Composable
fun StoreAppIconPreview() {
    CameraAlignNGTheme {
        Surface(
            modifier = Modifier
                .width(512.dp)
                .height(512.dp),
            color = Color(0xFFE3E340)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(320.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1C1C1C)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "App Icon",
                        tint = Color(0xFFE3E340),
                        modifier = Modifier.size(160.dp)
                    )
                }
            }
        }
    }
}

/**
 * Play Store Screenshot 1: Picture View
 */
@Preview(widthDp = 411, heightDp = 891, name = "Play Store Screenshot - Picture View")
@Composable
fun StoreScreenshotPictureViewPreview() {
    CameraAlignNGTheme {
        Surface(
            modifier = Modifier
                .width(411.dp)
                .height(891.dp),
            color = Color(0xFFE3E340)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Step 1: Choose Reference Photo",
                    color = Color(0xFF1C1C1C),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Mock Picture View Card
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE3E340).copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    tint = Color(0xFF1C1C1C),
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Select Reference Image",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1C1C1C)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "CameraAlignNG",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1C1C1C)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Play Store Screenshot 2: Camera Overlay View
 */
@Preview(widthDp = 411, heightDp = 891, name = "Play Store Screenshot - Camera Overlay")
@Composable
fun StoreScreenshotCameraViewPreview() {
    CameraAlignNGTheme {
        Surface(
            modifier = Modifier
                .width(411.dp)
                .height(891.dp),
            color = Color(0xFF1C1C1C)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Step 2: Align with Camera Overlay",
                        color = Color(0xFFE3E340),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Bottom Controls Card Preview
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF2C2C2C).copy(alpha = 0.95f),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Camera Preview Alpha: 50% (Back - FILL_CENTER)",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color(0xFFE3E340))
                            Icon(Icons.Default.SwapVert, contentDescription = null, tint = Color(0xFFE3E340))
                            Icon(Icons.Default.RotateRight, contentDescription = null, tint = Color(0xFFE3E340))
                            Icon(Icons.Default.Cameraswitch, contentDescription = null, tint = Color(0xFFE3E340))
                            Icon(Icons.Default.Colorize, contentDescription = null, tint = Color(0xFFE3E340))
                        }
                    }
                }
            }
        }
    }
}
