package com.aiinty.copayment.data.network

enum class ApiErrorCode(val code: String) {
    EMAIL_NOT_CONFIRMED("email_not_confirmed"),
    OTP_EXPIRED("otp_expired"),
    VALIDATION_FAILED("validation_failed"),
    OVER_EMAIL_SEND_RATE_LIMIT("over_email_send_rate_limit"),
    WEAK_PASSWORD("weak_password"),
    USER_ALREADY_EXISTS("user_already_exists"),
    INVALID_CREDENTIALS("invalid_credentials"),
    REFRESH_TOKEN_NOT_FOUND("refresh_token_not_found"),
    BAD_JWT("bad_jwt"),
}