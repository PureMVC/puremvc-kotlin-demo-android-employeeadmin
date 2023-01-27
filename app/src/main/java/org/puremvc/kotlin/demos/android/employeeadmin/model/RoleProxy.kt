//
//  RoleProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import org.json.JSONArray
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class RoleProxy(private val factory: (URL) -> HttpURLConnection) : Proxy(NAME, null) {

    companion object {
        const val NAME: String = "RoleProxy"
    }

    fun findAll(): List<Role> {
        val connection = factory(URL("http://10.0.2.2:8080/roles"))
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        (if (connection.responseCode == 200) connection.inputStream else connection.errorStream).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                val response = StringBuffer()
                reader.lines().forEach { response.append(it) }

                if (connection.responseCode != 200)
                    throw Exception(response.toString())

                val jsonArray = JSONArray(response.toString())
                val roles = arrayListOf<Role>()
                for (i in 0 until jsonArray.length()) {
                    roles.add(Role(jsonArray.getJSONObject(i)))
                }
                return roles
            }
        }
    }

    fun findByUserId(id: Long): ArrayList<Role> {
        val connection = factory(URL("http://10.0.2.2:8080/employees/$id/roles"))
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        (if (connection.responseCode == 200) connection.inputStream else connection.errorStream).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                val response = StringBuffer()
                reader.lines().forEach { response.append(it) }

                if (connection.responseCode != 200)
                    throw Exception(response.toString())

                val jsonArray = JSONArray(response.toString())
                val roles = arrayListOf<Role>()
                for (i in 0 until jsonArray.length()) {
                    roles.add(Role(jsonArray.getJSONObject(i)))
                }
                return roles
            }
        }
    }

    fun updateByUserId(id: Long, roles: List<Role>): Int {
        val connection = factory(URL("http://10.0.2.2:8080/employees/$id/roles"))
        connection.requestMethod = "PUT"
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("Content-Type", "application/json")

        BufferedOutputStream(connection.outputStream).use { stream ->
            BufferedWriter(OutputStreamWriter(stream, "UTF-8")).use { writer ->
                writer.write(roles.map { it.id }.toString())
            }
        }

        (if(connection.responseCode == 200) connection.inputStream else connection.errorStream).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                val response = StringBuffer()
                reader.lines().forEach { response.append(it) }

                if (connection.responseCode != 200)
                    throw Exception(response.toString())

                return 1
            }
        }
    }

}
