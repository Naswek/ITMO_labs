object Colors {
  val RED    = "\u001b[31m"
  val GREEN  = "\u001b[32m"
  val YELLOW = "\u001b[33m"
  val BLUE   = "\u001b[34m"
  val RESET  = "\u001b[0m"

  def printError(msg: String): Unit = println(s"$RED$msg$RESET")
  def printSuccess(msg: String): Unit = println(s"$GREEN$msg$RESET")
  def printWarning(msg: String): Unit = println(s"$YELLOW$msg$RESET")
  def printInfo(msg: String): Unit = println(s"$BLUE$msg$RESET")
}