# SoundID – Mobile Multitrack Audio Recorder

> A curated portfolio presentation of a private Android application project.

SoundID is a mobile multitrack audio recording and editing application for Android. It is designed to help musicians quickly capture, arrange, edit, and export musical ideas before they are lost.

The complete production source code is maintained in a private repository. This public repository contains project documentation, testing materials, screenshots, a demo, and selected code excerpts prepared for portfolio review.

## Project overview

SoundID has been designed and developed independently as a long-term Android project.

The application includes:

- Multitrack audio recording
- Synchronized multitrack playback
- Waveform timeline with zoom and navigation
- Trim, cut, silence, insert, and loop operations
- Track arm, mute, solo, gain, and pan controls
- Metronome during recording, playback, and export
- Project saving and loading
- Recording and export library
- WAV and AAC audio export
- Google Play closed testing

## Screenshots

Screenshots will be added to the [`screenshots`](screenshots/) directory.

Recommended screenshots:

1. Dashboard
2. Multitrack editor
3. Waveform timeline
4. Track controls
5. Library
6. Export or trim workflow

## Demo

A short application demo will be added to the [`demo`](demo/) directory or linked here.

Recommended demo length: 60–120 seconds.

The demo should show:

- Creating a new project
- Recording two tracks
- Playing them together
- Trimming or looping a section
- Adjusting mute, solo, gain, or pan
- Saving and exporting the project

## Technology

- Kotlin
- Jetpack Compose
- Android Studio
- Android AudioRecord
- Android AudioTrack
- MediaExtractor and MediaMuxer
- Git and GitHub
- Google Play Console

## Architecture and engineering

The application includes a custom audio runtime, frame-based playback tracking, multitrack synchronization, waveform rendering, non-destructive project state, audio editing utilities, caching, and atomic project saving.

See [Architecture](docs/ARCHITECTURE.md).

## Testing and quality assurance

The project has required continuous functional, regression, exploratory, compatibility, and release testing.

Public testing documentation:

- [Test plan](docs/TEST_PLAN.md)
- [Selected test cases](docs/TEST_CASES.md)
- [Bug report examples](docs/BUG_REPORTS.md)

Areas tested include:

- Recording and playback
- Multitrack synchronization
- Audio editing
- Project persistence
- Export
- Device and Android-version compatibility
- Orientation and fullscreen behaviour
- Regression after feature changes
- Google Play closed testing

## Selected code samples

The [`code-samples`](code-samples/) directory is intended for selected, reviewed excerpts from the private project.

Planned samples:

- `WaveformTimelineSample.kt`
- `AtomicProjectSaveSample.kt`
- `TrackArmLogicSample.kt`
- `AudioEngineExcerpt.kt`

Project-specific dependencies and implementation details may be omitted. The code samples are provided to demonstrate technical approach rather than to reproduce the complete application.

## My role

I independently designed, developed, tested, debugged, and prepared the application for distribution.

My work has included:

- Product and feature design
- Android UI development
- Audio recording and playback implementation
- Testing and bug reproduction
- Regression checks after changes
- Release preparation
- Google Play Console configuration
- Closed-test organisation and feedback collection

## Current status

The application is in closed testing and feature-freeze preparation. Current work is focused on testing, stability, release readiness, and documentation.

## Repository notice

This repository is a curated portfolio presentation. It does not contain the complete production source code.

Copyright © 2026 Zvonimir Mikulandra. All rights reserved.

No permission is granted to reproduce, distribute, publish, or use the included source code in another product without written permission.
