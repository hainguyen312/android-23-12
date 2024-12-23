package vn.edu.hust.studentman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter

class StudentAdapter(
  context: Context,
  students: List<StudentModel>
) : ArrayAdapter<StudentModel>(context, android.R.layout.simple_list_item_2, students) {

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

    val student = getItem(position)

    val text1 = view.findViewById<TextView>(android.R.id.text1)
    val text2 = view.findViewById<TextView>(android.R.id.text2)

    text1.text = student?.studentName
    text2.text = student?.studentId

    return view
  }
}
