package io.github.mattshoe.shoebox.trove

import io.github.mattshoe.shoebox.trove.commands.TroveCommand
import io.github.mattshoe.shoebox.trove.io.FileHandler
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.TextProgressMonitor
import java.io.File


suspend fun main(args: Array<String>) {
    val fileHandler = FileHandler()
    val config = Config(fileHandler).apply {
        initialize()
    }
    val workspace = fileHandler.loadProperties().getProperty("defaultWorkspace")
    if (workspace.isNullOrEmpty()) {
        initializeFirstUse(fileHandler, config)
    } else {
        println("\nCurrent workspace: $workspace\n")
    }

    TroveCommand()
        .main(args)
}

private suspend fun initializeFirstUse(fileHandler: FileHandler, config: Config) {
    printWelcome()
    if (!fileHandler.dirExists("${Config.REPO_DIR}/.git")) {
        println("It looks like this is your first time here!")
        var repository = promptForRepository()

        while (isSshUrl(repository!!)) {
            println("\nUnfortunately, Trove cannot support SSH at this time. Please use HTTPS instead.")
            repository = promptForRepository()
        }
        Git.cloneRepository()
            .setURI(repository)
            .setTimeout(60)
            .setDirectory(File(Config.REPO_DIR))
            .setProgressMonitor(TextProgressMonitor())
            .call()

        val workspaces = config.workspaces()
        if (workspaces.isEmpty()) {
            println("Looks like you don't have any workspaces to import!")
            print("What's the name of your first workspace? (think carefully): ")
        } else {
            println("Looks like you've got some workspaces already available: \n${workspaces.joinToString("\n\t") { it }}")
            print("\nChoose one of your existing workspaces or create a new one: ")
        }
        val workspace = readlnOrNull()

        println("Welcome to your new WorkSpace '$workspace'!")
        config.newWorkspace(workspace!!)
    }
}

private fun printWelcome() {
    println(
        """
            
            Welcome to Trove!
            
            This toolkit has many purposes, and its prime directives are two-fold:
                1. Abstract away common command line tedium
                2. Allow you to share configurations out-of-the-box between machines
                
        """.trimIndent()
    )
}

private fun promptForRepository(): String? {
    print("Enter the repository URL to hold your settings: ")
    return readlnOrNull()
}

fun isSshUrl(url: String): Boolean {
    return url.startsWith("ssh://")
            || url.startsWith("git@")
            || url.matches(Regex("^[\\w-]+@[\\w.-]+:.*$"))
}