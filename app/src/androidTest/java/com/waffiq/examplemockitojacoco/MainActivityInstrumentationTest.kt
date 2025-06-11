package com.waffiq.examplemockitojacoco

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Instrumentation test that reproduces the Mockito v5.18.0 + JaCoCo + Byte Buddy issue.
 *
 * This test will fail with the error:
 * "Failed to transform byte-buddy-1.17.5.jar to match attributes artifactType=jacoco-classes-jar"
 *
 * When Mockito v5.18.0 is used with JaCoCo enabled (testCoverageEnabled = true)
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentationTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun testMainActivityDisplaysCorrectly() {
    // Verify initial UI elements are displayed
    composeTestRule.onNodeWithText("Mockito v5.18.0 Issue Demo").assertIsDisplayed()
    composeTestRule.onNodeWithText("No data loaded").assertIsDisplayed()
    composeTestRule.onNodeWithText("Load Data").assertIsDisplayed()
    composeTestRule.onNodeWithText("Process Data").assertIsDisplayed()
  }

  @Test
  fun testLoadDataButton() {
    // Click the Load Data button
    composeTestRule.onNodeWithText("Load Data").performClick()

    // Verify the data is loaded
    composeTestRule.onNodeWithText("Data fetched successfully!").assertIsDisplayed()
  }

  @Test
  fun testProcessDataButton() {
    // Click the Process Data button
    composeTestRule.onNodeWithText("Process Data").performClick()

    // Verify the data is processed
    composeTestRule.onNodeWithText("Processed: Test input").assertIsDisplayed()
  }

  /**
   * This test specifically uses Mockito in an instrumentation test.
   * This is where the Byte Buddy + JaCoCo conflict typically manifests.
   */
  @Test
  fun testDataServiceWithMockito() {
    // Create a mock DataService - this triggers the Byte Buddy usage
    val mockDataService: DataService = mock()

    // Configure mock behavior
    whenever(mockDataService.fetchData()).thenReturn("Mocked data")
    whenever(mockDataService.processData(any())).thenReturn("Mocked processed data")
    whenever(mockDataService.validateData(any())).thenReturn(true)

    // Test the mock
    val result = mockDataService.fetchData()
    assert(result == "Mocked data")

    val processedResult = mockDataService.processData("test")
    assert(processedResult == "Mocked processed data")

    val isValid = mockDataService.validateData("test data")
    assert(isValid)

    // Verify interactions
    verify(mockDataService).fetchData()
    verify(mockDataService).processData("test")
    verify(mockDataService).validateData("test data")
  }

  /**
   * Test that demonstrates complex Mockito usage that would trigger
   * more intensive Byte Buddy transformations
   */
  @Test
  fun testComplexMockitoScenario() {
    val mockService: DataService = mock()

    // Multiple stubbing scenarios
    whenever(mockService.fetchData())
      .thenReturn("First call")
      .thenReturn("Second call")
      .thenThrow(RuntimeException("Third call"))

    // Test multiple calls
    assert(mockService.fetchData() == "First call")
    assert(mockService.fetchData() == "Second call")

    try {
      mockService.fetchData()
      assert(false) { "Should have thrown exception" }
    } catch (e: RuntimeException) {
      assert(e.message == "Third call")
    }

    // Verify call count
    verify(mockService, org.mockito.kotlin.times(3)).fetchData()
  }
}
