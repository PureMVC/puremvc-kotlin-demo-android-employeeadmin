//
//  Department.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "department")
data class Department(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "department_id") var id: Long,
    var name: String? = null): Parcelable {

    fun validate(): Boolean {
        return id != 0L
    }

}

data class UserDepartment(
    @Embedded var user: User,
    @Relation(parentColumn = "id", entityColumn = "department_id")
    val department: Department
)