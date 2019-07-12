package sk.csirt.viruschecker.client.web.config

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.apache.commons.io.FileUtils
import sk.csirt.viruschecker.config.defaultTimeout
import java.io.File
import java.nio.charset.Charset

class CommandLineArguments(parser: ArgParser) {
    val gateway by parser.positional(
        help = "Gateway url."
    )

    val socketTimeout by parser.storing(
        "-t", "--timeout",
        help = "Optional: Set socket timeout in milliseconds. Default is $defaultTimeout."
    ) { this.toLong() }.default(defaultTimeout)

}