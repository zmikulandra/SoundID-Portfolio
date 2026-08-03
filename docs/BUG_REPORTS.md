# Selected Bug Report Examples

These examples are based on real issues encountered during SoundID development. They have been simplified and rewritten for public portfolio use.

## BUG-001 — Inserted audio appears at the wrong timeline position

**Severity:** Major  
**Area:** Audio editing / Insert

### Summary

When inserting clipboard audio into a track with a non-zero timeline offset, the visual ghost marker showed the intended global position, but the inserted audio could be written using an incorrect track-local position.

### Preconditions

- A target track starts after the project timeline origin.
- Clipboard audio is available.

### Steps to reproduce

1. Select Insert mode.
2. Move the ghost marker to a visible position inside the target track.
3. Confirm insertion.
4. Play the edited section.

### Expected result

Inserted audio begins exactly at the displayed global timeline position.

### Actual result

Inserted audio begins earlier or later because the global timeline position is treated as a track-local position.

### Root cause

The insert path did not consistently convert the global timeline position into the target track's local audio position.

### Resolution

A dedicated timeline-to-audio-local mapping was introduced. Insert-after-end behaviour was also updated to calculate and render the required silence gap.

### Regression checks

- Same-track insert
- Cross-track insert
- Insert before track start
- Insert after track end
- Insert into tracks with different offsets

---

## BUG-002 — Existing track could be replaced when recording with ARM

**Severity:** Critical  
**Area:** Recording / Track management

### Summary

Recording while a populated track was armed risked behaviour that could be interpreted as replacing or targeting the existing track.

### Expected result

An existing recording must never be overwritten accidentally.

### Resolution

ARM behaviour was changed:

- Only one track can be armed at a time.
- Arming an already armed track unarms it.
- Recording into an empty armed slot fills that slot.
- Recording with a populated armed track inserts a new recording directly below it.
- ARM controls are disabled during active recording.

### Regression checks

- Empty armed slot
- Populated armed track
- Track ordering after recording
- Delete-last-take workflow
- ARM disabled during recording

---

## BUG-003 — Project ordering did not always reflect the latest save

**Severity:** Major  
**Area:** Persistence / Library

### Summary

A recently updated project could fail to appear at the top of the project list when multiple saves occurred with identical or unreliable timestamps.

### Expected result

The most recently saved or updated project appears first.

### Root cause

Ordering depended too strongly on timestamps that could collide or fail to advance reliably.

### Resolution

Project update timestamps were made monotonic by using a value greater than the previous stored timestamp when necessary. Updated projects are also moved to the beginning of the project index.

### Regression checks

- Rapid repeated saves
- Save existing project
- Save As
- Dashboard refresh
- Library refresh after returning to the app

---

## BUG-004 — Multitrack library preview was not synchronized

**Severity:** Major  
**Area:** Library playback

### Summary

Previewing a multitrack project from the library did not use the same synchronized playback model as the editor.

### Expected result

Project preview respects track offsets, trim, gain, pan, mute, and synchronized playback.

### Resolution

Library multitrack preview was moved to the same custom audio engine used by the editor.

### Regression checks

- Tracks with different offsets
- Trimmed tracks
- Mute and solo
- Gain and pan
- Stop and restart preview

---

## BUG-005 — Mini waveform used a different visible range

**Severity:** Minor  
**Area:** Timeline UI

### Summary

The compact waveform display did not always match the active editor timeline window.

### Expected result

Both waveform displays represent the same visible global timeline range.

### Resolution

The mini waveform was updated to use the editor's current visible timeline start and window duration.

### Regression checks

- Zoom in and out
- Horizontal navigation
- Playback auto-follow
- Trim mode
- Different project lengths
