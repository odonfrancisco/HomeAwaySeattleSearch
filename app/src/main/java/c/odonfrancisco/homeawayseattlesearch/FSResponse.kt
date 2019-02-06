package c.odonfrancisco.homeawayseattlesearch

import com.google.gson.annotations.SerializedName

data class FSResponse(@SerializedName("response") var venues: Venues)

data class Venues(val venues: List<Place>)