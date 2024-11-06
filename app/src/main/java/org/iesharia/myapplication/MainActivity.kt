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

        // Nombre
        var nameValue by remember { mutableStateOf("") }
        OutlinedTextField(
            value = nameValue,
            onValueChange = { nameValue = it },
            modifier = Modifier,
            textStyle = TextStyle(color = Color.DarkGray),
            label = { Text(text = "Nombre") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )

        // Edad
        var ageValue by remember { mutableStateOf("") }
        OutlinedTextField(
            value = ageValue,
            onValueChange = { ageValue = it },
            modifier = Modifier,
            textStyle = TextStyle(color = Color.DarkGray),
            label = { Text(text = "Edad") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )

        val buttonModifier: Modifier = Modifier.padding(20.dp)
        Row {
            // Botón Añadir
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

            // Botón Mostrar
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

            Button(
                modifier = buttonModifier,
                onClick = {
                    db.deleteAllNames()
                    Toast.makeText(context, "Todos los registros eliminados", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(text = "Eliminar",
                    fontSize = 9.sp

                )

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
