package mx.rmr.monitorcovid19

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_datos_pais.*
import org.json.JSONArray
import org.json.JSONObject

class DatosPaisActiv : AppCompatActivity()
{
    val entries = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_pais)

        val nombrePais = intent.getStringExtra("PAIS")
        tvNombrePais.text = nombrePais

        descargarDatos(nombrePais)
        //crearGrafica()
        descargarHistorico(nombrePais)
    }

    private fun descargarHistorico(nombrePais: String?) {
        // https://corona.lmao.ninja/v2/historical/:country
        if (nombrePais!=null) {
            AndroidNetworking.get("https://corona.lmao.ninja/v2/historical/$nombrePais")
                .build()
                .getAsJSONObject(object: JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val timeline = response?.get("timeline") as JSONObject?
                        val cases = timeline?.get("cases") as JSONObject?
                        if (cases != null) {
                            var indice: Float = 0f
                            for (llave in cases.keys()) {
                                val valor = cases.get(llave) as Int?
                                if (valor!! > 0) {
                                    // Agregarlo
                                    val entrada = Entry(indice, valor+0f)
                                    entries.add(entrada)
                                    indice += 1
                                }
                            }
                            crearGrafica()
                        }
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
        }
    }

    private fun crearGrafica() {
        val vl = LineDataSet(entries, "Personas")

        vl.setDrawValues(true)
        vl.lineWidth = 3f
        lineChart.xAxis.labelRotationAngle = 45f
        lineChart.data = LineData(vl)
        lineChart.axisRight.isEnabled = true
        lineChart.description.text = "COVID19"

        lineChart.animateX(1800, Easing.EaseInSine)
    }

    private fun descargarDatos(nombrePais: String?) {
        if (nombrePais!=null) {
            AndroidNetworking.get("https://corona.lmao.ninja/v2/countries/$nombrePais")
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        println(response)
                        val casos = response?.get("cases")
                        tvCasosPais.text = "$casos"
                        val recuperados = response?.get("recovered")
                        tvRecuperadosPais.text = "$recuperados"
                        val muertes = response?.get("deaths")
                        tvMuertosPais.text = "$muertes"

                        val dInfo = response?.get("countryInfo") as JSONObject?
                        val dirBandera = dInfo?.get("flag") as String?
                        descargarBandera(dirBandera)
                    }

                    override fun onError(anError: ANError?) {
                        println("Error: ${anError?.message}")
                    }
                })
        }
    }


    fun descargarBandera(direccion: String?) {
        if (direccion != null) {
            AndroidNetworking.get(direccion)
                .build()
                .getAsBitmap(object : BitmapRequestListener {
                    override fun onResponse(response: Bitmap?) {
                        imgBandera.setImageBitmap(response)
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
        }
    }
}
