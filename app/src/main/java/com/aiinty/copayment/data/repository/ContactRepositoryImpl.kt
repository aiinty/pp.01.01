package com.aiinty.copayment.data.repository

import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.contact.ContactInsertRequest
import com.aiinty.copayment.data.network.ContactApi
import com.aiinty.copayment.domain.model.Contact
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.repository.ContactRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val api: ContactApi,
    private val gson: Gson,
    private val prefs: UserPreferences,
) :  BaseRepositoryImpl(gson), ContactRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val bearerToken = { "Bearer ${prefs.getAccessToken()}" }

    override suspend fun getContacts(userId: String, range: String): Result<List<Contact>> {
        return withContext(ioDispatcher) {
            val response = api.getContacts(
                userId = "eq.$userId",
                authHeader = bearerToken.invoke(),
                range = range
            )

            handleApiResponse(response).fold(
                onSuccess = { contactsList ->
                    val contacts: List<Contact> = contactsList.map { contact ->
                        Contact(
                            id = contact.id,
                            userId = contact.user_id,
                            profile = Profile(
                                id = contact.profiles.id,
                                email = "",
                                phone = contact.profiles.phone,
                                fullName = contact.profiles.full_name,
                                avatarUrl = contact.profiles.avatar_url
                            ),
                            cardId = contact.card_id
                        )
                    }
                    Result.success(contacts)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        }
    }

    override suspend fun insertContact(contact: Contact): Result<Unit> {
        return withContext(ioDispatcher) {
            val requestBody = ContactInsertRequest(
                user_id = contact.userId,
                contact_user_id = contact.profile.id,
                card_id = contact.cardId
            )

            val response = api.insertContact(
                authHeader = bearerToken.invoke(),
                body = requestBody
            )
            handleEmptyResponse(response)
        }
    }

}
