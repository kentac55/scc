import enumeratum._

object Tokens {

  sealed trait Operator[T] extends EnumEntry
  sealed trait UnaryOperator[T] extends Operator[T]
  sealed trait BinaryOperator[T] extends Operator[T]

  type IntUnaryOperator = UnaryOperator[Int]
  type IntBinaryOperator = BinaryOperator[Int]

  object IntUnaryOperator extends Enum[IntUnaryOperator] {
    val values: IndexedSeq[IntUnaryOperator] = findValues

    case object + extends IntUnaryOperator
    case object - extends IntUnaryOperator
  }

  object IntBinaryOperator extends Enum[IntBinaryOperator] {
    val values: IndexedSeq[IntBinaryOperator] = findValues

    case object + extends IntBinaryOperator
    case object - extends IntBinaryOperator
    case object * extends IntBinaryOperator
    case object / extends IntBinaryOperator
  }

  trait Expression {
    val genExpr: String
  }

  sealed trait LiteralExpression[T] extends Expression {
    val value: T
  }

  sealed trait UnaryExpression[T] extends Expression {
    val operator: UnaryOperator[T]
    val operand: LiteralExpression[T]
  }

  sealed trait BinaryExpression[T] extends Expression {
    val operator: BinaryOperator[T]
    val left: LiteralExpression[T]
    val right: LiteralExpression[T]
  }

  sealed case class IntLiteralExpression(value: Int)
      extends LiteralExpression[Int] {
    override val genExpr: String = s"  movq $$${value}, %rax\n"
  }

  sealed case class IntUnaryExpression(operator: UnaryOperator[Int],
                                       operand: IntLiteralExpression)
      extends UnaryExpression[Int] {
    override val genExpr: String = operator match {
      case IntUnaryOperator.+ => s"  movq $$${operand.value}, %rax\n"
      case IntUnaryOperator.- => s"  movq $$-${operand.value}, %rax\n"
      case _                  => throw new Error("genExpr: Unknown unary op")
    }
  }

  sealed case class IntBinaryExpression(operator: BinaryOperator[Int],
                                        left: IntLiteralExpression,
                                        right: IntLiteralExpression)
      extends BinaryExpression[Int] {
    override val genExpr: String = {
      s"  movq $$${left.value}, %rax\n" + s"  movq $$${right.value}, %rcx\n" + (operator match {
        case IntBinaryOperator.+ => "  addq %rcx, %rax\n"
        case IntBinaryOperator.- => "  subq %rcx, %rax\n"
        case IntBinaryOperator.* => "  imulq %rcx, %rax\n"
        case IntBinaryOperator./ => "  movq $0, %rdx\n" + "  idiv %rcx\n"
        case _                   => throw new Error("genExpr: Unknown unary op")
      })
    }
  }
}
