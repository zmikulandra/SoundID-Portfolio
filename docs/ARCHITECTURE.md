# SoundID Architecture

## Purpose

This document provides a high-level overview of the SoundID Android application architecture. It intentionally omits private implementation details and complete production source code.

## Main application areas

### Dashboard and navigation

The dashboard provides access to:

- New recording
- New multitrack project
- Existing projects
- Recordings
- Exports
- Settings

### Multitrack editor

The editor manages:

- Track layout
- Timeline and waveform display
- Recording state
- Playback transport
- Selection and trim state
- Loop range
- Track arm, mute, solo, gain, and pan
- Insert, cut, silence, and delete operations

### Audio runtime

SoundID uses a custom playback engine based on Android `AudioTrack`.

Key responsibilities include:

- Preparing track audio
- Coordinating synchronized playback
- Tracking the current position in audio frames
- Applying track gain and pan
- Respecting mute and solo state
- Supporting trim and timeline offsets
- Providing playback state to the UI

Recording uses Android `AudioRecord`.

### Timeline and waveform

The waveform timeline is responsible for:

- Rendering waveform peaks
- Mapping screen coordinates to timeline positions
- Zooming and navigation
- Playhead display
- Auto-follow during playback
- Trim and loop markers
- Edge auto-scroll during editing

### Audio editing

Supported editing operations include:

- Trim
- Delete and cut
- Delete and replace with silence
- Insert on the same track
- Insert across tracks
- Ripple-style placement
- Silence gaps where required

The application uses WAV/PCM rendering paths for editing operations where reliable sample-level control is required.

### Project persistence

Projects are saved using an atomic write strategy:

1. Write project data to a temporary file.
2. Flush data to storage.
3. Replace the previous file only after a successful write.
4. Update project metadata and ordering.

This reduces the chance of project corruption if saving is interrupted.

### Caching

The project uses prepared-audio and waveform-peak caches to reduce repeated processing.

Caching responsibilities include:

- Prepared PCM audio
- Waveform peak data
- Memory reuse
- Disk-cache size management
- Version-based invalidation

### Export

Export combines project tracks while respecting:

- Timeline offsets
- Trim state
- Track gain
- Track pan
- Mute and solo
- Master gain
- Optional metronome

The export path can use WAV fallback when necessary.

## Simplified data flow

```text
User action
    ↓
Jetpack Compose UI
    ↓
Editor/project state
    ↓
Audio engine or editing utility
    ↓
Audio file / project storage / export
    ↓
Updated state returned to UI
```

## Technical challenges

The most significant technical challenges have included:

- Keeping several audio tracks synchronized
- Mapping zoomed timeline positions to audio-local positions
- Preventing project data loss during saving
- Maintaining correct behaviour after track reordering
- Handling audio formats during insert and export
- Keeping waveform visuals consistent with the active timeline window
- Preserving expected behaviour across Android devices and versions
