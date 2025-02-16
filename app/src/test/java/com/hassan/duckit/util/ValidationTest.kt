package com.hassan.duckit.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlin.test.Test

class ValidationTest {

    @Test
    fun `validateEmail with valid email should return Success`() {
        val result = Validation.validateEmail("test@example.com")
        assertTrue(result is Validation.ValidationResult.Success)
    }

    @Test
    fun `validateEmail with empty email should return Error`() {
        val result = Validation.validateEmail("")
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Email cannot be empty", (result as Validation.ValidationResult.Error).message)
    }

    @Test
    fun `validateEmail with invalid format should return Error`() {
        val result = Validation.validateEmail("invalid-email")
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Invalid email format", (result as Validation.ValidationResult.Error).message)
    }

    @Test
    fun `validatePassword with valid password should return Success`() {
        val result = Validation.validatePassword("Password1")
        assertTrue(result is Validation.ValidationResult.Success)
    }

    @Test
    fun `validatePassword with empty password should return Error`() {
        val result = Validation.validatePassword("")
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Password cannot be empty", (result as Validation.ValidationResult.Error).message)
    }

    @Test
    fun `validatePassword with less than 6 characters should return Error`() {
        val result = Validation.validatePassword("pass1")
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Password must be at least 6 characters", (result as Validation.ValidationResult.Error).message)
    }

    @Test
    fun `validatePassword with no digits should return Error`() {
        val result = Validation.validatePassword("password")
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Password must contain at least one number", (result as Validation.ValidationResult.Error).message)
    }

    @Test
    fun `validatePassword with no letters should return Error`() {
        val result = Validation.validatePassword("123456")
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Password must contain at least one letter", (result as Validation.ValidationResult.Error).message)
    }

    @Test
    fun `validateHeadline with valid headline should return Success`() {
        val result = Validation.validateHeadline("This is a valid headline")
        assertTrue(result is Validation.ValidationResult.Success)
    }

    @Test
    fun `validateHeadline with empty headline should return Error`() {
        val result = Validation.validateHeadline("")
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Headline cannot be empty", (result as Validation.ValidationResult.Error).message)
    }

    @Test
    fun `validateHeadline with too long headline should return Error`() {
        val longHeadline = "a".repeat(101)
        val result = Validation.validateHeadline(longHeadline)
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Headline too long (max 100 characters)", (result as Validation.ValidationResult.Error).message)
    }

    @Test
    fun `validateImageUrl with valid URL should return Success`() {
        val result = Validation.validateImageUrl("https://example.com/image.jpg")
        assertTrue(result is Validation.ValidationResult.Success)
    }

    @Test
    fun `validateImageUrl with empty URL should return Error`() {
        val result = Validation.validateImageUrl("")
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Image URL cannot be empty", (result as Validation.ValidationResult.Error).message)
    }

    @Test
    fun `validateImageUrl with invalid URL should return Error`() {
        val result = Validation.validateImageUrl("invalid-url")
        assertTrue(result is Validation.ValidationResult.Error)
        assertEquals("Must be a valid HTTP/HTTPS URL", (result as Validation.ValidationResult.Error).message)
    }
}

