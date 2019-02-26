package gurukul.com.googlemapsss

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesAPI {
    @GET("maps/api/place/nearbysearch/json?key=AIzaSyDdCGdR2cnWw0AB0LeN3jOTjKmkKag4tew")
    fun getPlaces(@Query("location") loc:String, @Query("type") t:String,@Query("radius") r:Int) : Call<Places>
}