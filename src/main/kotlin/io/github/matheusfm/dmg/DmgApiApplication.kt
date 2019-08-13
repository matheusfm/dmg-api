package io.github.matheusfm.dmg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DmgApiApplication

fun main(args: Array<String>) {
	runApplication<DmgApiApplication>(*args)
}
