package fes.aragon.consumirservicio

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import dgtic.unam.xmljson.Card
import dgtic.unam.xmljson.VolleyAPI
import fes.aragon.consumirservicio.databinding.ActivityMainBinding
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private  var direcciones = ArrayList<String?>()
    private var listaTarjetas=ArrayList<Card>() //talvez poner ?
    private lateinit var colores:IntArray
    private var contador: Int = 0

    private lateinit var recyclerView: RecyclerView
    private var adapter:MaterialAdapter?=null

    private lateinit var binding: ActivityMainBinding
    private lateinit var volleyAPI: VolleyAPI
    private val ipPuerto="192.168.1.74:8080"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        volleyAPI=VolleyAPI(this)

        if(adapter==null) run {
            adapter = MaterialAdapter(this, listaTarjetas)
        }

        colores=resources.getIntArray(R.array.inicio_colores)
        recyclerView=findViewById(binding.recyclerView.id)
        recyclerView.adapter=adapter
        recyclerView.layoutManager= LinearLayoutManager(this)


        binding.button.setOnClickListener {
            codigoPostal()
        }
    }

    private fun codigoPostal() {
        var cod=binding.editTextText.text
        if(cod.isNotEmpty()){
            val urlJSON = "http://"+ipPuerto+"/datos/"+cod
            var cadena = ""
            val jsonRequest = object : JsonArrayRequest(
                urlJSON,
                Response.Listener<JSONArray> { response ->
                    direcciones.clear()
                    listaTarjetas.clear()
                    contador = 0
                    if(response.length()>0) {
                        (0 until response.length()).forEach {
                            val codigo = response.getJSONObject(it)
                            cadena += codigo.get("codigo").toString() + "\n"
                            cadena += codigo.get("asentamiento").toString() + "\n"
                            val mun = codigo.getJSONObject("municipio")
                            cadena += mun.get("nombre").toString() + "\n"
                            val est = mun.getJSONObject("estado")
                            cadena += est.get("estados").toString() + "\n"
                            cadena += "\n"

                            direcciones.add(cadena)
                            val card=Card()
                            card.id=contador.toLong()
                            card.direccion=direcciones[contador]
                            card.color_recurso=colores[contador]
                            listaTarjetas.add(0, card)
                            contador++
                            cadena=""
                        }
                        recyclerView.adapter?.notifyDataSetChanged()
                    }else{
                        //binding.textView2.text = getString(R.string.no_hay_datos)
                        direcciones.clear()
                        listaTarjetas.clear()
                        contador = 0
                        recyclerView.adapter?.notifyDataSetChanged()
                        showAlert("Alerta", getString(R.string.no_hay_datos))
                    }
                },
                Response.ErrorListener {
                    //binding.textView2.text = getString(R.string.error)
                    direcciones.clear()
                    listaTarjetas.clear()
                    contador = 0
                    recyclerView.adapter?.notifyDataSetChanged()
                    showAlert("Alerta", getString(R.string.error))
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                    return headers
                }
            }
            volleyAPI.add(jsonRequest)
        }else{
           // binding.textView2.text = getString(R.string.falta_cd)
            direcciones.clear()
            listaTarjetas.clear()
            contador = 0
            recyclerView.adapter?.notifyDataSetChanged()
            showAlert("Alerta", getString(R.string.falta_cd))

        }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, which ->
                // Código que se ejecuta cuando se toca el botón Aceptar
            }
            .setNegativeButton("Cancelar") { dialog, which ->
                // Código que se ejecuta cuando se toca el botón Cancelar
            }
            .show()
    }
}

