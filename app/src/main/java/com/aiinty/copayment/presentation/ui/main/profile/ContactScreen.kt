package com.aiinty.copayment.presentation.ui.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.model.ContactWithMaskedCard
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.BaseTextField
import com.aiinty.copayment.presentation.ui._components.profile.ContactProfileItem
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.ui.theme.Greyscale500
import com.aiinty.copayment.presentation.viewmodels.ProfileUiState
import com.aiinty.copayment.presentation.viewmodels.ProfileViewModel

@Composable
fun ContactScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    when(val state = viewModel.uiState.value) {
        is ProfileUiState.Loading -> LoadingScreen(modifier)
        is ProfileUiState.Error -> ErrorScreen(modifier)
        is ProfileUiState.Success -> if (state.contacts != null) ContactScreenContent(
            modifier,
            state.contacts
        ) else viewModel.getContacts()
    }
}

@Composable
private fun ContactScreenContent(
    modifier: Modifier,
    contacts: List<ContactWithMaskedCard>
) {
    val searchQuery = remember { mutableStateOf("") }

    val filteredContacts = contacts.filter {
        it.contact.profile.fullName.contains(searchQuery.value, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            BaseTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                placeholder = { Text(stringResource(R.string.search)) },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.search),
                        contentDescription = stringResource(R.string.search)
                    )
                }
            )
        }

        item {
            Text(
                text = stringResource(R.string.all_contacts),
                color = Greyscale500,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        if (filteredContacts.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.no_contacts_found),
                    color = Greyscale500,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        } else {
            items(filteredContacts) { item ->
                ContactProfileItem(
                    contact = item.contact,
                    cardNumber = item.maskedCard
                )
            }
        }
    }
}

fun NavGraphBuilder.contactScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.ContactScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: ProfileViewModel = hiltViewModel(parentEntry)
        ContactScreen(
            modifier = modifier,
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactScreenPreview() {
    ContactScreen(
        Modifier.fillMaxSize()
    )
}