package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class EditStudentFragment : Fragment() {
    private var student: StudentModel? = null
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
        arguments?.let {
            student = it.getSerializable("student") as? StudentModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_edit_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextName = view.findViewById<EditText>(R.id.edit_student_name)
        val editTextId = view.findViewById<EditText>(R.id.edit_student_id)
        val btnSave = view.findViewById<Button>(R.id.edit_btn_save)

        // Hiển thị thông tin sinh viên được chỉnh sửa
        student?.let {
            editTextName.setText(it.studentName)
            editTextId.setText(it.studentId)
        }

        btnSave.setOnClickListener {
            val updatedName = editTextName.text.toString().trim()
            val updatedId = editTextId.text.toString().trim()

            if (updatedName.isNotEmpty() && updatedId.isNotEmpty()) {
                val updatedStudent = StudentModel(updatedName, updatedId)

                if (dbHelper.updateStudent(updatedStudent)) {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("updatedStudent", updatedStudent)
                    findNavController().popBackStack()
                }
            }
        }
    }
}
