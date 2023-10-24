@file:JvmName("MainInternal")

package com.yelp.codegen

import com.yelp.codegen.plugin.PrimaryKey
import com.yelp.codegen.plugin.RoomVariables
import io.swagger.codegen.DefaultGenerator
import io.swagger.codegen.config.CodegenConfigurator
import org.apache.commons.cli.*

fun main() {
    val options = Options()
    options.addRequiredOption(
            "p",
            "platform",
            true,
            "The platform to generate"
    )
    options.addRequiredOption(
            "i",
            "input",
            true,
            "Path to the input spec"
    )
    options.addRequiredOption(
            "o",
            "output",
            true,
            "Path to the output directory"
    )
    options.addRequiredOption(
            "s",
            "service",
            true,
            "Name of the service to build"
    )
    options.addRequiredOption(
            "v",
            "version",
            true,
            "Version to use when generating the code."
    )
    options.addRequiredOption(
            "g",
            "groupid",
            true,
            "The fully qualified domain name of company/organization."
    )
    options.addRequiredOption(
            "a",
            "artifactid",
            true,
            "The artifact id to be used when generating the code."
    )
    options.addOption(
            Option.builder("ignoreheaders")
                    .hasArg().argName("Comma separated list of headers to ingore")
                    .desc("A comma separated list of headers that will be ignored by the generator")
                    .build()
    )

    val parser: CommandLineParser = DefaultParser()
    val parsed = mapOf<Any, String>()
    //val parsed: CommandLine = parser.parse(options, args)

    val specVersion = /*parsed['v']*/"1.0"

    val configurator = CodegenConfigurator()
    configurator.lang = parsed['p']
    configurator.inputSpec = parsed['i']
    configurator.outputDir = parsed['o']

    configurator.lang = "kotlin-coroutines"
    configurator.inputSpec = "./office_free_specs.json"
    configurator.outputDir = "./testout"

    //configurator.addAdditionalProperty(LANGUAGE, parsed['p'])
    configurator.addAdditionalProperty(LANGUAGE, "kotlin-coroutines")

    configurator.addAdditionalProperty(SPEC_VERSION, specVersion)
    configurator.addAdditionalProperty(SERVICE_NAME, /*parsed['s']*/ "test")
    configurator.addAdditionalProperty(GROUP_ID, /*parsed['g']*/ "asd.test")
    configurator.addAdditionalProperty(ARTIFACT_ID, /*parsed['a']*/ "asdasd")
    configurator.addAdditionalProperty(HEADERS_TO_IGNORE, /*parsed["ignoreheaders"]*/
            "deviceId,appVersion,ipAddress,os,osVersion,deviceBrand,deviceModel,X-Operation-ID,Content-Type")

    var rv = RoomVariables("FieldCountingDto", listOf("id"), "test")
    rv.generateKeys = listOf(PrimaryKey("asdasd", true))
    val roomAnnotations = listOf(rv)
    configurator.addAdditionalProperty(ROOM_ANNOTATIONS, roomAnnotations)


    DefaultGenerator().opts(configurator.toClientOptInput()).generate()
    copySpec(checkNotNull(configurator.inputSpec), checkNotNull(configurator.outputDir))
}

private operator fun CommandLine.get(opt: Char): String? {
    return getOptionValue(opt, null)
}

private operator fun CommandLine.get(opt: String): String? {
    return getOptionValue(opt, null)
}
