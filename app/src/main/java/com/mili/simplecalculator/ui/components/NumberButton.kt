package com.mili.simplecalculator.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mili.simplecalculator.domain.model.CalculatorInput

@Composable
fun NumberButton(
    numberOption: CalculatorInput.NumberInput,
    modifier: Modifier = Modifier,
    color: Color,
    onClick: (CalculatorInput.NumberInput) -> Unit
) {

    Button(
        onClick = { onClick.invoke(numberOption) },
        modifier = modifier
            .width(72.dp)
            .height(72.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color, // Set button background color
            contentColor = Color.White // Set button text color
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = numberOption.number.toInt().toString(),
            fontSize = 32.sp,
            style = MaterialTheme.typography.displayMedium
        )
    }
}