package com.hassan.duckit.base

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.time.Duration.Companion.seconds

class TurbineTestRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                base.evaluate()
            }
        }
    }

    suspend fun <T> Flow<T>.test(
        validate: suspend TurbineTestContext<T>.() -> Unit
    ) {
        this.test(timeout = 3.seconds, validate = validate)
    }
}
