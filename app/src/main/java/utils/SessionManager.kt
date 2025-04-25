package utils

import model.Usuario

object SessionManager {
    var currentUser: Usuario? = null

    fun isLoggedIn(): Boolean = currentUser != null

    fun logout() {
        currentUser = null
    }
}