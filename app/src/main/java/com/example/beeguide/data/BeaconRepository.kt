package com.example.beeguide.data
import org.altbeacon.beacon.Beacon

import java.util.UUID

class BeaconRepository {
    private val beaconRanges: MutableMap<Int, Beacon> = mutableMapOf()

    fun getCurrentRanges(): Map<Int, Beacon> {
        return beaconRanges
    }

    fun getRangeOf(id: Int): Beacon? {
        return beaconRanges[id]
    }
}