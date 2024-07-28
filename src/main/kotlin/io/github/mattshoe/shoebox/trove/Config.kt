package io.github.mattshoe.shoebox.trove

import io.github.mattshoe.shoebox.trove.io.FileHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class Config(
    private val fileHandler: FileHandler
) {
    companion object {
        val CONFIG_DIR = "${System.getenv("HOME")}/.trove"
        val PROPERTIES_FILE = "$CONFIG_DIR/trove.properties"
        val REPO_DIR = "$CONFIG_DIR/repo"
    }

    suspend fun initialize() {
        fileHandler.makeDir(CONFIG_DIR)
        fileHandler.makeDir(REPO_DIR)
        fileHandler.makeFileIfNotExists(PROPERTIES_FILE)
    }

    suspend fun workspaces(): List<String> = withContext(Dispatchers.IO) {
        val directory = File(REPO_DIR)
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles { _, name -> name.endsWith(".yaml", ignoreCase = true) }?.toList() ?: emptyList()
        } else {
            emptyList()
        }.map {
            it.name.removeSuffix(".yaml")
        }
    }

    suspend fun newWorkspace(name: String) = withContext(Dispatchers.IO) {
        val file = File("${REPO_DIR}/${name}.yaml")
        if (!file.exists()) {
            file.createNewFile()
        }
        with (fileHandler.loadProperties()) {
            this.setProperty("defaultWorkspace", name)
            fileHandler.saveProperties(this)
        }
    }
}