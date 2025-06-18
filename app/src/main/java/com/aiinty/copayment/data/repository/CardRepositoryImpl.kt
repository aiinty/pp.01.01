package com.aiinty.copayment.data.repository

import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.card.CardInsertRequest
import com.aiinty.copayment.data.model.card.CardsResponse
import com.aiinty.copayment.data.model.card.MaskedCardRequest
import com.aiinty.copayment.data.model.card.MaskedCardResponse
import com.aiinty.copayment.data.network.CardApi
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.domain.repository.CardRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val api: CardApi,
    private val gson: Gson,
    private val prefs: UserPreferences,
) :  BaseRepositoryImpl(gson), CardRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val bearerToken = { "Bearer ${prefs.getAccessToken()}" }

    override suspend fun getCards(userId: String): Result<List<Card>> {
        return withContext(ioDispatcher) {
            val response = api.getCards(
                userId = "eq.$userId",
                authHeader = bearerToken.invoke()
            )

            handleApiResponse(response).fold(
                onSuccess = { cardsList ->
                    val cards: List<Card> = cardsList
                        .filter { card -> card.id != null }.map { card ->
                            Card(
                                id = card.id!!,
                                userId = card.user_id,
                                cardNumber = card.card_number,
                                cardStyle = CardStyle.entries[card.card_style],
                                cardHolderName = card.cardholder_name,
                                expirationDate = card.expiration_date,
                                cvv = card.cvv,
                                balance = card.balance,
                                isFrozen = card.is_frozen,
                                isContactlessDisabled = card.is_contactless_disabled,
                                isMagstripeDisabled = card.is_magstripe_disabled
                            )
                        }
                    Result.success(cards)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        }
    }

    override suspend fun insertCard(card: Card): Result<Unit> {
        return withContext(ioDispatcher) {
            val requestBody = CardInsertRequest(
                user_id = card.userId,
                card_number = card.cardNumber,
                card_style = card.cardStyle.ordinal,
                cardholder_name = card.cardHolderName,
                expiration_date = card.expirationDate,
                cvv = card.cvv,
            )

            val response = api.insertCard(
                authHeader = bearerToken.invoke(),
                body = requestBody
            )
            handleEmptyResponse(response)
        }
    }

    override suspend fun updateCard(card: Card): Result<Unit> {
        return withContext(ioDispatcher) {
            val requestBody = CardsResponse(
                user_id = card.userId,
                card_number = card.cardNumber,
                card_style = card.cardStyle.ordinal,
                cardholder_name = card.cardHolderName,
                expiration_date = card.expirationDate,
                cvv = card.cvv,
                balance = card.balance,
                is_frozen = card.isFrozen,
                is_contactless_disabled = card.isContactlessDisabled,
                is_magstripe_disabled = card.isMagstripeDisabled,
            )

            val response = api.updateCard(
                id = "eq.${card.id}",
                authHeader = bearerToken.invoke(),
                body = requestBody
            )
            handleEmptyResponse(response)
        }
    }

    override suspend fun getMaskedCards(cardIds: List<String>): Result<List<MaskedCardResponse>> {
        return withContext(ioDispatcher) {
            val requestBody = cardIds.map { MaskedCardRequest(card_id = it) }
            val response = api.getMaskedCards(
                authHeader = bearerToken.invoke(),
                body = requestBody
            )
            handleApiResponse(response)
        }
    }

}
