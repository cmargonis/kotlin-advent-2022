package day07

import readInput
import java.util.LinkedList

private const val DIRECTORY = "./day07"

private const val COMMAND = "$"
private const val SPACE_NEEDED_FOR_UPDATE = 30000000

fun main() {

    fun traverseFileSystem(node: Node, action: (node: Node) -> Unit) {
        if (node is Node.Directory) node.children.forEach { traverseFileSystem(it, action) }
        action.invoke(node)
    }

    fun performCommand(line: List<String>, fileSystem: FileSystemTree) {
        if (line[1] == "cd") {
            val argument = line[2]
            if (argument == "..") {
                fileSystem.moveOneUp()
            } else {
                fileSystem.moveToOrCreate(argument)
            }
        }
    }

    fun constructFileSystem(input: List<String>): FileSystemTree {
        val fileSystem = FileSystemTree()

        input.drop(1).forEach {
            val line = it.split(" ")
            if (it.startsWith(COMMAND)) {
                performCommand(line, fileSystem)
            } else if (it.startsWith("dir")) {
                fileSystem.createDir(line.last())
            } else {
                fileSystem.createFile(size = line.first().toInt(), name = line.last())
            }
        }
        fileSystem.moveToRoot()
        return fileSystem
    }

    fun part1(input: List<String>): Int {
        val fileSystem = constructFileSystem(input)
        val directories: MutableList<Node.Directory> = mutableListOf()
        traverseFileSystem(fileSystem.currentWorkingDirectory) {
            if (it is Node.Directory && it.name != "/") directories.add(it)
        }
        return directories.filter { it.getTotalSize() <= 100000 }.sumOf { it.getTotalSize() }
    }

    fun part2(input: List<String>): Int {
        val fileSystem = constructFileSystem(input)
        val directories: MutableList<Node.Directory> = mutableListOf()
        traverseFileSystem(fileSystem.currentWorkingDirectory) {
            if (it is Node.Directory && it.name != "/") directories.add(it)
        }
        val spaceToEmpty = SPACE_NEEDED_FOR_UPDATE - fileSystem.freeSpace

        return directories.filter { it.getTotalSize() >= spaceToEmpty }.minBy { it.getTotalSize() }.getTotalSize()
    }


    val input = readInput("${DIRECTORY}/Day07")
    println(part1(input))
    check(part1(input) == 1908462)
    println(part2(input))
    check(part2(input) == 3979145)
}

class FileSystemTree {

    private val totalDiskSpace: Int = 70000000

    val freeSpace: Int
        get() {
            moveToRoot()
            return totalDiskSpace - currentWorkingDirectory.getTotalSize()
        }

    var currentWorkingDirectory: Node.Directory = Node.Directory(mutableListOf(), "/")
        private set

    fun moveToOrCreate(dirName: String) {
        val tmpDir = currentWorkingDirectory.moveToDirectory(dirName)
        currentWorkingDirectory = tmpDir ?: currentWorkingDirectory.createDirectory(dirName)
    }

    fun moveOneUp() {
        if (currentWorkingDirectory.parent != null) {
            currentWorkingDirectory = currentWorkingDirectory.parent!!
        }
    }

    fun createDir(dirName: String) {
        if (currentWorkingDirectory.children.find { it.name == dirName } != null) error("Directory $dirName already exists!")
        currentWorkingDirectory.createDirectory(dirName)
    }

    fun createFile(size: Int, name: String) {
        if (currentWorkingDirectory.children.find { it.name == name } != null) error("File $name already exists!")
        currentWorkingDirectory.createFile(size = size, name = name)
    }

    fun moveToRoot() {
        while (currentWorkingDirectory.parent != null) {
            currentWorkingDirectory = currentWorkingDirectory.parent!!
        }
    }

    private val tree: LinkedList<Node> = LinkedList()

    init {
        tree.add(currentWorkingDirectory)
    }
}

sealed class Node(open val name: String, open val parent: Directory? = null) {

    abstract fun getTotalSize(): Int

    data class Directory(
        val children: MutableList<Node>,
        override val name: String,
        override val parent: Directory? = null
    ) : Node(name, parent) {

        override fun getTotalSize(): Int = children.sumOf { it.getTotalSize() }

        fun moveToDirectory(dirName: String): Directory? =
            children.filterIsInstance<Directory>().firstOrNull() { it.name == dirName }

        fun createDirectory(name: String): Directory {
            val directory = Directory(mutableListOf(), name, this)
            children.add(directory)
            return directory
        }

        fun createFile(size: Int, name: String): File {
            val file = File(name, size)
            children.add(file)
            return file
        }
    }

    data class File(override val name: String, val size: Int) : Node(name) {

        override fun getTotalSize(): Int = size
    }

}
