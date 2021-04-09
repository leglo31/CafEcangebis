package com.example.cafecangebis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


class DashBoardActivity : AppCompatActivity() {

    // déclaration auteur
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
// initialisation auteur
        auth = FirebaseAuth.getInstance()

        btn_change_password.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {

        if (current_password.text.isNotEmpty() &&
                new_password.text.isNotEmpty() &&
                confirm_password.text.isNotEmpty()) {

            if (new_password.text.toString().equals(confirm_password.text.toString())) {

                val user: FirebaseUser? = auth.currentUser
                if (user != null && user.email != null) {
                    val credential: AuthCredential = EmailAuthProvider
                            .getCredential(user.email!!, current_password.text.toString())

                    user?.reauthenticate(credential)
                            ?.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this, "Ré-authentification réussie", Toast.LENGTH_SHORT).show()

                                    user?.updatePassword(new_password.text.toString())
                                            ?.addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(this, "Mot de passe changé avec succès", Toast.LENGTH_SHORT).show()
                                                    auth.signOut()
                                                    startActivity(Intent(this, LoginActivity::class.java))
                                                    finish()
                                                }
                                            }
                                } else {
                                    Toast.makeText(this, "Ré-authentification a échoué", Toast.LENGTH_SHORT).show()
                                }
                            }
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(this, "Non correspondance du mot de passe", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Rentrez tous les champs", Toast.LENGTH_SHORT).show()
        }
    }
}