package com.example.beeguide.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import okhttp3.internal.format
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.RegionViewModel

@Composable
fun RangedBeaconList(regionViewModel: RegionViewModel){
    val rangedBeaconState = regionViewModel.rangedBeacons.observeAsState(listOf())

    val rangedBeacons = rangedBeaconState.value

    Box {
        Column {
            if(rangedBeacons.isEmpty()){
                Row {
                    Text(text = "No Beacons found")
                }
            }else{
                rangedBeacons.map { beacon ->
                    RangedBeaconListElement(beacon = beacon)
                }
            }
            
        }
    }
}

@Composable
fun RangedBeaconListElement(
    beacon: Beacon,
    modifier: Modifier = Modifier
){
    Row(modifier = modifier.border(4.dp, Color.Black, RectangleShape)){
        Text(text = beacon.id1.toString() + "\n" + format("%.3f", beacon.distance))

    }
}