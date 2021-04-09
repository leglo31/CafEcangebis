package com.example.cafecangebis

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SignUpActivity : AppCompatActivity() {

    // déclaration auteur
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // initialisation auteur
        auth = FirebaseAuth.getInstance()

        btn_sign_up.setOnClickListener {
            signUpUser()
        }
    }
    private fun signUpUser(){
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

        // Créer un utilisateur avec e-mail et mot de passe
        auth.createUserWithEmailAndPassword(tv_username.text.toString(),tv_password.text.toString())
            .addOnCompleteListener (this) {task->
                if (task.isSuccessful) {

                    //Connexion réussie, mettre à jour l'interface utilisateur avec les informations de l'utilisateur connecté
                    val user : FirebaseUser? = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                startActivity(Intent(this,LoginActivity::class.java))
                                finish()
                            }
                        }

                }else{

                    //Si la connexion échoue, affiche un message à l'utilisateur./
                    Toast.makeText(baseContext, "Echec de l'authentification. Veuillez réessayer svp.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}

