package gurukul.com.googlemapsss

import com.google.gson.annotations.SerializedName

data class OpeningHours(@SerializedName("open_now")
                        val openNow: Boolean = false)