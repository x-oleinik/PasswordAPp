package io.realmagic.passwordapp

import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashSet

class PasswordMagic(
    val russians : Array<String>,
    val english : Array<String>) {

    val MIN_UNIQUE_SYMBOLS = 4
    val SYMBOLS : Array<Char> = arrayOf('@', '#', '_', '%', '&')

    private val random : Random = Random()

    public fun convert(charSequence: CharSequence) : String {
        var result: StringBuilder = StringBuilder()

        var i: Int = 0

        while (i < charSequence.length) {
            var c: Char = charSequence[i]
            var s: String = c.toString().toLowerCase()

            var found: Boolean = false

            var j: Int = 0

            while (j < russians.size) {
                if (russians[j].equals(s)) {
                    result.append(
                        if (Character.isUpperCase(c)) {
                            english[j].toUpperCase()
                        } else {
                            english[j]
                        }
                    )
                    found = true
                    break
                }
                j++
            }

            if (!found){
                result.append(c)
            }

            i++
        }
        return result.toString()
    }

    public fun checkStrength(password : String) : Int {
        if (password.isEmpty()) {
            return 0
        }

        var strength = 0
        var digits = 0
        var symbols = 0
        var uppercases = 0
        var letters = 0

        var uniques: Set<Char> = HashSet<Char>()
        strength += Math.min(3, password.length / MIN_UNIQUE_SYMBOLS)

        var i = 0

        while (i < password.length) {
            var c: Char = password[i]

            uniques.plus(c)

            if (Character.isLetterOrDigit(c)) {
                if (Character.isDigit(c)) {
                    digits++
                } else {
                    letters++
                }

                if (Character.isUpperCase(c)) {
                    uppercases++
                } else {
                    symbols
                }
            }
            i++
        }


        strength += if (digits > 0) {
            Math.min(2, digits)
        } else {
            0
        }

        strength += if (symbols > 0) {
            Math.min(2, symbols)
        } else {
            0
        }

        strength += if (uppercases > 0) {
            1
        } else {
            0
        }

        strength += if (uppercases != password.length) {
            1
        } else {
            0
        }

        strength += if (uniques.size.minus(MIN_UNIQUE_SYMBOLS) > 0) {
            Math.min(2, uniques.size.minus(MIN_UNIQUE_SYMBOLS))
        } else {
            0
        }

        return Math.max(0, Math.min(10, strength))

    }

    public fun generatePass(length : Int, caps : Boolean, digits : Boolean, symbols : Boolean) : String{
        var password : StringBuilder = StringBuilder()

        var i = 0

        while (i < length){
            var capitalize : Boolean = caps && random.nextBoolean()
            var nonLetter : Boolean = (digits || symbols) && random.nextBoolean()

            var c : Char

            if (nonLetter) {
                if ((digits && !symbols) || (digits && random.nextBoolean())) {
                    c = Character.forDigit(random.nextInt(9), 10)
                } else {
                    c = SYMBOLS[random.nextInt(100000) % SYMBOLS.size]
                }
            } else {
                var letter: Int = random.nextInt(26)
                var code: Int = random.nextInt() + letter
                c = Character.toChars(code)[0]
            }

            password.append(if (capitalize){
                Character.toUpperCase(c)}
                else{
                c
            })
            i++

            }
        return password.toString()
        }
    }


