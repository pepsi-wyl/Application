package site.ylan.application.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface TaskNavKey : NavKey {
    @Serializable
    data object TaskList : TaskNavKey

    @Serializable
    data class AddEditTask(val taskId: String? = null) : TaskNavKey
}
