package gurukul.com.googlemapsss

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var place_list: List<ResultsItem>? = null

class MainActivity : AppCompatActivity() {

    var fManager = supportFragmentManager
    var sMap: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var status = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)

        sMap = supportFragmentManager.findFragmentById(R.id.frag1) as SupportMapFragment

        fab1.setOnClickListener {
            if (status == PackageManager.PERMISSION_GRANTED) {
                currentLocation()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    123
                )
            }
        }

        skBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        btnList.setOnClickListener {
            var r = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/").build()
            var api = r.create(PlacesAPI::class.java)

            var call: Call<Places> = api.getPlaces("${tvLat.text.toString()},${tvLongi.text.toString()}",spType.selectedItem.toString(),tvValue.text.toString().toInt())

            call.enqueue(object : Callback<Places> {

                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    var p = response.body()
                    place_list = p?.results

                    Toast.makeText(this@MainActivity, place_list?.size.toString(), Toast.LENGTH_LONG).show()

                    var tx = fManager.beginTransaction()
                    tx.replace(R.id.frag1, ListOfPlaces())
                    tx.commit()
                }

                override fun onFailure(call: Call<Places>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Get Places Failed...", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            currentLocation()
        } else {
            Toast.makeText(this@MainActivity, "Without permission we can not load your location", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun currentLocation() {
        sMap?.getMapAsync(object : OnMapReadyCallback {
            @SuppressLint("MissingPermission")
            override fun onMapReady(p0: GoogleMap?) {
                var lManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 1F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        var longi = location?.longitude
                        var lati = location?.latitude

                        tvLat.text = lati.toString()
                        tvLongi.text = longi.toString()

                        var option = MarkerOptions()
                        option.position(LatLng(lati!!, longi!!))
                        p0?.addMarker(option)
                        p0?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lati, longi), 15F))
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }
                })
            }
        })
    }
}
