package com.hassan.duckit.util

object Validation {
    private const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    private const val URL_PATTERN = "^https?://.*$"

    sealed class ValidationResult {
        data object Success : ValidationResult()
        data class Error(val message: String) : ValidationResult()
    }

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error("Email cannot be empty")
            !email.matches(EMAIL_PATTERN.toRegex()) -> ValidationResult.Error("Invalid email format")
            else -> ValidationResult.Success
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error("Password cannot be empty")
            password.length < 6 -> ValidationResult.Error("Password must be at least 6 characters")
            !password.any { it.isDigit() } -> ValidationResult.Error("Password must contain at least one number")
            !password.any { it.isLetter() } -> ValidationResult.Error("Password must contain at least one letter")
            else -> ValidationResult.Success
        }
    }

    fun validateHeadline(headline: String): ValidationResult {
        return when {
            headline.isBlank() -> ValidationResult.Error("Headline cannot be empty")
            headline.length > 100 -> ValidationResult.Error("Headline too long (max 100 characters)")
            else -> ValidationResult.Success
        }
    }

    fun validateImageUrl(url: String): ValidationResult {
        return when {
            url.isBlank() -> ValidationResult.Error("Image URL cannot be empty")
            !url.matches(URL_PATTERN.toRegex()) -> ValidationResult.Error("Must be a valid HTTP/HTTPS URL")
            else -> ValidationResult.Success
        }
    }
}
