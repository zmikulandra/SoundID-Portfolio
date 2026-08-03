/*
 * Selected and adapted portfolio excerpt from SoundID.
 *
 * The complete production source code is maintained in a private repository.
 * This self-contained sample demonstrates the real track ARM and
 * recording-placement rules while omitting application-specific dependencies.
 *
 * Copyright © 2026 Zvonimir Mikulandra. All rights reserved.
 */

@JvmInline
value class TrackId(val value: Int)

enum class ArmState { ARMED, UNARMED }

data class Track(
    val id: TrackId,
    val recordingKey: String? = null,
    val armState: ArmState = ArmState.UNARMED
) {
    val isEmpty: Boolean get() = recordingKey == null
}

enum class RecordingPlacement {
    FILLED_EMPTY_ARMED_SLOT,
    INSERTED_BELOW_ARMED_TRACK,
    FIRST_AVAILABLE_SLOT
}

data class RecordingTarget(
    val targetIndex: Int,
    val placement: RecordingPlacement,
    val tracksBeforeRecording: List<Track>
)

sealed interface PlacementResult {
    data class Available(val target: RecordingTarget) : PlacementResult
    data object NoEmptySlot : PlacementResult
}

object TrackArmLogic {
    fun toggleArm(
        tracks: List<Track>,
        selectedId: TrackId,
        isRecording: Boolean
    ): List<Track> {
        // Production disables ARM controls while recording.
        if (isRecording) return tracks

        val selected = tracks.firstOrNull { it.id == selectedId } ?: return tracks
        val shouldArmSelected = selected.armState != ArmState.ARMED

        // Mapping every row guarantees that at most one track remains armed.
        return tracks.map { track ->
            track.copy(
                armState = if (shouldArmSelected && track.id == selectedId) {
                    ArmState.ARMED
                } else {
                    ArmState.UNARMED
                }
            )
        }
    }

    fun chooseRecordingTarget(tracks: List<Track>): PlacementResult {
        val armedIndex = tracks.indexOfFirst { it.armState == ArmState.ARMED }
        val armedTrack = tracks.getOrNull(armedIndex)

        if (armedTrack?.isEmpty == true) {
            return PlacementResult.Available(
                RecordingTarget(
                    targetIndex = armedIndex,
                    placement = RecordingPlacement.FILLED_EMPTY_ARMED_SLOT,
                    tracksBeforeRecording = tracks
                )
            )
        }

        val targetIndex = if (armedTrack != null) {
            firstEmptyAtOrAfter(tracks, armedIndex + 1)
        } else {
            tracks.indexOfFirst { it.isEmpty }
        }
        if (targetIndex == -1) return PlacementResult.NoEmptySlot

        val placement = if (armedTrack != null) {
            RecordingPlacement.INSERTED_BELOW_ARMED_TRACK
        } else {
            RecordingPlacement.FIRST_AVAILABLE_SLOT
        }
        return PlacementResult.Available(
            RecordingTarget(targetIndex, placement, tracks)
        )
    }

    fun placeRecording(
        target: RecordingTarget,
        recordingKey: String
    ): List<Track> {
        require(recordingKey.isNotBlank())
        val updated = target.tracksBeforeRecording.toMutableList()

        if (target.placement == RecordingPlacement.INSERTED_BELOW_ARMED_TRACK) {
            val armedIndex = target.tracksBeforeRecording
                .indexOfFirst { it.armState == ArmState.ARMED }
            check(armedIndex in updated.indices)
            check(target.targetIndex in updated.indices)
            check(updated[target.targetIndex].isEmpty)
            val insertIndex = armedIndex + 1
            check(insertIndex in updated.indices)
            check(target.targetIndex >= insertIndex)
            for (index in target.targetIndex downTo insertIndex + 1) {
                updated[index] = updated[index].copy(
                    recordingKey = updated[index - 1].recordingKey,
                    armState = ArmState.UNARMED
                )
            }

            // Only audio moves between fixed slots, so every TrackId remains stable.
            updated[insertIndex] = updated[insertIndex].copy(
                recordingKey = recordingKey,
                armState = ArmState.UNARMED
            )
        } else {
            // Safety rule: only a verified empty slot may be filled, never overwritten.
            check(updated[target.targetIndex].isEmpty)
            updated[target.targetIndex] = updated[target.targetIndex].copy(
                recordingKey = recordingKey
            )
        }

        return updated.map { it.copy(armState = ArmState.UNARMED) }
    }

    fun deleteLatestTake(target: RecordingTarget): List<Track> =
        target.tracksBeforeRecording.map { it.copy() }

    private fun firstEmptyAtOrAfter(tracks: List<Track>, startIndex: Int): Int =
        tracks.indices.firstOrNull { it >= startIndex && tracks[it].isEmpty } ?: -1
}
