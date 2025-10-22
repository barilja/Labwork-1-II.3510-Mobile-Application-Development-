package com.tumme.scrudstudents.data.local.model

enum class Gender(val value: String) {//class of the gender useful to the students database
    Male("Male"),
    Female("Female"),
    NotConcerned("Not concerned");

    companion object {
        /*
          Safely converts a string to a Gender enum.
          It searches through all enum entries and finds the first one whose `value` property matches the input string.
          If no match is found (e.g., the input is an invalid string or null), it uses the Elvis operator (?:)
          to return `NotConcerned` as a safe default value, preventing crashes from unexpected input.
         */
        fun from(value: String) = Gender.entries.firstOrNull { it.value == value } ?: NotConcerned
    }
}
