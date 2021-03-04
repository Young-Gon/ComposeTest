package com.example.composetest

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.composetest.model.natwork.respose.Image
import com.example.composetest.ui.theme.ComposeTestTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.coil.CoilImage

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    searchbar()
                }
            }
        }
    }
}

@Composable
fun searchbar() {
    val viewModel = viewModel(SearchImageViewModel::class.java)
    // val keyword by remember { mutableStateOf(TextFieldValue()) }

    val imageList= viewModel.imageList.collectAsLazyPagingItems()
    LazyGridFor(imageList)

    val keyword by viewModel.keyword.collectAsState()
    TextField(keyword, onValueChange = {
        viewModel.searchImage(it)
    },
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth(),
    )
}

@Composable
fun LazyGridFor(imageList: LazyPagingItems<Image>) {
    LazyColumn {
        itemsIndexed(imageList) { index, image ->
            if(index%2==1){
                Row{
                    imageList[index-1]?.let { ImageItem(image = it,0) }
                    ImageItem(image = image!!,1)
                }
            }
        }
    }
}

@Composable
fun ImageItem(image: Image, columnIndex:Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth(if(columnIndex==1) 1f else 0.5f)
            .aspectRatio(1f)
    ){
        Log.d("TAG","image url=${image.thumbnail_url}")
        CoilImage(
            data = image.thumbnail_url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        ){

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTestTheme {
        searchbar()
    }
}

data class Artist(
    val name: String,
    val lastSeenOnline: String,
)