/*
 * Selected and adapted portfolio excerpt from SoundID.
 *
 * The complete production source code is maintained in a private repository.
 * This self-contained sample demonstrates the project's atomic-save strategy
 * while omitting application-specific storage and serialization details.
 *
 * Copyright © 2026 Zvonimir Mikulandra. All rights reserved.
 */

import java.io.FileOutputStream
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption.ATOMIC_MOVE
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

data class ProjectMetadata(
    val id: Long,
    val createdAtMs: Long,
    val updatedAtMs: Long
)

data class ProjectDocument(
    val metadata: ProjectMetadata,
    val content: String
)

enum class SaveMode { SAVE, SAVE_AS }

interface ProjectCodec {
    fun encode(project: ProjectDocument): ByteArray
    fun decodeMetadata(bytes: ByteArray): ProjectMetadata?
}

/**
 * Save operations are expected to be serialized by the caller,
 * matching the production editor workflow.
 */

class ProjectStore(
    private val directory: Path,
    private val codec: ProjectCodec,
    private val clockMs: () -> Long = System::currentTimeMillis
) {
    private val indexFile: Path = directory.resolve("project-index")

    fun save(
        source: ProjectDocument,
        mode: SaveMode
    ): Result<ProjectDocument> = runCatching {
        Files.createDirectories(directory)

        val targetId = when (mode) {
            SaveMode.SAVE -> source.metadata.id
            SaveMode.SAVE_AS -> generateNewId()
        }
        require(targetId > 0L) { "A project ID must be positive" }

        val target = projectFile(targetId)
        val previousProject = readIfPresent(target)
        val previousMetadata = previousProject?.let(codec::decodeMetadata)
        val createdAt = when (mode) {
            SaveMode.SAVE -> previousMetadata?.createdAtMs
                ?: source.metadata.createdAtMs
            SaveMode.SAVE_AS -> targetId
        }
        val updatedAt = monotonicUpdatedAt(
            requested = source.metadata.updatedAtMs,
            createdAt = createdAt,
            previous = previousMetadata?.updatedAtMs
        )
        val saved = source.copy(
            metadata = ProjectMetadata(targetId, createdAt, updatedAt)
        )

        // SAVE retains the ID and therefore replaces one project instead of
        // appending a duplicate. SAVE_AS receives a collision-free new ID.
        writeAtomically(target, codec.encode(saved))

        val previousIndex = readIfPresent(indexFile)
        val reorderedIds = loadIndex(previousIndex)
            .filterNot { it == targetId }
            .let { listOf(targetId) + it }

        try {
            writeAtomically(indexFile, encodeIndex(reorderedIds))
        } catch (indexFailure: Throwable) {
            // Keep project data and its index transactional with each other.
            try {
                restore(target, previousProject)
            } catch (rollbackFailure: Throwable) {
                indexFailure.addSuppressed(rollbackFailure)
            }
            throw indexFailure
        }

        saved
    }

    private fun monotonicUpdatedAt(
        requested: Long,
        createdAt: Long,
        previous: Long?
    ): Long {
        val nextAfterPrevious = previous
            ?.takeIf { it > 0L }
            ?.let { if (it == Long.MAX_VALUE) it else it + 1L }
            ?: 0L

        return maxOf(
            clockMs(),
            requested.coerceAtLeast(0L),
            createdAt.coerceAtLeast(0L),
            nextAfterPrevious
        )
    }

    private fun writeAtomically(target: Path, bytes: ByteArray) {
        val temporary = Files.createTempFile(
            directory,
            target.fileName.toString() + ".",
            ".tmp"
        )
        try {
            FileOutputStream(temporary.toFile()).use { output ->
                output.write(bytes)
                output.flush()
                output.fd.sync()
            }
            replaceAfterSuccessfulWrite(temporary, target)
        } finally {
            // No-op after a successful move; removes debris after any failure.
            Files.deleteIfExists(temporary)
        }
    }

    private fun replaceAfterSuccessfulWrite(temporary: Path, target: Path) {
        try {
            Files.move(temporary, target, ATOMIC_MOVE, REPLACE_EXISTING)
        } catch (_: AtomicMoveNotSupportedException) {
            // This is the safest portable fallback available through NIO, but
            // unlike ATOMIC_MOVE it does not guarantee atomicity on every platform.
            Files.move(temporary, target, REPLACE_EXISTING)
        }
    }

    private fun restore(target: Path, previous: ByteArray?) {
        if (previous != null) {
            writeAtomically(target, previous)
        } else {
            Files.deleteIfExists(target)
        }
    }

    private fun generateNewId(): Long {
        var candidate = clockMs().coerceAtLeast(1L)
        while (Files.exists(projectFile(candidate))) candidate += 1L
        return candidate
    }

    private fun projectFile(id: Long): Path = directory.resolve("project-$id")

    private fun readIfPresent(path: Path): ByteArray? =
        if (Files.exists(path)) Files.readAllBytes(path) else null

    private fun loadIndex(bytes: ByteArray?): List<Long> =
        bytes?.toString(Charsets.UTF_8)
            ?.lineSequence()
            ?.mapNotNull(String::toLongOrNull)
            ?.toList()
            .orEmpty()

    private fun encodeIndex(ids: List<Long>): ByteArray =
        ids.joinToString(separator = "\n").toByteArray(Charsets.UTF_8)
}
