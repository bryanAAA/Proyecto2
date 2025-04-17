package utils

import kotlinx.coroutines.*


object CartManager {
    private val cart = mutableMapOf<String, Int>()
    private var job: Job? = null
    private var cartStartTime: Long = 0L

    fun addItem(name: String, quantity: Int) {
        if (quantity <= 0) return
        cart[name] = quantity

        if (cartStartTime == 0L) {
            cartStartTime = System.currentTimeMillis()
            startClearTimer()
        }
    }

    fun getCart(): Map<String, Int> = cart.toMap()

    fun clearCart() {
        cart.clear()
        cartStartTime = 0L
        job?.cancel()
        job = null
    }

    fun getCantidad(nombre: String): Int {
        return cart[nombre] ?: 0
    }
    private fun startClearTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {
            delay(30 * 60 * 1000) // 30 minutos
            clearCart()
        }
    }
}