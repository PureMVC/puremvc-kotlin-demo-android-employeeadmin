//
//  Role.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "role")
data class Role(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String? = null): Parcelable

@Entity(tableName = "user_role", primaryKeys = ["user_id", "role_id"], indices = [Index(value = ["user_id", "role_id"], unique = true)], foreignKeys = [
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Role::class, parentColumns = ["id"], childColumns = ["role_id"], onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)])
data class UserRoleJoin(
    @ColumnInfo(name = "user_id", index = true) var userId: Long,
    @ColumnInfo(name = "role_id", index = true) var roleId: Long)

data class UserRole (
    @Embedded var user: User,
    @Relation(parentColumn = "id", entityColumn = "id", associateBy = Junction(UserRoleJoin::class, parentColumn = "user_id", entityColumn = "role_id"))
    var roles: List<Role>
)
