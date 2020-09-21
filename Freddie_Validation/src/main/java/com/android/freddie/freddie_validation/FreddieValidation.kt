package com.android.freddie.freddie_validation

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object FreddieValidation {

    /** 메일주소 형식 */
    fun isValidEmail(email: String?): Boolean {
        if (email == null) return false
        return Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])").matches(email)
    }

    /** 비밀번호 : 영문 + 숫자 8자 이상 */
    fun isValidPasswordEasy(password: String): Boolean {
        return Pattern.matches("^(?=.*?[a-z])(?=.*?[0-9]).{8,}\$", password)
    }

    /** 비밀번호 : 대소문자 + 숫자 */
    fun isValidPasswordNormal(password: String): Boolean {
        return Pattern.matches("^(?=.*?[A-Za-z])(?=.*?[0-9]).{6,}\$", password)
    }

    /** 비밀번호 : 대소문자 + 숫자 + 특수문자 */
    fun isValidPasswordStandard(password: String): Boolean {
        return Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{6,}\$", password)
    }

    /** 한국 폰번호 */
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return Pattern.matches("(01[016789])(\\d{3,4})(\\d{4})", phoneNumber)
    }

    /** 한국 일반전화 */
    fun isValidCallNumber(callNumber: String): Boolean {
        return Pattern.matches("^(0(2|3[13]|4[14]|5[15]|6[14]))(\\d{3,4})(\\d{4})\$", callNumber)
    }

    /** 한국 이름 4자리까지만 */
    fun isValidKoreanName(name: String): Boolean {
        return Pattern.matches("^[가-힣]{2,4}\$", name)
    }

    /** 한국 차번호 */
    fun isValidCarNumber(carNum: String?): Boolean {
        var returnValue = false
        return try {
            var regex = "^\\d{2}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}/*$"
            var p = Pattern.compile(regex)
            var m: Matcher = p.matcher(carNum)
            val regex2 = "^\\d{3}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}/*$"
            val p2 = Pattern.compile(regex2)
            val m2: Matcher = p2.matcher(carNum)
            if (m.matches()) {
                returnValue = true
            } else if (m2.matches()) {
                returnValue = true
            } else {
                //2번째 패턴 처리
                regex = "^[서울|부산|대구|인천|대전|광주|울산|제주|경기|강원|충남|전남|전북|경남|경북|세종]{2}\\d{2}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}$"
                p = Pattern.compile(regex)
                m = p.matcher(carNum)
                if (m.matches()) {
                    returnValue = true
                }
            }
            returnValue
        } catch (e: Exception) {
            false
        }
    }

    /** 12월을 초과하는 값이거나 해당월의 마지막 날짜를 넘는지 확인합니다. */
    private fun isValidDate(year: Int, month: Int, day: Int): Boolean {
        if (month > 12 || month < 1) {
            return false
        }
        val now = Calendar.getInstance()
        now.set(year, month - 1, 1)
        val maxDayOfMonth: Int = now.getActualMaximum(Calendar.DAY_OF_MONTH)
        if (day > maxDayOfMonth || day < 1) {
            return false
        }
        return true
    }

    /** 올바른 날짜 형식인가 && 실제 있는 날인가 && 현재 날짜보다 미래일 수 없음(생일 이므로) */
    fun isValidDateAndBirthDay(birthString: String): Boolean {
        if (birthString.length < 6) return false
        val year: Int = birthString.substring(0, 2).toInt()
        val month: Int = birthString.substring(2, 4).toInt()
        val day: Int = birthString.substring(4, 6).toInt()
        if (!isValidDate(year, month, day)) {
            return false
        }

        val birthDate: Date = SimpleDateFormat("yyMMdd", Locale.getDefault()).parse(birthString)
        val birth: Calendar = Calendar.getInstance().apply {time = birthDate}
        val birthYear: Int = birth.get(Calendar.YEAR)
        val birthMonth: Int = birth.get(Calendar.MONTH)
        val birthDay: Int = birth.get(Calendar.DAY_OF_MONTH)

        val now: Calendar = Calendar.getInstance()
        val currentYear: Int = now.get(Calendar.YEAR)
        val currentMonth: Int = now.get(Calendar.MONTH)
        val currentDay: Int = now.get(Calendar.DAY_OF_MONTH)
        if (birthYear > currentYear) {
            return false
        } else if (birthYear == currentYear) {
            if (birthMonth > currentMonth) {
                return false
            } else if (birthMonth == currentMonth) {
                if (birthDay > currentDay) {
                    return false
                }
            }
        }
        return true
    }

    private val LOGIC_NUM = intArrayOf(1, 3, 7, 1, 3, 7, 1, 3, 5, 1)

    /** 사업자 번호 유효성 검사 */
    fun isValidBusinessId(regNum: String): Boolean {
        if (!isNumeric(regNum) || regNum.length != 10) return false
        var sum = 0
        var j = -1
        for (i in 0..8) {
            j = Character.getNumericValue(regNum[i])
            sum += j * LOGIC_NUM[i]
        }
        sum += (Character.getNumericValue(regNum[8]) * 5 / 10)
        val checkNum = (10 - sum % 10) % 10
        return checkNum == Character.getNumericValue(regNum[9])
    }

    private fun isNumeric(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val sz = str.length
        for (i in 0 until sz) {
            if (!Character.isDigit(str[i])) {
                return false
            }
        }
        return true
    }

    /** 같은 날짜인지 */
    fun isSameDate(date1: String, date2: String): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd")
        var day1: Date? = null
        var day2: Date? = null
        try {
            day1 = format.parse(date1)
            day2 = format.parse(date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val compare = day1!!.compareTo(day2)
        return if (compare > 0) {
            false
        } else compare >= 0
    }
}