package com.aiinty.copayment.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.aiinty.copayment.domain.model.Profile
import com.google.gson.Gson

class UserPreferences(
    private val context: Context
) {

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_EMAIL_NEW = "user_email_new"
        private const val KEY_USER_PASSWORD = "user_password"
        private const val KEY_USER_PIN = "user_pin"
        private const val KEY_PROFILE = "profile"
        private const val KEY_SELECTED_CARD = "selected_card"
    }

    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveFirstLaunch(value: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()
    }

    fun getFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    fun saveUserId(id: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, id).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun saveUserEmail(email: String) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    fun saveUserNewEmail(email: String) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL_NEW, email).apply()
    }

    fun getUserNewEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL_NEW, null)
    }

    fun saveUserPassword(password: String) {
        sharedPreferences.edit().putString(KEY_USER_PASSWORD, password).apply()
    }

    fun getUserPassword(): String? {
        return sharedPreferences.getString(KEY_USER_PASSWORD, null)
    }

    fun saveUserPin(pin: String) {
        sharedPreferences.edit().putString(KEY_USER_PIN, pin).apply()
    }

    fun getUserPin(): String? {
        return sharedPreferences.getString(KEY_USER_PIN, null)
    }

    fun saveSelectedCard(cardId: String) {
        sharedPreferences.edit().putString(KEY_SELECTED_CARD, cardId).apply()
    }

    fun getSelectedCard(): String? {
        return sharedPreferences.getString(KEY_SELECTED_CARD, null)
    }

    fun saveProfile(profile: Profile) {
        val json = Gson().toJson(profile)
        sharedPreferences.edit().putString(KEY_PROFILE, json).apply()
    }

    fun getProfile(): Profile? {
        val json = sharedPreferences.getString(KEY_PROFILE, null)
        return json?.let {
            try {
                Gson().fromJson(it, Profile::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun clearUserCredentials() {
        sharedPreferences.edit()
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_PASSWORD)
            .apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}