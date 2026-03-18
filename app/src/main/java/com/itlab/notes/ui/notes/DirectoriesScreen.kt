package com.itlab.notes.ui.notes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class DirectoryItemUi(
    val name: String,
    val noteCount: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectoriesScreen(
    directories: List<DirectoryItemUi> = previewDirectoriesFallback(),
    onDirectoryClick: (DirectoryItemUi) -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Directories", color = colors.onSurface) },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = colors.onSurface)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(horizontal = 16.dp)) {
            items(directories) { dir ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDirectoryClick(dir) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Stars,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = dir.name,
                        color = colors.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        color = colors.surfaceVariant,
                        shape = CircleShape
                    ) {
                        Text(
                            text = dir.noteCount.toString(),
                            color = colors.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = colors.onSurfaceVariant)
                }
            }
        }
    }
}
private fun previewDirectoriesFallback(): List<DirectoryItemUi> =
    listOf(
        DirectoryItemUi(name = "All Notes", noteCount = 0),
        DirectoryItemUi(name = "My Study", noteCount = 0),
        DirectoryItemUi(name = "How to Cook", noteCount = 0),
        DirectoryItemUi(name = "My poems", noteCount = 0),
    )