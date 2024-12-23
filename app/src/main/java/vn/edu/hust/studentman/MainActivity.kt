package vn.edu.hust.studentman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main) // Chỉ quản lý NavHostFragment
  }

  override fun onBackPressed() {
    // Quản lý điều hướng quay lại
    val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
    if (navController?.navigateUp() != true) {
      super.onBackPressed()
    }
  }
}
