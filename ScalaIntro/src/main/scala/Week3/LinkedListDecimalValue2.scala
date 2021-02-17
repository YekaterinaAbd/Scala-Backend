package Week3

class ListNode(_x: Int = 0, _next: ListNode = null) {
  var next: ListNode = _next
  var x: Int = _x
}

object LinkedListDecimalValue2 extends App {
  def getDecimalValue(head: ListNode): Int = {
    var binaryString = ""
    var curr = head
    while (curr != null) {
      binaryString += curr.x
      curr = curr.next
    }
    Integer.parseInt(binaryString, 2)
  }
}
