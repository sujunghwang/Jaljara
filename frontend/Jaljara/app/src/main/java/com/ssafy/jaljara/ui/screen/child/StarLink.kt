package com.ssafy.jaljara.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.screen.child.ChildApp

@Composable
fun StarLink(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "icon")
            Text(text = "바른 수면 별자리", fontSize = 40.sp)
        }
        Text(
            text = "별자리 들어갈 곳!",
            modifier = Modifier.padding(top = 150.dp, bottom = 150.dp)
        )
        Text(
            text = "0 / 7",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        Button(
            onClick = {},
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(Color.Magenta),
            modifier = Modifier.wrapContentSize().padding(top = 16.dp, bottom = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_card_giftcard_24),
                contentDescription = "present drawable",
                modifier = Modifier.size(36.dp)
            )
        }
        Button(onClick = {},
            contentPadding = PaddingValues(12.dp),
            enabled = true,
            colors = ButtonDefaults.buttonColors(Color.Blue),
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
        ) {
            Text(text = "보상 획득")
        }
    }

}

@Composable
@Preview(showSystemUi = true)
fun preview(){
    StarLink()
}