package mx.rmr.monitorcovid19

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), ListenerListaPais
{
    var adaptadorPaises: AdaptadorPaises? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        rvListaPaises.layoutManager = layout

        adaptadorPaises = AdaptadorPaises(this, Pais.arrPaises)
        adaptadorPaises?.listener = this
        rvListaPaises.adapter = adaptadorPaises

        AndroidNetworking.initialize(baseContext)

        // Descargar paises
        progressBar.visibility = View.INVISIBLE
        descargarPaises()
    }

    private fun descargarPaises() {
        progressBar.visibility = View.VISIBLE

        AndroidNetworking.get("https://corona.lmao.ninja/v2/countries?sort=country")
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    progressBar.visibility = View.INVISIBLE
                    if (response != null) {
                        var arrPaises = mutableListOf<Pais>()
                        for (i in 0 until response.length()) {
                            val dPais = response.get(i) as JSONObject
                            val nombrePais = dPais.getString("country")
                            val numeroCasos = dPais.getInt("cases")
                            val pais = Pais(nombrePais, numeroCasos)
                            arrPaises.add(pais)
                            arrPaises.add(0,pais)
                        }
                        adaptadorPaises?.arrPaises = arrPaises.toTypedArray()
                        adaptadorPaises?.notifyDataSetChanged()
                    }
                }

                override fun onError(anError: ANError?) {
                    println("Error: ${anError?.message}")
                    progressBar.visibility = View.INVISIBLE
                }
            })
    }

    override fun itemClicked(posicion: Int) {
        val intDatosPais = Intent(this, DatosPaisActiv::class.java)
        val pais = adaptadorPaises?.arrPaises?.get(posicion)?.nombre
        intDatosPais.putExtra("PAIS", pais)

        startActivity(intDatosPais)
    }
}
