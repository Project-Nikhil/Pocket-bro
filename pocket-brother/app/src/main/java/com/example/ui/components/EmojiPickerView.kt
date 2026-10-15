package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle

fun insertEmojiAtCursor(current: TextFieldValue, emoji: String): TextFieldValue {
    val text = current.text
    val selection = current.selection
    val start = selection.min.coerceIn(0, text.length)
    val end = selection.max.coerceIn(0, text.length)
    val newText = text.replaceRange(start, end, emoji)
    val newCursor = start + emoji.length
    return TextFieldValue(text = newText, selection = TextRange(newCursor))
}

enum class EmojiCategory(val label: String, val iconEmoji: String) {
    RECENT("Recent", "🕒"),
    SMILEYS("Smileys", "😀"),
    PEOPLE("Gestures", "🫂"),
    HEARTS("Hearts", "💖"),
    FOOD("Food", "🍕"),
    ANIMALS("Nature", "🐱"),
    ACTIVITIES("Fun", "🎮")
}

private val EMOJI_DATABASE = mapOf(
    EmojiCategory.SMILEYS to listOf(
        "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "🥹", "😊",
        "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😗", "😋",
        "😛", "😜", "🤪", "😝", "🤑", "🤗", "🤭", "🤫", "🤔", "🫡",
        "🤐", "🤨", "😐", "😑", "😶", "😏", "😒", "🙄", "😬", "🤥",
        "😌", "😔", "😪", "🤤", "😴", "😷", "🤒", "🤕", "🤢", "🤮",
        "🤧", "🥵", "🥶", "🥴", "😵", "🤯", "🤠", "🥳", "🥸", "😎",
        "🤓", "🧐", "😕", "🫤", "😟", "🙁", "😮", "😯", "😲", "😳",
        "🥺", "🥹", "😦", "😧", "😨", "😰", "😥", "😢", "😭", "😱"
    ),
    EmojiCategory.PEOPLE to listOf(
        "👋", "🤚", "🖐️", "✋", "🖖", "👌", "🤌", "🤏", "✌️", "🤞",
        "🫰", "🤟", "🤘", "🤙", "👈", "👉", "👆", "🖕", "👇", "☝️",
        "👍", "👎", "✊", "👊", "🤛", "🤜", "👏", "🙌", "🫶", "👐",
        "🤲", "🤝", "🙏", "✍️", "💅", "🤳", "💪", "🫂", "👶", "👧",
        "👦", "👩", "🧑", "👨", "👩‍🦰", "👨‍🦰", "👩‍🦱", "👨‍🦱", "👩‍🦳", "👨‍🦳"
    ),
    EmojiCategory.HEARTS to listOf(
        "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔",
        "❤️‍🔥", "❤️‍🩹", "❣️", "💕", "💞", "💓", "💗", "💖", "💘", "💝",
        "💟", "💌", "🫶", "✨", "⭐", "🌟", "💫", "🔥", "💯", "🎉"
    ),
    EmojiCategory.FOOD to listOf(
        "🍕", "🍔", "🍟", "🌭", "🍿", "🧈", "🧇", "🥞", "🧀", "🥖",
        "🍜", "🍲", "🍛", "🍚", "🍙", "🍘", "🍢", "🍣", "🍤", "🥟",
        "🍦", "🍧", "🍨", "🍩", "🍪", "🎂", "🍰", "🧁", "🍫", "🍬",
        "🍭", "🍮", "🍯", "☕", "🫖", "🍵", "🧃", "🥤", "🧋", "🫗"
    ),
    EmojiCategory.ANIMALS to listOf(
        "🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐻‍❄️", "🐨",
        "🐯", "🦁", "🐮", "🐷", "🐸", "🐵", "🐔", "🐧", "🐦", "🐤",
        "🌸", "💮", "🏵️", "🌹", "🥀", "🌺", "🌻", "🌼", "🌷", "🌱",
        "☀️", "🌤️", "⛅", "🌧️", "⛈️", "🌩️", "❄️", "🌙", "⭐", "🌈"
    ),
    EmojiCategory.ACTIVITIES to listOf(
        "🎮", "🕹️", "🎲", "🧩", "🎯", "🎳", "🎧", "🎤", "📱", "💻",
        "📚", "📖", "✏️", "📝", "🎒", "🎓", "⏰", "⏱️", "⏳", "💤",
        "⚽", "🏀", "🏈", "⚾", "🎾", "🏸", "🥊", "🥋", "🏆", "🥇"
    )
)

@Composable
fun EmojiPickerView(
    recentEmojis: List<String>,
    onEmojiSelected: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(if (recentEmojis.isNotEmpty()) EmojiCategory.RECENT else EmojiCategory.SMILEYS) }
    var searchQuery by remember { mutableStateOf("") }

    val displayedEmojis = remember(selectedCategory, searchQuery, recentEmojis) {
        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            EMOJI_DATABASE.values.flatten().distinct().filter {
                // Search by query or matching context
                it.contains(q) || matchesEmojiKeyword(it, q)
            }
        } else if (selectedCategory == EmojiCategory.RECENT) {
            recentEmojis.ifEmpty { EMOJI_DATABASE[EmojiCategory.SMILEYS] ?: emptyList() }
        } else {
            EMOJI_DATABASE[selectedCategory] ?: emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(SurfaceDark)
            .border(width = 0.8.dp, color = SurfaceBorder)
    ) {
        // Search bar & Close button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceCardDark)
                    .border(0.6.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search emoji...",
                                color = TextSubtle,
                                fontSize = 12.5.sp
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(color = TextCream, fontSize = 13.sp),
                            cursorBrush = SolidColor(AccentAmber),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = onClose,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Picker",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Category Tabs
        if (searchQuery.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                EmojiCategory.entries.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) AccentAmber.copy(alpha = 0.2f) else Color.Transparent)
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                color = if (isSelected) AccentAmber else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = cat.iconEmoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = cat.label,
                                color = if (isSelected) AccentAmber else TextMuted,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Emoji Grid
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 42.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(displayedEmojis) { emoji ->
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable { onEmojiSelected(emoji) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 24.sp)
                }
            }
        }
    }
}

private fun matchesEmojiKeyword(emoji: String, query: String): Boolean {
    val q = query.lowercase()
    return when {
        q in "smile happy joy laugh" && emoji in "😀😃😄😁😆😅😂🤣😊" -> true
        q in "love heart cute" && emoji in "❤️🥰😍💖💕💓🫶" -> true
        q in "sad cry tears down hurt" && emoji in "🥺😭😢😥💔" -> true
        q in "food eat hungry pizza burger" && emoji in "🍕🍔🍟🍜🍦🍫☕" -> true
        q in "fire lit hot cool" && emoji in "🔥😎✨💯" -> true
        q in "hand bye wave hi hug" && emoji in "👋🫂👍🙌" -> true
        q in "cat dog animal" && emoji in "🐱🐶🐼🦊🐻" -> true
        q in "sleep tired yawn night" && emoji in "😴🥱😪💤🌙" -> true
        else -> false
    }
}
