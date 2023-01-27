//
//  UserProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import android.content.res.Resources
import org.json.JSONArray
import org.json.JSONObject
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

class UserProxy(private val factory: (URL) -> HttpURLConnection): Proxy(NAME, null) {

    companion object {
        const val NAME = "UserProxy"
    }

    fun findAll(): ArrayList<User> {
        val connection = factory(URL("http://10.0.2.2:8080/employees"))
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Language", "en-US");

        (if (connection.responseCode == 200) connection.inputStream else connection.errorStream).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                val response = StringBuffer()
                reader.lines().forEach { response.append(it) }

                if (connection.responseCode != 200)
                    throw java.lang.Exception(response.toString())

                val jsonArray = JSONArray(response.toString())
                val users = arrayListOf<User>()
                for (i in 0 until jsonArray.length()) {
                    users.add(User(jsonArray.getJSONObject(i)))
                }
                return users
            }
        }
    }

    fun findById(id: Long): User {
        val connection = factory(URL("http://10.0.2.2:8080/employees/$id"))
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        (if (connection.responseCode == 200) connection.inputStream else connection.errorStream).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                val response = StringBuffer()
                reader.lines().forEach { response.append(it) }

                if (connection.responseCode != 200)
                    throw Exception(response.toString())

                return User(JSONObject(response.toString()))
            }
        }
    }

    fun save(user: User): Long {
        val connection = factory(URL("http://10.0.2.2:8080/employees"))
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("Content-Type", "application/json")

        BufferedOutputStream(connection.outputStream).use { stream ->
            BufferedWriter(OutputStreamWriter(stream, "UTF-8")).use { writer ->
                writer.write(user.toJSONObject().toString())
            }
        }

        (if (connection.responseCode == 201) connection.inputStream else connection.errorStream).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                val response = StringBuffer()
                reader.lines().forEach { response.append(it) }

                if (connection.responseCode != 201)
                    throw Exception(response.toString())

                return User(JSONObject(response.toString())).id ?: 0
            }
        }
    }

    fun update(user: User): Int {
        val connection = factory(URL("http://10.0.2.2:8080/employees/${user.id}"))
        connection.requestMethod = "PUT"
        connection.doOutput = true
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("Content-Type", "application/json")

        BufferedOutputStream(connection.outputStream).use { stream ->
            BufferedWriter(OutputStreamWriter(stream, "UTF-8")).use { writer ->
                writer.write((user.toJSONObject().toString()))
            }
        }

        (if (connection.responseCode == 200) connection.inputStream else connection.errorStream).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                val response = StringBuffer()
                reader.lines().forEach { response.append(it) }

                if (connection.responseCode != 200)
                    throw Exception(response.toString())

                return 1
            }
        }
    }

    fun deleteById(id: Long): Int {
        val connection = factory(URL("http://10.0.2.2:8080/employees/$id"))
        connection.requestMethod = "DELETE"

        if (connection.responseCode != 204)
            throw Exception(Resources.getSystem().getString(R.string.error_delete))

        return 1
    }

    fun findAllDepartments(): List<Department> {
        val connection = factory(URL("http://10.0.2.2:8080/departments"))
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        (if (connection.responseCode == 200) connection.inputStream else connection.errorStream).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                val response = StringBuffer()
                reader.readLine().also { response.append(it) }

                if (connection.responseCode != 200)
                    throw java.lang.Exception(response.toString())

                val jsonArray = JSONArray(response.toString())
                val departments = arrayListOf<Department>()
                for (i in 0 until jsonArray.length()) {
                    departments.add(Department(jsonArray.getJSONObject(i)))
                }
                return departments
            }
        }
    }

}