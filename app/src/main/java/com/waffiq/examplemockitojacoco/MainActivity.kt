package com.waffiq.examplemockitojacoco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.waffiq.examplemockitojacoco.ui.theme.ExamplemockitojacocoTheme

class MainActivity : ComponentActivity() {
  private val dataService = DataService()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ExamplemockitojacocoTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          MainScreen(
            dataService = dataService,
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}

@Composable
fun MainScreen(
  dataService: DataService,
  modifier: Modifier = Modifier
) {
  var data by remember { mutableStateOf("No data loaded") }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Mockito v5.18.0 Issue Demo",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp)
      )

      Text(
        text = data,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = 16.dp)
      )

      Button(
        onClick = {
          data = dataService.fetchData()
        }
      ) {
        Text("Load Data")
      }

      Button(
        onClick = {
          data = dataService.processData("Test input")
        },
        modifier = Modifier.padding(top = 8.dp)
      ) {
        Text("Process Data")
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  ExamplemockitojacocoTheme {
    MainScreen(dataService = DataService())
  }
}

// Simple service class to test with Mockito
open class DataService {
  open fun fetchData(): String {
    return "Data fetched successfully!"
  }

  open fun processData(input: String): String {
    return "Processed: $input"
  }

  open fun validateData(data: String): Boolean {
    return data.isNotEmpty()
  }
}
