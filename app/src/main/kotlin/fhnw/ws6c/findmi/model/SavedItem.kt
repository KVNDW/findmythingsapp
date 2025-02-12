package fhnw.ws6c.findmi.model

import fhnw.ws6c.findmi.data.Entities.Inside1
import fhnw.ws6c.findmi.data.Entities.Inside2
import fhnw.ws6c.findmi.data.Entities.Item
import fhnw.ws6c.findmi.data.Entities.Location

data class SavedItem(
    val item: Item,
    val location: Location,
    val inside1: Inside1,
    val inside2: Inside2

)