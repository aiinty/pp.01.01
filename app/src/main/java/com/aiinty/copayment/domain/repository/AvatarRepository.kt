package com.aiinty.copayment.domain.repository

import java.io.File

interface AvatarRepository {
    suspend fun uploadAvatar(userId: String, fileName: String, file: File): Result<Unit>
}