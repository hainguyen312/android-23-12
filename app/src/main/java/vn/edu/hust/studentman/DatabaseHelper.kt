package vn.edu.hust.studentman

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "StudentDB"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "students"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID TEXT PRIMARY KEY, $COLUMN_NAME TEXT)"
        db.execSQL(createTable)
        if (DatabaseUtils.queryNumEntries(db, TABLE_NAME) == 0L) {
            seedDatabase(db)
        }
    }

    private fun seedDatabase(db: SQLiteDatabase) {
        val initialStudents = listOf(
            StudentModel("Nguyễn Văn An", "SV001"),
            StudentModel("Trần Thị Bảo", "SV002"),
            StudentModel("Lê Hoàng Cường", "SV003"),
            StudentModel("Phạm Thị Dung", "SV004"),
            StudentModel("Đỗ Minh Đức", "SV005"),
            StudentModel("Vũ Thị Hoa", "SV006"),
            StudentModel("Hoàng Văn Hải", "SV007"),
            StudentModel("Bùi Thị Hạnh", "SV008"),
            StudentModel("Đinh Văn Hùng", "SV009"),
            StudentModel("Nguyễn Thị Linh", "SV010"),
            StudentModel("Phạm Văn Long", "SV011"),
            StudentModel("Trần Thị Mai", "SV012"),
            StudentModel("Lê Thị Ngọc", "SV013"),
            StudentModel("Vũ Văn Nam", "SV014"),
            StudentModel("Hoàng Thị Phương", "SV015"),
            StudentModel("Đỗ Văn Quân", "SV016"),
            StudentModel("Nguyễn Thị Thu", "SV017"),
            StudentModel("Trần Văn Tài", "SV018"),
            StudentModel("Phạm Thị Tuyết", "SV019"),
            StudentModel("Lê Văn Vũ", "SV020")
        )
        initialStudents.forEach { student ->
            val values = ContentValues().apply {
                put(COLUMN_ID, student.studentId)
                put(COLUMN_NAME, student.studentName)
            }
            val result = db.insert(TABLE_NAME, null, values)
            if (result == -1L) {
                Log.e("DatabaseHelper", "Failed to add student: $student")
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addStudent(student: StudentModel): Boolean {
        if (getStudentById(student.studentId) != null) return false

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, student.studentId)
            put(COLUMN_NAME, student.studentName)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    fun updateStudent(student: StudentModel): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.studentName)
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(student.studentId))
        db.close()
        return result > 0
    }

    fun deleteStudent(studentId: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(studentId))
        db.close()
        return result > 0
    }

    fun getAllStudents(): List<StudentModel> {
        val students = mutableListOf<StudentModel>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            students.add(StudentModel(name, id))
        }
        cursor.close()
        db.close()
        return students
    }

    fun getStudentById(studentId: String): StudentModel? {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, "$COLUMN_ID = ?", arrayOf(studentId), null, null, null)
        return if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            StudentModel(name, studentId)
        } else {
            null
        }.also {
            cursor.close()
            db.close()
        }
    }
}
