package com.aiinty.copayment.presentation.ui.main.profile

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.utils.FileUtils.uriToFile
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.auth.EmailTextField
import com.aiinty.copayment.presentation.ui._components.auth.FullNameTextField
import com.aiinty.copayment.presentation.ui._components.auth.PhoneTextField
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.profile.ProfileAvatar
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.viewmodels.CardViewModel
import com.aiinty.copayment.presentation.viewmodels.ProfileUiState
import com.aiinty.copayment.presentation.viewmodels.ProfileViewModel
import java.io.File

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    UiErrorHandler(viewModel = viewModel)
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }
    when(val state = viewModel.uiState.value) {
        is ProfileUiState.Loading -> LoadingScreen(modifier)
        is ProfileUiState.Error -> ErrorScreen(modifier)
        is ProfileUiState.Success -> EditProfileContent(
            modifier,
            state.profile,
            viewModel
        )
    }
}

@Composable
private fun EditProfileContent(
    modifier: Modifier,
    profile: Profile,
    viewModel: ProfileViewModel,
) {
    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val imageFile = remember { mutableStateOf<File?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri.value = uri
        uri?.let {
            imageFile.value = uriToFile("avatar", ".png", it, context)
        }
    }

    val fullName = remember { mutableStateOf(profile.fullName) }
    val fullNameError = remember { mutableStateOf<Int?>(null) }
    val email = remember { mutableStateOf(profile.email) }
    val emailError = remember { mutableStateOf<Int?>(null) }
    val phone = remember { mutableStateOf(profile.phone ?: "") }
    val phoneError = remember { mutableStateOf<Int?>(null) }
    val isInputsValidated = fullNameError.value == null && emailError.value == null &&
            phoneError.value == null && fullName.value.isNotEmpty()
            && email.value.isNotEmpty() && phone.value.isNotEmpty()

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                ProfileAvatar(
                    avatarUrl = profile.fullAvatarUrl,
                    localImageUri = selectedImageUri.value,
                    onClick = {
                        imagePickerLauncher.launch("image/*")
                    }
                )
            }

            EditProfileFields(
                fullName = fullName,
                fullNameError = fullNameError,
                phone = phone,
                phoneError = phoneError,
                email = email,
                emailError = emailError
            )
        }

        BaseButton(
            onClick = {
                if (isInputsValidated) {
                    val newProfile = Profile(
                        id = profile.id,
                        fullName = fullName.value,
                        email = email.value,
                        phone = phone.value,
                        avatarUrl = profile.avatarUrl
                    )

                    viewModel.updateProfile(
                        newProfile,
                        newAvatarFile = imageFile.value
                    )
                }
            },
            enabled = isInputsValidated
        ) {
            Text(
                text = stringResource(R.string.save),
                fontSize = 16.sp,
                fontWeight = FontWeight.W700,
                color = Color.White
            )
        }
    }
}

@Composable
private fun EditProfileFields(
    modifier: Modifier = Modifier,
    fullName: MutableState<String>,
    fullNameError: MutableState<Int?>,
    phone: MutableState<String>,
    phoneError: MutableState<Int?>,
    email: MutableState<String>,
    emailError: MutableState<Int?>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FullNameTextField(
            modifier = Modifier.fillMaxWidth(),
            fullName = fullName.value,
            onFullNameChange = { fullName.value=it },
            onValidationResultChange={ error ->
                fullNameError.value=error
            }
        )

        PhoneTextField(
            modifier = Modifier.fillMaxWidth(),
            phone = phone.value,
            onPhoneChange = { phone.value = it },
            onValidationResultChange = { error ->
                phoneError.value=error
            }
        )

        EmailTextField(
            modifier = Modifier.fillMaxWidth(),
            email = email.value,
            onEmailChange = { email.value = it },
            onValidationResultChange = { error ->
                emailError.value=error
            }
        )
    }
}

fun NavGraphBuilder.editProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.EditProfileScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: ProfileViewModel = hiltViewModel(parentEntry)
        EditProfileScreen(
            modifier = modifier,
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(
        Modifier.fillMaxSize()
    )
}
