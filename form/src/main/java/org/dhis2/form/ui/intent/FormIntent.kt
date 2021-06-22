package org.dhis2.form.ui.intent

import org.dhis2.form.mvi.MviIntent

sealed class FormIntent : MviIntent {
    data class SelectDateFromAgeCalendar(
        val uid: String,
        val date: String?
    ) : FormIntent()

    data class ClearDateFromAgeCalendar(
        val uid: String
    ) : FormIntent()

    data class SelectLocationFromCoordinates(
        val uid: String,
        val coordinates: String?,
        val extraData: String
    ) : FormIntent()

    data class SelectLocationFromMap(
        val uid: String,
        val featureType: String,
        val coordinates: String?
    ) : FormIntent()
}
