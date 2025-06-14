package com.aiinty.copayment.domain.model

data class Profile(
    val id: String,
    val email: String,
    val phone: String?,
    val fullName: String,
    val avatarUrl: String
) {
    val fullAvatarUrl: String
        get() = "https://cqvwbtjvzqmjfskbduav.supabase.co/storage/v1/object/public/avatars/${avatarUrl}"
}
