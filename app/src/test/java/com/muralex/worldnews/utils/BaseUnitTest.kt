package com.muralex.achiever.utilities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muralex.worldnews.utils.MainCoroutineScopeRule
import org.junit.Rule
import org.mockito.ArgumentCaptor


open class BaseUnitTest {

    @get:Rule
    var coroutineTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    object MockitoHelper {
        // use this in place of captor.capture() if you are trying to capture an argument that is not nullable
        fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
    }
}