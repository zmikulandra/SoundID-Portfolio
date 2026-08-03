# Selected SoundID Test Cases

## TC-REC-001 — Record into an empty armed track

**Preconditions**

- A project is open.
- An empty track is armed.
- Microphone permission is granted.

**Steps**

1. Press Record.
2. Record audio for five seconds.
3. Press Stop.
4. Select Save.

**Expected result**

- A new audio item appears in the armed track.
- The recording duration is approximately five seconds.
- Playback starts from the correct project position.
- No other track is modified.

---

## TC-REC-002 — Occupied armed track must not be overwritten

**Preconditions**

- A project contains three populated tracks.
- The second track is armed.

**Steps**

1. Press Record.
2. Record audio for five seconds.
3. Press Stop.
4. Select Save.

**Expected result**

- The existing second track is not overwritten.
- The new recording is inserted directly below the armed track.
- Remaining tracks retain their relative order.

---

## TC-REC-003 — Delete the most recent take

**Preconditions**

- A recording has just been completed.
- The post-recording dialog is visible.

**Steps**

1. Select Delete.

**Expected result**

- The most recent take is removed.
- Previous tracks remain unchanged.
- Track ordering returns to its pre-recording state.

---

## TC-PLAY-001 — Multitrack synchronized playback

**Preconditions**

- A project contains at least two tracks with known synchronized content.

**Steps**

1. Move the playhead to the project start.
2. Press Play.
3. Listen through a section containing both tracks.

**Expected result**

- Both tracks begin at their correct timeline positions.
- No progressive timing drift is audible.
- The playhead follows the active playback position.

---

## TC-TRACK-001 — Solo overrides non-solo tracks

**Preconditions**

- A project contains at least three audible tracks.

**Steps**

1. Enable Solo on the second track.
2. Press Play.
3. Enable Solo on a second track while playback is active.
4. Disable all Solo states.

**Expected result**

- Only soloed tracks are heard while any Solo state is active.
- Muted tracks remain inaudible.
- Normal track playback returns after all Solo states are disabled.

---

## TC-EDIT-001 — Insert audio after the target track end

**Preconditions**

- The clipboard contains valid audio.
- A target track ends before the intended insert position.

**Steps**

1. Select Insert mode.
2. Move the ghost insertion marker beyond the target track end.
3. Confirm insertion.

**Expected result**

- Silence fills the gap between the target end and insert position.
- Clipboard audio begins at the displayed ghost position.
- The result remains synchronized with the global timeline.

---

## TC-EDIT-002 — Delete and replace with silence

**Preconditions**

- A track contains audio across the selected range.

**Steps**

1. Select an internal range.
2. Choose Delete.
3. Select the Silence option.
4. Confirm.

**Expected result**

- Audio in the selected range is replaced with silence.
- Material after the range keeps its original timeline position.
- Total track duration does not collapse due to the operation.

---

## TC-SAVE-001 — Update an existing project

**Preconditions**

- An existing project has been opened.
- A visible change has been made.

**Steps**

1. Press Save.
2. Return to the dashboard.
3. Reopen the same project.

**Expected result**

- The existing project is updated instead of duplicated.
- The latest changes are present.
- The project appears in the correct recent-project order.

---

## TC-SAVE-002 — Save As creates an independent project

**Preconditions**

- An existing project is open.

**Steps**

1. Choose Save As.
2. Enter a new project name.
3. Save.
4. Return to the project list.

**Expected result**

- A new project is created with a different identifier.
- The original project remains available and unchanged.
- Both projects can be opened independently.

---

## TC-EXPORT-001 — Export respects track controls

**Preconditions**

- A project contains multiple tracks.
- One track is muted.
- Another track has non-default gain and pan.

**Steps**

1. Export the full project.
2. Play the exported file.

**Expected result**

- The muted track is absent.
- Gain and pan settings are applied.
- Export duration matches the expected project range.
- The file can be played by a standard audio player.

---

## TC-UI-001 — Landscape fullscreen editor

**Preconditions**

- The editor is open.

**Steps**

1. Enter fullscreen mode.
2. Rotate or attempt to rotate the device.
3. Open the track list.
4. Open settings.
5. Exit fullscreen.

**Expected result**

- Fullscreen remains in the intended landscape layout.
- Editor controls remain accessible.
- Track list and settings can be scrolled where needed.
- Exiting fullscreen restores normal navigation.
