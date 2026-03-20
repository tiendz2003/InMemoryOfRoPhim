package com.rophim.player.utils

import com.rophim.player.model.track.CueWithTiming

internal interface CuesProvider {
    /** The current delay or offset in milliseconds. */
    val offset: Long

    fun addCue(cue: CueWithTiming)

    fun clearCues()
}
