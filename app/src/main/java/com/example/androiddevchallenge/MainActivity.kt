/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import coil.transform.CircleCropTransformation
import com.example.androiddevchallenge.ui.theme.MyTheme
import dev.chrisbanes.accompanist.coil.CoilImage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Home(navController) }
        composable("detail") { Detail(navController) }
    }
}

@Composable
fun Home(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { TopBarTitle() },
                navigationIcon = {
                    TopBarIcon(R.drawable.smile)
                })
        },
        content = {
            HomeContent(navController)
        }
    )
}

@Composable
fun HomeContent(navController: NavController) {
    val viewModel: DogViewModel = viewModel()
    val data = viewModel.result.observeAsState(initial = emptyList())
    viewModel.getDogs()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.background(MaterialTheme.colors.secondary)
    ) {
        items(data.value) { dog ->
            Card(
                modifier = Modifier.padding(5.dp),
                elevation = 5.dp,
                contentColor = MaterialTheme.colors.onSecondary
            ) {
                SimpleDog(dog = dog, onClick = {
                    navController.currentBackStackEntry?.arguments?.putParcelable("dog", dog)
                    navController.navigate("detail")
                })
            }
        }
    }
}

@Composable
fun SimpleDog(dog: Dog, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(data = dog.urls?.thumb ?: "",
            contentDescription = "",
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(5.dp),
            requestBuilder = {
                transformations(CircleCropTransformation())
            })
        Spacer(modifier = Modifier.size(20.dp))
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Name: ${dog.name ?: "..."}",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                "Age: ${dog.age ?: (1..5).random()}",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                "Likes: ${dog.likes}",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun TopBarTitle() {
    Text(
        "Dog Adopt",
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        fontSize = 20.sp
    )
}

@Composable
fun TopBarIcon(resId: Int, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .padding(15.dp)
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun Detail(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { TopBarTitle() },
                navigationIcon = {
                    TopBarIcon(R.drawable.back) { navController.navigateUp() }
                })
        },
        content = {
            DetailContent(navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}, backgroundColor = MaterialTheme.colors.primary) {
                Text("Like")
            }
        },
        backgroundColor = MaterialTheme.colors.secondary
    )
}

@Composable
fun DetailContent(navController: NavController) {
    val dog = navController.previousBackStackEntry?.arguments?.getParcelable<Dog>("dog")
    dog?.let {
        DetailDog(dog = it)
    }
}

@Composable
fun DetailDog(dog: Dog) {
    LazyColumn {
        item {
            CoilImage(
                data = dog.urls?.full ?: "",
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item { Spacer(modifier = Modifier.size(20.dp)) }
        item {
            Text(
                "Name: ${dog.name ?: "..."}",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        item { Spacer(modifier = Modifier.size(2.dp)) }
        item {
            Text(
                "Age: ${dog.age ?: (1..5).random()}",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        item { Spacer(modifier = Modifier.size(2.dp)) }
        item {
            Text(
                "Likes: ${dog.likes}",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        item { Spacer(modifier = Modifier.size(20.dp)) }
        item {
            Text(
                dog.description ?: dog.alt_description ?: "...",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}