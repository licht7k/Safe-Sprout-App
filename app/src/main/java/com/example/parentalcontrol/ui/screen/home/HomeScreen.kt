package com.example.parentalcontrol.ui.screen.home
import com.example.parentalcontrol.R

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.outlined.BatteryFull
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource




@Immutable
data class DeviceUi(
    val name: String,
    val batteryPercent: Int,
    val wifiName: String // you can repurpose this later if you want
)

@Immutable
data class TodayActivityUi(
    val label: String = "Child Activity Today",
    val usageLabel: String = "Phone Usage",
    val totalMinutes: Int,
    val progress: Float,
    val mostUsedAppName: String,
)

@Immutable
data class QuickTileUi(
    val title: String,
    val icon: @Composable () -> Unit,
    val bg: Color,
    val onClick: () -> Unit
)

@Immutable
data class MenuItemUi(
    val title: String,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    device: DeviceUi = DeviceUi(
        name = "IPHONE 17 PRO MAX",
        batteryPercent = 21,
        wifiName = "lanesuu"
    ),
    today: TodayActivityUi = TodayActivityUi(
        totalMinutes = 132,
        progress = 0.32f,
        mostUsedAppName = "TikTok"
    ),
    onOpenMessages: () -> Unit = {},
    onLogout: () -> Unit = {},

    onOpenTodayActivity: () -> Unit = {},
    onOpenBrowserFilters: () -> Unit = {},
    onOpenAppLock: () -> Unit = {},
    onOpenGamesTasks: () -> Unit = {},
    onOpenNotifications: () -> Unit = {},
    onOpenApplications: () -> Unit = {},
    onOpenRequests: () -> Unit = {},
    onOpenDowntime: () -> Unit = {},
) {
    val bg = Color(0xFFF6F6F6)
    val headerLeft = Color(0xFF1E4DBA)
    val headerRight = Color(0xFFC27BFF)

    val tiles = listOf(
        QuickTileUi("Browser\nFilters", { Icon(Icons.Outlined.Public, null) }, Color(0xFFB9B1D6), onOpenBrowserFilters),
        QuickTileUi("App\nLock", { Icon(Icons.Filled.Lock, null) }, Color(0xFFCDB6FF), onOpenAppLock),
        QuickTileUi("Games/\nTasks", { Icon(Icons.Filled.VideogameAsset, null) }, Color(0xFFE5B3F1), onOpenGamesTasks),
    )

    val overviewItems = listOf(
        MenuItemUi("Notifications", { Icon(Icons.Outlined.NotificationsNone, null) }, onOpenNotifications),
        MenuItemUi("Applications", { Icon(Icons.Outlined.Extension, null) }, onOpenApplications),
    )

    val supervisionItems = listOf(
        MenuItemUi("Requests", { Icon(Icons.Outlined.ChatBubbleOutline, null) }, onOpenRequests),
        MenuItemUi("Downtime", { Icon(Icons.Outlined.Schedule, null) }, onOpenDowntime),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(bottom = 0.dp)
    ) {
        item {
            val headerShape = RoundedCornerShape(
                bottomStart = 18.dp,
                bottomEnd = 18.dp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .clip(headerShape)
                    .background(Brush.horizontalGradient(listOf(headerLeft, headerRight)))
            ) {
                HeaderRow(
                    device = device,
                    childName = device.wifiName, // ✅ placeholder for now
                    onOpenMessages = onOpenMessages,
                    onLogout = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 22.dp, vertical = 18.dp)
                )
            }
        }

        item {
            TodayActivityCard(
                today = today,
                onClick = onOpenTodayActivity,
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .offset(y = (-140).dp)
            )
        }

        val tilesOffset = (-115).dp
        item {
            QuickTilesRow(
                tiles,
                Modifier
                    .padding(horizontal = 22.dp)
                    .padding(top = 14.dp)
                    .offset(y = tilesOffset)
            )
        }

        item {
            Column(modifier = Modifier.offset(y = (-100).dp)) {
                SectionTitle("Device Overview", Modifier.padding(start = 22.dp, top = 26.dp))
                MenuCard(overviewItems, Modifier.padding(horizontal = 22.dp, vertical = 12.dp))
            }
        }

        item {
            Column(modifier = Modifier.offset(y = (-100).dp)) {
                SectionTitle("Device Supervisions", Modifier.padding(start = 22.dp, top = 18.dp))
                MenuCard(supervisionItems, Modifier.padding(horizontal = 22.dp, vertical = 12.dp))
            }
        }

        item { Spacer(Modifier.height(10.dp)) }


    }
}

@Composable
private fun HeaderRow(
    device: DeviceUi,
    childName: String,
    onOpenMessages: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var profileOpen by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = device.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "▾",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // ✅ Clock -> Battery
                    PillSmall(
                        text = "${device.batteryPercent}%",
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.BatteryFull,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    )

                    Spacer(Modifier.width(10.dp))

                    // ✅ Folder -> Person (child name)
                    PillSmall(
                        text = childName,
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    )
                }
            }

            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Messages",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onOpenMessages() }
            )

            Spacer(Modifier.width(12.dp))

            // ✅ Profile button: circle + profile icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f))
                    .clickable { profileOpen = !profileOpen },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        if (profileOpen) {
            ProfileMenuCard(
                expanded = profileOpen,
                onDismiss = { profileOpen = false },
                onLogout = {
                    profileOpen = false
                    onLogout()
                }
            )
        }
    }
}

@Composable
private fun ProfileMenuCard(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = DpOffset(x = (-24).dp, y = -(10).dp), // ✅ move menu to the RIGHT
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        shadowElevation = 14.dp,
        modifier = Modifier.border(width = 1.dp,
        color = Color(0xFFDDDDDD), // light gray border
        shape = RoundedCornerShape(16.dp)
    )
    ) {
        Text(
            text = "Account",
            modifier = Modifier.padding(start = 32.dp, top = 10.dp, bottom = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF777777)
        )

        DropdownMenuItem(

            text = {
                Text(
                    text = "Logout",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD11A2A)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = null,
                    tint = Color(0xFFD11A2A)
                )
            },
            onClick = {
                onDismiss()
                onLogout()
            }


        )
    }
}

@Composable
private fun PillSmall(
    text: String,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.20f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(14.dp)) { icon() }
        Spacer(Modifier.width(6.dp))
        Text(text = text, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun TodayActivityCard(
    today: TodayActivityUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(34.dp)
    val outline = Color(0xFFE4E4E4)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(3.dp, outline, shape)
            .clickable { onClick() }
            .padding(horizontal = 28.dp, vertical = 26.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {

            /* ---------- HEADER ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = today.label,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(14.dp)) // ✅ proper spacing
                    Text(
                        text = today.usageLabel, // "Phone Usage"
                        fontSize = 15.sp,
                        color = Color(0xFF777777)
                    )
                }

                Text(
                    text = "→",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.offset(y = (-15).dp)
                )
            }

            Spacer(Modifier.height(8.dp))
            UsageProgress(progress = today.progress)

            Spacer(Modifier.height(8.dp))
            Text(
                text = formatMinutes(today.totalMinutes),
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )

            Spacer(Modifier.height(14.dp)) // ✅ proper spacing
            Text(
                text = "Most Used App",
                fontSize = 15.sp,
                color = Color(0xFF777777)
            )

            Spacer(Modifier.height(8.dp))

            /* ---------- MOST USED APP ROW ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(28.dp) // 🔽 smaller icon
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "♪",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp // 🔽 smaller icon text
                    )
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = today.mostUsedAppName,
                    fontSize = 15.sp, // 🔽 reduced
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }
        }
    }
}




@Composable
private fun UsageProgress(progress: Float) {
    val bg = Color(0xFFE8E6F4)
    val fg = Color(0xFF6E63FF)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(999.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFFB9B1D6), fg)))
        )
    }
}

@Composable
private fun FolderGraphic(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val back = Path().apply {
            moveTo(w * 0.12f, h * 0.36f)
            quadraticTo(w * 0.10f, h * 0.18f, w * 0.28f, h * 0.18f)
            lineTo(w * 0.46f, h * 0.18f)
            quadraticTo(w * 0.52f, h * 0.18f, w * 0.56f, h * 0.26f)
            lineTo(w * 0.88f, h * 0.26f)
            quadraticTo(w * 0.96f, h * 0.26f, w * 0.96f, h * 0.36f)
            lineTo(w * 0.96f, h * 0.84f)
            quadraticTo(w * 0.96f, h * 0.96f, w * 0.84f, h * 0.96f)
            lineTo(w * 0.20f, h * 0.96f)
            quadraticTo(w * 0.08f, h * 0.96f, w * 0.08f, h * 0.84f)
            close()
        }
        drawPath(back, color = Color(0xFF6E63FF))

        val front = Path().apply {
            moveTo(w * 0.18f, h * 0.44f)
            quadraticTo(w * 0.16f, h * 0.30f, w * 0.30f, h * 0.30f)
            lineTo(w * 0.52f, h * 0.30f)
            quadraticTo(w * 0.58f, h * 0.30f, w * 0.62f, h * 0.38f)
            lineTo(w * 0.86f, h * 0.38f)
            quadraticTo(w * 0.94f, h * 0.38f, w * 0.94f, h * 0.48f)
            lineTo(w * 0.94f, h * 0.86f)
            quadraticTo(w * 0.94f, h * 0.96f, w * 0.84f, h * 0.96f)
            lineTo(w * 0.26f, h * 0.96f)
            quadraticTo(w * 0.14f, h * 0.96f, w * 0.14f, h * 0.86f)
            close()
        }
        drawPath(front, color = Color(0xFFB9B1D6))

        drawRoundRect(
            color = Color.White.copy(alpha = 0.15f),
            topLeft = Offset(w * 0.20f, h * 0.56f),
            size = Size(w * 0.42f, h * 0.10f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
        )
    }
}

@Composable
private fun QuickTilesRow(tiles: List<QuickTileUi>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        tiles.forEach { tile -> QuickTile(tile = tile, modifier = Modifier.weight(1f)) }
    }
}


@Composable
private fun QuickTile(tile: QuickTileUi, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(28.dp)
    val iconTint = Color(0xFF6E63FF).copy(alpha = 0.55f)

    Box(
        modifier = modifier
            .height(110.dp)
            .clip(shape)
            .background(tile.bg)
            .clickable { tile.onClick() }
            .padding(18.dp)
    ) {

            Text(
                text = tile.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1F1F1F),
                lineHeight = 18.sp
            )


        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(66.dp).offset(y = 20.dp), // 👈 move icon DOWN,
            contentAlignment = Alignment.Center
        ) {
            Surface(color = Color.Transparent) {
                CompositionLocalProvider(LocalContentColor provides iconTint) { tile.icon() }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(text = text, modifier = modifier, fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.Black)
}

@Composable
private fun MenuCard(items: List<MenuItemUi>, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(30.dp)
    val outline = Color(0xFFE4E4E4)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White)
            .border(3.dp, outline, shape)
            .padding(vertical = 10.dp)
    ) {
        items.forEach { item -> MenuRow(item) }
    }
}

@Composable
private fun MenuRow(item: MenuItemUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.Black) {
            Box(modifier = Modifier.size(34.dp), contentAlignment = Alignment.Center) {
                item.icon()
            }
        }

        Spacer(Modifier.width(16.dp))

        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F1F1F),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF777777),
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun HomeFooter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp), // small top
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.safesprout_logo),
            contentDescription = "SafeSprout Logo",
            modifier = Modifier.size(72.dp) // ✅ not 250
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF777777)
        )
    }
}



private fun formatMinutes(total: Int): String {
    val h = total / 60
    val m = total % 60
    return "${h} h  ${m} mins"
}
