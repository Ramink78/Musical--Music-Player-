package rk.core

data class PlayerState(
    val isPlaying: Boolean,
    val trackName: String,
    val trackArtist: String,
    val coverUri: String?,
    val totalTime: String,
    val passedTime: String,
    val shuffleMode: Boolean,
    val repeatMode: RepeatMode,
    val isFavorite: Boolean,
    val playbackSpeed: PlaybackSpeed,
    val progress: Float
) {
    companion object {
        val idle = PlayerState(
            isPlaying = false,
            trackName = "",
            trackArtist = "",
            totalTime = "00:00",
            passedTime = "00:00",
            shuffleMode = false,
            repeatMode = RepeatMode.Off,
            isFavorite = false,
            coverUri = null,
            playbackSpeed = PlaybackSpeed.Normal,
            progress = 0f,
        )
    }
}
