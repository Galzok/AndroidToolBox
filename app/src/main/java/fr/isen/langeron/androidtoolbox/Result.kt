package fr.isen.langeron.androidtoolbox




data class Result(
    val email: String,
    val location: Location,
    val name: Name,
    val picture: Picture
)

data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String
)

data class Name(
    val first: String,
    val last: String,
    val title: String
)

data class Location(
    val city: String,
   // val coordinates: Coordinates,
    val country: String,
    val postcode: Int,
    val state: String
   // val street: Street,
    // val timezone: Timezone
)

data class Image (
    val large: String,
    val medium: String,
    val thumbnail: String
)