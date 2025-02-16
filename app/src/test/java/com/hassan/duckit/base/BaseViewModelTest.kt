package com.hassan.duckit.base

import org.junit.Rule


abstract class BaseViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val turbineRule = TurbineTestRule()
}
