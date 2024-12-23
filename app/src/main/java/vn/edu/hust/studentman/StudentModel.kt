package vn.edu.hust.studentman

import java.io.Serializable

data class StudentModel(
    val studentName: String,
    val studentId: String
) : Serializable
