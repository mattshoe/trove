package io.github.mattshoe.shoebox.trove.commands

import com.github.ajalt.clikt.core.CliktCommand

class TroveCommand: CliktCommand(name = "trove") {
    override fun run() {
        println("The trove hath been treasured!")
    }
}