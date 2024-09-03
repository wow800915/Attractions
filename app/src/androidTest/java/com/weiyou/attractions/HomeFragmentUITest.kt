package com.weiyou.attractions

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.weiyou.attractions.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeFragmentUITest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
        launchActivity<MainActivity>()
    }

    @Test
    fun testLanguageSelectionEnglish() {
        onView(withId(R.id.iv_lang)).check(matches(isDisplayed())).perform(click())

        onView(withText("英文")).perform(click())

        onView(allOf(withId(R.id.tv_title), withParent(withId(R.id.upperBarLayout))))
            .check(matches(withText("Explore Taipei")))
    }

    @Test
    fun testLanguageSelectionChinese() {
        onView(withId(R.id.iv_lang)).check(matches(isDisplayed())).perform(click())

        onView(withText("Traditional Chinese")).perform(click())

        onView(allOf(withId(R.id.tv_title), withParent(withId(R.id.upperBarLayout))))
            .check(matches(withText("悠遊台北")))
    }

}

