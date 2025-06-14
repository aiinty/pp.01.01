package com.aiinty.copayment.domain.repository

import kotlinx.coroutines.delay
import java.io.File

class FakeAvatarRepository : AvatarRepository {

    override suspend fun uploadAvatar(userId: String, fileName: String, file: File): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }
}
