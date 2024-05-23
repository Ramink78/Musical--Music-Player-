package rk.core

enum class SortOrder {
    DateAddedAsc,
    DateAddedDesc,
    NameAsc,
    NameDesc
}


fun SortOrder.toSortClause() =
    when (this) {
        SortOrder.DateAddedAsc -> "$SONG_DATE_ADDED ASC"
        SortOrder.DateAddedDesc -> "$SONG_DATE_ADDED DESC"
        SortOrder.NameAsc -> "$SONG_TITLE ASC"
        SortOrder.NameDesc -> "$SONG_TITLE DESC"
    }