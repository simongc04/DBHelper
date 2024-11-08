// MainActivity.kt
package org.iesharia.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.iesharia.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.unit.Dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) { innerPadding ->
                    MainActivityContent(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun MainActivityContent(modifier: Modifier) {
    val context = LocalContext.current
    val db = DBHelper(context)

    var lName: String by remember { mutableStateOf("ID / Nombre") }
    var lAge: String by remember { mutableStateOf("Edad") }

    // Estados para los campos de entrada
    var nameValue by remember { mutableStateOf("") }
    var ageValue by remember { mutableStateOf("") }
    var idToDelete by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Base de Datos",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        Text(
            text = "Muuuuuy simple\nNombre/Edad",
            fontSize = 10.sp
        )

        // Campo Nombre
        OutlinedTextField(
            value = nameValue,
            onValueChange = { nameValue = it },
            modifier = Modifier,
            textStyle = TextStyle(color = Color.DarkGray),
            label = { Text(text = "Nombre") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )

        // Campo Edad
        OutlinedTextField(
            value = ageValue,
            onValueChange = { ageValue = it },
            modifier = Modifier,
            textStyle = TextStyle(color = Color.DarkGray),
            label = { Text(text = "Edad") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )

        // Campo ID para eliminación
        OutlinedTextField(
            value = idToDelete,
            onValueChange = { idToDelete = it },
            modifier = Modifier,
            textStyle = TextStyle(color = Color.DarkGray),
            label = { Text(text = "ID para eliminar") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )


        val buttonModifier: Modifier = Modifier.padding(10.dp)

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = buttonModifier,
                onClick = {
                    val name = nameValue
                    val age = ageValue
                    db.addName(name, age)
                    Toast.makeText(
                        context,
                        "$name adjuntado a la base de datos",
                        Toast.LENGTH_LONG
                    ).show()
                    nameValue = ""
                    ageValue = ""
                }
            ) {
                Text(text = "Añadir")
            }

            Button(
                modifier = buttonModifier,
                onClick = {
                    val cursor = db.getName()
                    lName = "ID / Nombre"
                    lAge = "Edad"
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            val id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
                            val name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl))
                            val age = cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL))
                            lName += "\n$id / $name"
                            lAge += "\n$age"
                        } while (cursor.moveToNext())
                        cursor.close()
                    }
                }
            ) {
                Text(text = "Mostrar")
            }
        }

        // Segunda fila de botones: Eliminar y Actualizar
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = buttonModifier,
                onClick = {
                    val id = idToDelete.toIntOrNull()
                    if (id != null) {
                        db.EliminarID(id)
                        Toast.makeText(
                            context,
                            "Registro con ID $id eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor ingrese una ID válida",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    idToDelete = ""
                }
            ) {
                Text(text = "Eliminar")
            }

            // Botón de Actualizar al lado del botón Eliminar
            Button(
                modifier = buttonModifier,
                onClick = {
                    // No se realiza ninguna acción cuando se hace clic
                }
            ) {
                Text(text = "Actualizar")
            }
        }




        Row {
                Text(
                    modifier = buttonModifier,
                    text = lName
                )
                Text(
                    modifier = buttonModifier,
                    text = lAge
                )
            }
        }

    }
