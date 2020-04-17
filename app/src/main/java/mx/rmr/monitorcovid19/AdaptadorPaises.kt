package mx.rmr.monitorcovid19

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_datos_pais.view.*
import kotlinx.android.synthetic.main.renglon_pais.view.*
import kotlinx.android.synthetic.main.renglon_pais.view.imgBandera

class AdaptadorPaises (private val contexto: Context, var arrPaises: Array<Pais>) : RecyclerView.Adapter<AdaptadorPaises.RenglonMateria>()
{
    var listener: ListenerListaPais? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RenglonMateria {
        val vista = LayoutInflater.from(contexto).inflate(R.layout.renglon_pais, parent, false)
        return RenglonMateria(vista)
    }

    override fun getItemCount(): Int {
        return arrPaises.size
    }

    override fun onBindViewHolder(holder: RenglonMateria, position: Int) {
        val pais = arrPaises[position]
        holder.vistaPais.tvPais.text = pais.nombre
        holder.vistaPais.tvCasos.text = "${pais.casos}"
        holder.vistaPais.imgBandera.setImageResource(R.drawable.flag)

        // Listener
        holder.vistaPais.setOnClickListener {
            listener?.itemClicked(position)
        }
    }

    inner class RenglonMateria (var vistaPais: View) : RecyclerView.ViewHolder(vistaPais)
    {

    }
}