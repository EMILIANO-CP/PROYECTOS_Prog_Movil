package fes.aragon.consumirservicio

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dgtic.unam.xmljson.Card

class MaterialAdapter(private val context: Context, private val listaTarjetas: ArrayList<Card>) :
    RecyclerView.Adapter<MaterialAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var inicial: TextView
        var direccionTarjeta: TextView
        var imagenView: ImageView
        var card: CardView
        init {
            inicial = view.findViewById(R.id.initial)
            direccionTarjeta = view.findViewById(R.id.direccion_tarjeta)
            imagenView = view.findViewById(R.id.image_view)
            card = view.findViewById(R.id.card_layout)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = LayoutInflater.from(parent.context)
        val v = li.inflate(R.layout.card_view_holder, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val direccion: String? = listaTarjetas[position].direccion
        val color: Int = listaTarjetas[position].color_recurso
        val id_contador: Long = listaTarjetas[position].id
        val inicialCaracter: TextView = holder.inicial
        val direccionTexView: TextView = holder.direccionTarjeta
        val imagen: ImageView = holder.imagenView
        inicialCaracter.setBackgroundColor(color)
        inicialCaracter.text = id_contador.toString()//direccion?.substring(0, 1) //cambiar a nulo o codigo postal
        direccionTexView.text = direccion
        imagen.setImageResource(R.drawable.img)
        holder.card.setOnClickListener {
            Toast.makeText(context, "Carta " + direccion, Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int {
        return if (listaTarjetas.isEmpty()) {
            0
        } else {
            listaTarjetas.size
        }
    }
    override fun getItemId(position: Int): Long {
        return listaTarjetas[position].id
    }
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        animateCircularReveal(holder.itemView)
    }
    private fun animateCircularReveal(view: View) {
        val centroX = 0
        val centerY = 0
        val inicioRadius = 0.0f
        val finRadius = kotlin.math.max(view.width, view.height)
        val animacion: Animator = ViewAnimationUtils.createCircularReveal(
            view,
            centroX,
            centerY,
            inicioRadius,
            finRadius.toFloat()
        )
        view.visibility = View.VISIBLE
        animacion.start()
    }
}