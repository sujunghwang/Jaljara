package com.ssafy.jaljara.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.R

@Composable
fun ParentMain(){
    LazyColumn(content = {
        items(1000) { index ->
            Text(text = "부모 메인 실험 Item: $index")
        }
    })

}

@Composable
fun ParentMainView(){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Children()
        CurrentRewardContainer()
        CurrentRewardContainer()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Children(){
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        shape = RoundedCornerShape(12.dp),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp,60.dp)
                )
                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp,60.dp)
                )
                Image(painter = painterResource(id = R.drawable.ic_person_add),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp,50.dp)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentRewardContainer() {
    var reward by remember { mutableStateOf("놀이동산 가기") }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        shape = RoundedCornerShape(12.dp),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
//                        modifier = Modifier.size(60.dp, 60.dp)
                )
                Column() {
                    Text(text = "현재 보상")
                    Text(text = "$reward", fontSize = 20.sp)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildSetTimeCard(modifier: Modifier = Modifier) {

    var wakeupTime by remember { mutableStateOf("5:00") }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        content = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "$wakeupTime", fontSize = 20.sp)
                Text(text = "Wake Up")
                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.End)
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun ParentMainScreenView() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Children()
        CurrentRewardContainer()
        CurrentRewardContainer()
        Row(modifier = Modifier.fillMaxWidth()) {
            ChildSetTimeCard(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(0.1f))
            ChildSetTimeCard(Modifier.weight(1f))
        }
    }
}