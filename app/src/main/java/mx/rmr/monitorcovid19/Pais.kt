package mx.rmr.monitorcovid19

class Pais (val nombre: String, val casos: Int): Comparable<Pais>
{
    override fun compareTo(other: Pais): Int {
        return nombre.compareTo(other.nombre)
    }

    companion object {
        val arrPaises = arrayOf(
            Pais("México", 10),
            Pais("España", 20)
        )
    }
}