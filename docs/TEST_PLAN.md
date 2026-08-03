# SoundID Test Plan

## 1. Objective

The purpose of this test plan is to verify that SoundID reliably records, plays, edits, saves, loads, and exports multitrack audio projects on supported Android devices.

## 2. Test scope

### In scope

- Project creation
- Single and multitrack recording
- Track arming
- Playback and synchronization
- Timeline navigation and zoom
- Trim and loop
- Cut, silence, insert, and delete
- Mute, solo, gain, pan, and master gain
- Metronome
- Saving and loading
- Recording and project library
- Audio export
- Orientation and fullscreen behaviour
- Closed-test installation and update flow

### Out of scope for this portfolio document

- Full source-code security review
- Professional audio-laboratory measurements
- Unreleased product roadmap features

## 3. Test types

### Functional testing

Verify that each feature produces the expected result.

### Regression testing

Re-test existing functionality after changes to audio, editor, saving, export, or navigation logic.

### Exploratory testing

Use the application outside the main expected workflow to find unexpected states and interaction problems.

### Compatibility testing

Verify installation and core workflows across supported Android versions, screen sizes, and devices.

### Usability testing

Check whether recording, editing, saving, and export are understandable without developer guidance.

### Release testing

Run a final checklist on the signed release build before distribution.

## 4. Test environment

Recommended coverage:

- At least one lower-performance Android device
- At least one modern Samsung device
- At least one Xiaomi/Redmi device
- Multiple Android versions within the supported range
- Portrait and landscape orientation
- Internal or closed Google Play testing build

## 5. Test data

Use:

- Short voice recordings
- Longer recordings
- Silence
- Loud and quiet input
- Two or more simultaneous project tracks
- Tracks with different timeline offsets
- Projects with trim and loop ranges
- WAV and AAC-compatible source material

## 6. Core test areas

### Recording

Verify:

- Recording into an empty armed track
- Recording when an occupied track is armed
- Stopping, saving, skipping, and deleting the last take
- Input gain selection
- Correct insertion order
- No accidental overwrite

### Playback

Verify:

- Synchronized start
- Correct timeline offsets
- Pause, resume, and stop
- Playback after editing
- Playback after project reload
- Mute and solo combinations

### Editing

Verify:

- Trim boundaries
- Cut joins
- Silence replacement
- Insert before start
- Insert inside a track
- Insert after track end
- Same-track and cross-track insert
- Undo and redo

### Persistence

Verify:

- New project save
- Existing project update
- Save As
- Project ordering
- Data integrity after closing and reopening
- Behaviour after interrupted or failed save where practical

### Export

Verify:

- Full project export
- Trimmed export
- Gain, pan, mute, and solo application
- Optional metronome
- File creation
- Playback of the exported result

## 7. Entry criteria

Testing can begin when:

- The build installs successfully
- Required permissions can be granted
- Core navigation opens
- Recording and playback initialise without a blocking error

## 8. Exit criteria

A release candidate is acceptable when:

- No known blocker or critical defect remains
- Core recording, playback, saving, and export pass
- High-risk regression tests pass
- The signed build installs through the intended distribution channel
- Known minor issues are documented

## 9. Defect severity

### Blocker

The application cannot be used or tested.

### Critical

Data loss, project corruption, recording failure, or unusable export.

### Major

A core feature behaves incorrectly, but another workflow may remain available.

### Minor

A limited functional or visual issue that does not block the main workflow.

### Cosmetic

Presentation issue with no functional impact.
