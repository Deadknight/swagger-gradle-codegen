package com.yelp.codegen.plugin

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

abstract class IRoomVariables {
    @Input
    abstract fun getEntityName() : Property<String>

    @Input
    abstract fun getTableName() : Property<String>

    @Input
    abstract fun getPrimaryKeys() : ListProperty<String>

    @Input
    abstract fun getFts3() : Property<Boolean>

    @Input
    abstract fun getFts4() : Property<Boolean>
}

data class PrimaryKey(
    var name: String = "",
    var autogenerate: Boolean = false
)

class RoomVariables {
    constructor() {

    }
    constructor(entityName: String, primaryKeys: List<String>, tableName: String)
    {
        this.entityName = entityName
        this.primaryKeys = primaryKeys
        this.tableName = tableName
    }
    var entityName: String = ""
    var tableName: String? = null
    var primaryKeys: List<String> = listOf()
    var fts3: Boolean = false
    var fts4: Boolean = false
    var generateKeys: List<PrimaryKey> = listOf()
}