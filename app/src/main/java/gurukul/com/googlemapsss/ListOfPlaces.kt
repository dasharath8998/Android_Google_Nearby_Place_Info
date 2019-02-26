package gurukul.com.googlemapsss

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.list_places.*
import kotlinx.android.synthetic.main.list_places.view.*

class ListOfPlaces: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.list_places,container,false)

        var temp_list = mutableListOf<String>()
        for(list in place_list!!){
            temp_list.add("Name: ${list.name}\nVicinity: ${list.vicinity}\nRatings: ${list.rating}")
        }

        v.lView.adapter = ArrayAdapter<String>(activity,R.layout.indview,temp_list)
        return v
    }

}