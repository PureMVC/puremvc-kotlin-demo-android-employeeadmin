//
//  StartupCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import org.puremvc.kotlin.multicore.patterns.command.MacroCommand

class StartupCommand: MacroCommand() {

    override fun initializeMacroCommand() {
        addSubCommand { PrepModelCommand() }
        addSubCommand { PrepViewCommand() }
    }

}