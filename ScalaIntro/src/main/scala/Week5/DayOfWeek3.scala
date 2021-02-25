package Week5

import Week5.Date.weekDay
import java.util.Calendar

case class Date(year: Int, month: Int, day: Int)

object Date {
  private def dayByIndex(index: Int): String = {
    val daysOfTheWeek = List("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    daysOfTheWeek(index)
  }

  def weekDay(date: Date): String = {
    val calendar = Calendar.getInstance()
    calendar.set(date.year, date.month - 1, date.day)
    dayByIndex(calendar.get(Calendar.DAY_OF_WEEK) - 1)
  }
}

object Solution3 {

  def dayOfTheWeek(day: Int, month: Int, year: Int): String = {
    val date = Date(year, month, day)
    weekDay(date)
  }
}

object DayOfWeek3 extends App {
  println(Solution3.dayOfTheWeek(31, 8, 2019))
}
