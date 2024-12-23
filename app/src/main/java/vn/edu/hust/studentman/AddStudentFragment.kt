package vn.edu.hust.studentman

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AddStudentFragment : Fragment() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_add_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.add_btn_save).setOnClickListener {
            val name = view.findViewById<EditText>(R.id.add_student_name).text.toString()
            val id = view.findViewById<EditText>(R.id.add_student_id).text.toString()
            if (name.isNotBlank() && id.isNotBlank()) {
                val newStudent = StudentModel(name, id)
                if (dbHelper.addStudent(newStudent)) {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("newStudent", newStudent)
                    findNavController().popBackStack()
                }
            }
        }
    }
}