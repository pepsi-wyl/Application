package site.ylan.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.ylan.application.model.Priority
import site.ylan.application.ui.theme.ApplicationTheme

@Composable
fun PriorityChip(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, label) = when (priority) {
        Priority.HIGH -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            "HIGH"
        )
        Priority.MEDIUM -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "MEDIUM"
        )
        Priority.LOW -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "LOW"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    val priorities = Priority.entries.toTypedArray()

    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        priorities.forEachIndexed { index, priority ->
            val isSelected = priority == selectedPriority
            SegmentedButton(
                selected = isSelected,
                onClick = { onPrioritySelected(priority) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = priorities.size),
                icon = {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
            ) {
                Text(
                    text = when (priority) {
                        Priority.LOW -> "Low"
                        Priority.MEDIUM -> "Medium"
                        Priority.HIGH -> "High"
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriorityChipPreview() {
    ApplicationTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            PriorityChip(priority = Priority.LOW, modifier = Modifier.padding(end = 8.dp))
            PriorityChip(priority = Priority.MEDIUM, modifier = Modifier.padding(end = 8.dp))
            PriorityChip(priority = Priority.HIGH)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrioritySelectorPreview() {
    ApplicationTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            PrioritySelector(
                selectedPriority = Priority.MEDIUM,
                onPrioritySelected = {}
            )
        }
    }
}
