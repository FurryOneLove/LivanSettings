package ru.who.livansetting.data

data class PredefinedIntent(val name: String, val action: String) {
    companion object {
        val ALL = listOf(
            PredefinedIntent("Intent 1", "ru.who.livansetting.CUSTOM_INTENT_1"),
            PredefinedIntent("Intent 2", "ru.who.livansetting.CUSTOM_INTENT_2"),
            PredefinedIntent("Intent 3", "ru.who.livansetting.CUSTOM_INTENT_3"),
            PredefinedIntent("Intent 4", "ru.who.livansetting.CUSTOM_INTENT_4"),
            PredefinedIntent("Intent 5", "ru.who.livansetting.CUSTOM_INTENT_5"),
            PredefinedIntent("Intent 6", "ru.who.livansetting.CUSTOM_INTENT_6"),
            PredefinedIntent("Intent 7", "ru.who.livansetting.CUSTOM_INTENT_7"),
            PredefinedIntent("Intent 8", "ru.who.livansetting.CUSTOM_INTENT_8"),
            PredefinedIntent("Intent 9", "ru.who.livansetting.CUSTOM_INTENT_9"),
            PredefinedIntent("Intent 10", "ru.who.livansetting.CUSTOM_INTENT_10")
        )
    }
}
