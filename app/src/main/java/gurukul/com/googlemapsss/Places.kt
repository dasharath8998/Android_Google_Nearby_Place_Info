package gurukul.com.googlemapsss

import com.google.gson.annotations.SerializedName

data class Places(@SerializedName("results")
                  val results: List<ResultsItem>?,
                  @SerializedName("status")
                  val status: String = "")