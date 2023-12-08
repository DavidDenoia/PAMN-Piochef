package com.example.trabajofinalv2

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class EditarPerfil : Fragment(R.layout.fragment_editar_perfil) {
    lateinit var publicButton: Button
    lateinit var privateButton: Button
    lateinit var backButton: ImageView
    lateinit var imageView: ImageView
    lateinit var buttonEditProfile: Button
    lateinit var guardarCambiosButton: Button
    lateinit var cambiarNombreUsuario: EditText
    lateinit var cambiarDescripcion: EditText

    // Código de solicitud para la selección de imágenes
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    // Declarar una variable para la URL de la imagen actual
    var imageUrl: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        publicButton = view.findViewById(R.id.publicVisibility)
        privateButton = view.findViewById(R.id.privateVisibility)
        backButton = view.findViewById(R.id.flecha_atras)
        imageView = view.findViewById(R.id.profile_image)
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile)
        guardarCambiosButton = view.findViewById(R.id.Guardar_cambios)
        cambiarNombreUsuario = view.findViewById(R.id.editTextUserName)
        cambiarDescripcion = view.findViewById(R.id.editTextDescription)

        // Obtiene la URL de la imagen del usuario (reemplaza con tu lógica para obtener la URL)
        val imageUrl: String? = obtenerUrlDeLaImagenDelUsuario(imageView)
        //Establecemos la imagen de perfil
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.placeholder_image)
        }

        backButton.setOnClickListener{
            findNavController().navigate(R.id.action_pantallaEditarPefil_to_pantallaMenuInferior)
        }

        // Cuando el botón público se selecciona
        publicButton.setOnClickListener {
            publicButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorSelected))
            privateButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorUnselected))
        }

        // Cuando el botón privado se selecciona
        privateButton.setOnClickListener {
            publicButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorUnselected))
            privateButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorSelected))
        }

        buttonEditProfile.setOnClickListener {
            // Abre la galería de imágenes
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        guardarCambiosButton.setOnClickListener {
            guardarImagen(imageUri, imageUrl, imageView, requireContext())

            val nuevoNombre = cambiarNombreUsuario.text.toString()
            if(nuevoNombre != ""){
                cambiarNombreUsuario(nuevoNombre, requireContext())
            }


            val nuevaDescripcion = cambiarDescripcion.text.toString()
            if(nuevaDescripcion != ""){
                cambiarDescripcion(nuevaDescripcion, requireContext())
            }


        }
    }
    // En el método onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
            imageUri = data?.data

            // Actualiza el ImageView con la nueva imagen
            imageView.setImageURI(imageUri)

            // Guarda la URL de la imagen
            imageUrl = imageUri.toString()
        }
    }
}
private fun obtenerUrlDeLaImagenDelUsuario(imageView: ImageView): String? {
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (userId != null) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)

        // Agrega un listener para obtener los datos del usuario desde la Realtime Database
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Comprueba si el usuario tiene una URL de imagen en la base de datos
                if (snapshot.hasChild("profileImageUrl")) {
                    // Obtiene la URL de la imagen del usuario
                    val imageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

                    // Utiliza la URL de la imagen para cargarla en el ImageView
                    if (imageUrl != null) {
                        Picasso.get().load(imageUrl).into(imageView)
                    } else {
                        // Si no hay URL, puedes mostrar una imagen de marcador de posición o dejar el ImageView vacío
                        // Por ejemplo, mostraremos una imagen de marcador de posición
                        imageView.setImageResource(R.drawable.placeholder_image)
                    }
                } else {
                    // Si no hay una URL de imagen en la base de datos, puedes manejarlo de acuerdo a tus necesidades
                    // Por ejemplo, mostrar una imagen de marcador de posición
                    imageView.setImageResource(R.drawable.placeholder_image)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja cualquier error que pueda ocurrir al obtener datos de la base de datos
                // Puedes agregar tu lógica de manejo de errores aquí
            }
        })
    }

    return null
}

fun guardarImagen(imageUri: Uri?, imageUrl:String?, imageView: ImageView, context: Context) {
    // Verifica si hay una imagen seleccionada
    if (imageUri != null) {
        // Guarda la información en Firebase
        Picasso.get().load(imageUrl).into(imageView)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            Picasso.get().load(imageUrl).into(imageView)
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
            val storageReference = FirebaseStorage.getInstance().reference.child("users").child(userId).child("profileImage.jpg")

            // Subir la imagen
            storageReference.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // Obtener la URL de descarga
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        val updates = hashMapOf<String, Any>(
                            "profileImageUrl" to imageUrl
                        )

                        databaseReference.updateChildren(updates)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error al guardar cambios: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

fun cambiarNombreUsuario(nuevoNombre: String, context: Context) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)

        val updates = hashMapOf<String, Any>(
            "username" to nuevoNombre
        )

        databaseReference.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Nombre de usuario actualizado exitosamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al actualizar el nombre de usuario: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

fun cambiarDescripcion(nuevaDescripcion: String, context: Context) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)

        val updates = hashMapOf<String, Any>(
            "description" to nuevaDescripcion
        )

        databaseReference.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Descripcion Actualizada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}