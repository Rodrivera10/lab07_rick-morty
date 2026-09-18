package com.rodrigorivera.lab07_moviles


    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.compose.foundation.Image


    import androidx.compose.ui.res.painterResource


    @Composable
    fun LoginScreen(onNavigateToCharacters: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween

        ){
            Spacer(modifier = Modifier.height(60.dp))

            //Logo de Rick and morty
            Image(
                painter = painterResource(id = R.drawable.rick_morty_logo),
                contentDescription = "Logo Rick and Morty",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Button(
                onClick = onNavigateToCharacters,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar", fontSize = 18.sp)
            }

            Text(
                text = "Rodrigo Rivera | 251023 | UVG",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant

            )
            }
        }


class LoginScreen {}