package com.waffiq.examplemockitojacoco

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for DataService - these typically work fine even with Mockito v5.18.0
 * because they don't involve JaCoCo instrumentation transforms
 */
class DataServiceUnitTest {

  @Test
  fun testFetchData() {
    val dataService = DataService()
    val result = dataService.fetchData()
    assertEquals("Data fetched successfully!", result)
  }

  @Test
  fun testProcessData() {
    val dataService = DataService()
    val result = dataService.processData("test input")
    assertEquals("Processed: test input", result)
  }

  @Test
  fun testValidateData() {
    val dataService = DataService()

    assertTrue(dataService.validateData("valid data"))
    assertFalse(dataService.validateData(""))
  }

  @Test
  fun testMockitoInUnitTest() {
    // This should work fine - unit tests don't have the JaCoCo transform issue
    val mockService: DataService = mock()

    whenever(mockService.fetchData()).thenReturn("Mocked unit test data")
    whenever(mockService.validateData("test")).thenReturn(true)

    val result = mockService.fetchData()
    val isValid = mockService.validateData("test")

    assertEquals("Mocked unit test data", result)
    assertTrue(isValid)

    verify(mockService).fetchData()
    verify(mockService).validateData("test")
  }
}