package com.example.cafecangebis

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {

    // déclaration auteur
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialisation auteur
        auth = FirebaseAuth.getInstance()

        btn_sign_up.setOnClickListener {
            startActivity(Intent (this, SignUpActivity::class.java))
            finish()
        }

        btn_log_in.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        if (tv_username.text.toString().isEmpty()){
            tv_username.error = "Entrez email"
            tv_username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(tv_username.text.toString()).matches()){
            tv_username.error = "Entrez email correct"
            tv_username.requestFocus()
            return
        }

        if(tv_password.text.toString().isEmpty()){
            tv_password.error = "Entrez mot de passe"
            tv_password.requestFocus()
            return
        }

        // Se connecter avec un email et mot de passe
        auth.signInWithEmailAndPassword(tv_username.text.toString(),tv_password.text.toString() )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Connexion réussie, mise à jour de l'interface utilisateur avec les informations de l'utilisateur connecté
                    val user : FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    // Si la connexion échoue, affiche un message à l'utilisateur.
                    Toast.makeText(baseContext, "Echec connexion",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

            }
    }

    public override fun onStart(){
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(currentUser: FirebaseUser?){

        if (currentUser != null){
            if (currentUser.isEmailVerified) {
                startActivity(Intent(this, DashBoardActivity::class.java))
                finish()

            }else {
                // Si l'email erroné, affiche message à l'utilisateur
                Toast.makeText(baseContext, "Vérifiez votre email", Toast.LENGTH_SHORT).show()
            }

        }else{
            // Si la connexion échoue, affiche un message à l'utilisateur.
            Toast.makeText(baseContext, "Echec connexion", Toast.LENGTH_SHORT).show()
        }
    }
}


