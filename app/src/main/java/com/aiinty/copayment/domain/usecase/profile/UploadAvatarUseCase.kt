package com.aiinty.copayment.domain.usecase.profile

import com.aiinty.copayment.domain.repository.AvatarRepository
import java.io.File

class UploadAvatarUseCase(
    private val avatarRepository: AvatarRepository
) {

    suspend operator fun invoke(
        userId: String,
        fileName: String,
        file: File
    ): Result<Unit> {
        return avatarRepository.uploadAvatar(userId, fileName, file)
    }
}