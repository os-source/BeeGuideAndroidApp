package com.example.beeguide.ui.screens.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beeguide.R
import com.example.beeguide.network.AuthResult
import com.example.beeguide.ui.components.BeeGuidePasswordField
import com.example.beeguide.ui.components.BeeGuideTextField
import com.example.beeguide.ui.viewmodels.SignInViewModel
import com.example.beeguide.ui.viewmodels.UserViewModel

@Composable
fun SignInScreen(
    onSignUpButtonClicked: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    userViewModel: UserViewModel,
    signInViewModel: SignInViewModel = viewModel(factory = SignInViewModel.Factory)
) {
    val context = LocalContext.current

    LaunchedEffect(signInViewModel, context) {
        signInViewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    userViewModel.fetchUser()
                    navigateToHomeScreen()
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(context, "Du konntest nicht angemeldet werden!", Toast.LENGTH_SHORT).show()
                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(context, "Ein unbekannter Fehler ist aufgetreten!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.size(20.dp))

        Image(
            painter = painterResource(id = R.drawable.undraw_sign_in_re_o58h),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(100.dp, 50.dp)
                .alpha(0.5f),
        )

        Text(
            text = stringResource(id = R.string.sign_in),
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.size(30.dp))

        BeeGuideTextField(
            value = signInViewModel.signInUiState.email,
            onValueChange = { signInViewModel.emailChanged(it) },
            label = "E-Mail",
            icon = Icons.Rounded.Email,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(20.dp))

        BeeGuidePasswordField(
            value = signInViewModel.signInUiState.password,
            onValueChange = { signInViewModel.passwordChanged(it) },
            submit = { signInViewModel.signIn() },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(20.dp))

        Button(onClick = { signInViewModel.signIn() }) {
            Text(text = stringResource(id = R.string.sign_in))
        }

        Spacer(modifier = Modifier.size(20.dp))

        Row {
            Text(
                text = "${stringResource(id = R.string.no_profile_yet)} ",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
            Text(
                text = stringResource(id = R.string.sign_up),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                modifier = Modifier.clickable(onClick = onSignUpButtonClicked),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}