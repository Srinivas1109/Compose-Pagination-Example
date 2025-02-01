package com.example.paginationexample.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.paginationexample.domain.model.Image
import com.example.paginationexample.presentation.theme.PaginationExampleTheme
import com.example.paginationexample.presentation.ui.MainViewModel
import com.example.paginationexample.presentation.ui.UiEvent
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaginationExampleTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val images: LazyPagingItems<Image> = viewModel.images.collectAsLazyPagingItems()

    KoinContext {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = {
                    Text(text = "Image Gallery")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .padding(innerPadding)
            ) {
                Spacer(modifier = modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.query,
                    onValueChange = {
                        viewModel.onEvent(UiEvent.OnQueryChange(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Search images")
                    },
                    singleLine = true,
                    maxLines = 1,
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        IconButton(onClick = {
//                            viewModel.onEvent(UiEvent.GetImages(uiState.query))
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
//                        viewModel.onEvent(UiEvent.GetImages(uiState.query))
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
                Spacer(modifier = modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        count = images.itemCount,
                        key = images.itemKey { it.uuid },
                        contentType = { images.itemContentType() }) { index ->
                        images[index]?.let { image ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(image.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = modifier
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                    }

                    when (val state = images.loadState.refresh) { //FIRST LOAD
                        is LoadState.Error -> {
                            item(span = { GridItemSpan(3) }) {
                                Box(
                                    modifier = modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Initial load error")
                                }
                            }
                        }

                        is LoadState.Loading -> { // Loading UI
                            item(span = { GridItemSpan(3) }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    Text(
                                        text = "Refreshing...",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        else -> {}
                    }

                    when (val state = images.loadState.append) { // Pagination
                        is LoadState.Error -> {
                            //TODO Pagination Error Item
                            //state.error to get error message
                        }

                        is LoadState.Loading -> { // Pagination Loading UI
                            item(span = { GridItemSpan(3) }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    Text(
                                        text = "Fetching more...",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

}