package com.example.build_logic

import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

fun DependencyHandlerScope.addComposeOfficialDependencies() {

    composeOfficialDependencies.forEach {
        add("implementation",it)
    }
}
