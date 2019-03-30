package com.anou.prototype.core.common

object Constants {
    const val EMAIL_ADDRESS = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

    const val SPECIALS = "!@~<>,'`^;:_=?*+#.!&%$(){}" //all special characters
    const val All_UPPERCASE = "A-Z" //all uppercase letters
    const val All_LOWERCASE = "a-z" //all lowercase characters
    const val All_NUMBERS = "0-9" //all numbers

    const val PASSWORD_CHARS = "^(?=.*[$All_UPPERCASE])(?=.*[$All_LOWERCASE]).{2,}$" //a lower case letter must occur at least once,an upper case letter must occur at least once
    const val PASSWORD_COUNT = "[$All_LOWERCASE$All_UPPERCASE$All_NUMBERS$SPECIALS]{6,128}$" //at Least 6 characters
    const val PASSWORD_SPECIALS = "^(?=.*[$All_NUMBERS])(?=.*[$SPECIALS]).{2,}$" //# a digit must occur at least once, a special character must occur at least once
    const val PASSWORD_SEQUENCE = "^(?!.*(.)\\1{2,})[$All_LOWERCASE$All_UPPERCASE$All_NUMBERS$SPECIALS]{1,}$" //not contain sequences of more than < maxSameCharSequence > repeated characters.

}