package vn.edu.hust.studentman

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class StudentListFragment : Fragment() {
    private val students = mutableListOf<StudentModel>()
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var dbHelper: DatabaseHelper
    private var selectedStudentIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.layout_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.listView_students)
        val fabAddStudent = view.findViewById<View>(R.id.fab_add_student)

        // Đọc dữ liệu từ database
        students.addAll(dbHelper.getAllStudents())

        // Thiết lập adapter cho ListView
        studentAdapter = StudentAdapter(requireContext(), students)
        listView.adapter = studentAdapter

        registerForContextMenu(listView)

        // Lắng nghe sự kiện nhấn nút thêm
        fabAddStudent.setOnClickListener {
            findNavController().navigate(R.id.action_addStudent)
        }

        // Lắng nghe dữ liệu trả về từ AddStudentFragment
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<StudentModel>("newStudent")
            ?.observe(viewLifecycleOwner) { newStudent ->
                if (dbHelper.addStudent(newStudent)) {
                    students.add(newStudent)
                    studentAdapter.notifyDataSetChanged()
                }
            }

        // Lắng nghe dữ liệu cập nhật từ EditStudentFragment
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<StudentModel>("updatedStudent")
            ?.observe(viewLifecycleOwner) { updatedStudent ->
                val index = students.indexOfFirst { it.studentId == updatedStudent.studentId }
                if (index != -1 && dbHelper.updateStudent(updatedStudent)) {
                    students[index] = updatedStudent
                    studentAdapter.notifyDataSetChanged()
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add_new) {
            findNavController().navigate(R.id.action_addStudent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.menu_edit -> {
                val bundle = Bundle().apply {
                    putSerializable("student", students[info.position])
                }
                findNavController().navigate(R.id.action_editStudent, bundle)
            }
            R.id.menu_remove -> {
                val studentId = students[info.position].studentId
                if (dbHelper.deleteStudent(studentId)) {
                    students.removeAt(info.position)
                    studentAdapter.notifyDataSetChanged()
                }
            }
        }
        return super.onContextItemSelected(item)
    }
}