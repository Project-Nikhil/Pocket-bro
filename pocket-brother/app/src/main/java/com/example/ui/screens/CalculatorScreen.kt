package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.BackgroundWarmDark
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextCream
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import kotlin.math.sqrt

@Composable
fun CalculatorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var display by remember { mutableStateOf("0") }
    var previousValue by remember { mutableStateOf<Double?>(null) }
    var pendingOp by remember { mutableStateOf<String?>(null) }
    var shouldClearOnNextDigit by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableStateOf("Basic") } // Basic, GPA, Grade

    // GPA inputs
    var marksTotal by remember { mutableStateOf("450") }
    var marksMax by remember { mutableStateOf("500") }
    var calculatedPercentage by remember { mutableStateOf("90.0%") }

    fun onDigit(d: String) {
        if (display == "0" || shouldClearOnNextDigit) {
            display = d
            shouldClearOnNextDigit = false
        } else {
            display += d
        }
    }

    fun onOp(op: String) {
        val current = display.toDoubleOrNull() ?: 0.0
        previousValue = current
        pendingOp = op
        shouldClearOnNextDigit = true
    }

    fun onEquals() {
        val current = display.toDoubleOrNull() ?: 0.0
        val prev = previousValue
        if (prev != null && pendingOp != null) {
            val result = when (pendingOp) {
                "+" -> prev + current
                "-" -> prev - current
                "×" -> prev * current
                "÷" -> if (current != 0.0) prev / current else 0.0
                "%" -> prev * (current / 100.0)
                else -> current
            }
            display = if (result % 1.0 == 0.0) result.toInt().toString() else "%.4f".format(result).trimEnd('0').trimEnd('.')
            previousValue = null
            pendingOp = null
            shouldClearOnNextDigit = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWarmDark)
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("calc_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextCream
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Calculator & Utilities",
                color = TextCream,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1.3f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs: Basic, Grade & GPA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCardDark)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Basic", "GPA & Grade").forEach { tab ->
                val isSelected = selectedTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) AccentAmber else Color.Transparent)
                        .clickable { selectedTab = tab }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = if (isSelected) Color(0xFF261405) else TextMuted,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (selectedTab == "Basic") {
            // Calculator Screen Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceCardDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = display,
                    color = TextCream,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calculator Keypad
            val buttons = listOf(
                listOf("C", "√", "%", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "±", "=")
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                buttons.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        row.forEach { btn ->
                            val isOp = btn in listOf("÷", "×", "-", "+", "=")
                            val isSpecial = btn in listOf("C", "√", "%", "±")

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        when {
                                            btn == "=" -> AccentAmber
                                            isOp -> Color(0xFF3B2E1E)
                                            isSpecial -> SurfaceCardElevated
                                            else -> SurfaceCardDark
                                        }
                                    )
                                    .border(0.8.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                                    .clickable {
                                        when (btn) {
                                            "C" -> {
                                                display = "0"
                                                previousValue = null
                                                pendingOp = null
                                            }
                                            "=" -> onEquals()
                                            "√" -> {
                                                val v = display.toDoubleOrNull() ?: 0.0
                                                display = if (v >= 0) "%.4f".format(sqrt(v)).trimEnd('0').trimEnd('.') else "Error"
                                                shouldClearOnNextDigit = true
                                            }
                                            "±" -> {
                                                val v = display.toDoubleOrNull() ?: 0.0
                                                display = (-v).toString().removeSuffix(".0")
                                            }
                                            in listOf("+", "-", "×", "÷", "%") -> onOp(btn)
                                            "." -> if (!display.contains(".")) display += "."
                                            else -> onDigit(btn)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = btn,
                                    color = if (btn == "=") Color(0xFF261405) else if (isOp) AccentAmber else TextCream,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // GPA & Percentage Calculator Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceCardDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Percentage & GPA Calculator", color = AccentAmber, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                OutlinedTextField(
                    value = marksTotal,
                    onValueChange = { marksTotal = it },
                    label = { Text("Marks Obtained") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream)
                )
                OutlinedTextField(
                    value = marksMax,
                    onValueChange = { marksMax = it },
                    label = { Text("Maximum Marks") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextCream, unfocusedTextColor = TextCream)
                )
                Button(
                    onClick = {
                        val obt = marksTotal.toDoubleOrNull() ?: 0.0
                        val max = marksMax.toDoubleOrNull() ?: 1.0
                        val pct = if (max > 0) (obt / max) * 100.0 else 0.0
                        val gpa = (pct / 10.0).coerceAtMost(10.0)
                        calculatedPercentage = "%.1f%%  (GPA: %.2f / 10)".format(pct, gpa)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate", color = Color(0xFF261405), fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceCardElevated)
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Result: $calculatedPercentage",
                        color = AccentAmber,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
