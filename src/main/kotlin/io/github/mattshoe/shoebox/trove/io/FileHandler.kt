package io.github.mattshoe.shoebox.trove.io

import io.github.mattshoe.shoebox.trove.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class FileHandler {
    suspend fun dirExists(path: String): Boolean = withContext(Dispatchers.IO) {
        File(path).let {
            it.exists() && it.exists()
        }
    }

    suspend fun fileExists(path: String): Boolean = withContext(Dispatchers.IO) {
        File(path).let {
            it.exists() && it.isFile
        }
    }

    suspend fun makeDir(path: String): Path = withContext(Dispatchers.IO) {
        Files.createDirectories(Paths.get(path))
    }

    suspend fun makeFileIfNotExists(filePath: String) = withContext(Dispatchers.IO) {
        val path = Paths.get(filePath)
        try {
            if (Files.notExists(path)) {
                Files.createFile(path)
            }
        } catch (_: FileAlreadyExistsException) { }
    }

    suspend fun read(filePath: String): InputStream? = withContext(Dispatchers.IO) {
        return@withContext try {
            FileInputStream(filePath)
        } catch (e: IOException) {
            println(e)
            null
        }
    }

    suspend fun create(path: String) = withContext(Dispatchers.IO) {
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    suspend fun loadProperties(): Properties = withContext(Dispatchers.IO) {
        val properties = Properties()
        FileInputStream(Config.PROPERTIES_FILE).use { input ->
            properties.load(input)
        }
        properties
    }

    suspend fun saveProperties(properties: Properties) = withContext(Dispatchers.IO) {
        FileOutputStream(Config.PROPERTIES_FILE).use { output ->
            properties.store(output, null) // null for no comment at the top of the file
        }
    }
}