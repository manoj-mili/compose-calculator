package com.mili.simplecalculator.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mili.simplecalculator.R
import com.mili.simplecalculator.domain.model.CalculatorInput
import com.mili.simplecalculator.domain.model.Clear

@Composable
fun ClearButton(
    operatorOption: CalculatorInput.ClearInput,
    modifier: Modifier = Modifier,
    color: Color,
    onClick: (CalculatorInput.ClearInput) -> Unit
) {
    Button(
        onClick = { onClick.invoke(operatorOption) },
        modifier = modifier
            .width(72.dp)
            .height(72.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color, // Set button background color
            contentColor = Color.White // Set button text color
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        if (operatorOption.clear == Clear.Delete) {
            Icon(
                painter = painterResource(id = R.drawable.outline_backspace_24),
                contentDescription = null
            )
        } else {
            Text(
                text = operatorOption.clear.symbol,
                fontSize = 22.sp,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}
