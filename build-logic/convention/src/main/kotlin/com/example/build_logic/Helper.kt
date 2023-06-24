package com.example.build_logic

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.addComposeOfficialDependencies() {

    composeOfficialDependencies.forEach {
        add("implementation",it)
    }
}
