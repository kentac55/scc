import Tokens._

import scala.util.matching.Regex
import io.StdIn
import scala.util.parsing.combinator.RegexParsers

object SccParser extends RegexParsers with App {
  def intRegex: Regex = "\\d+".r
  // TODO: generate from ENUM
  def intUnaryOperatorRegex: Regex = "\\+|\\-".r
  def intBinaryOperatorsRegex: Regex = "\\+|\\-|\\*|/".r

  def intLiteralExpression: Parser[IntLiteralExpression] = intRegex ^^ {
    value =>
      Option(Integer.parseInt(value)) match {
        case Some(v) => IntLiteralExpression(v)
        case None    => throw new Exception(s"$value is not acceptable")
      }
  }
  def intUnaryExpression: Parser[IntUnaryExpression] =
    intUnaryOperatorRegex ~ intLiteralExpression ^^ {
      case op ~ literal =>
        IntUnaryExpression(IntUnaryOperator.withName(op), literal)
    }
  def intBinaryExpression: Parser[IntBinaryExpression] =
    intLiteralExpression ~ intBinaryOperatorsRegex ~ intLiteralExpression ^^ {
      case left ~ op ~ right =>
        IntBinaryExpression(IntBinaryOperator.withName(op), left, right)
    }
  def intParser: Parser[Expression] =
    intBinaryExpression | intUnaryExpression | intLiteralExpression

  def genAsm(expression: Expression): String = {
    "  .global main\n" +
      "main:\n" +
      expression.genExpr +
      "  ret"
  }

  val source = StdIn.readLine()
  val expr = parse(intParser, source) match {
    case Success(matched, _) => matched
    case Failure(msg, _)     => throw new Exception(s"FAILURE: $msg")
    case Error(msg, _)       => throw new Exception(s"ERROR: $msg")
  }
  println(genAsm(expr))
}
