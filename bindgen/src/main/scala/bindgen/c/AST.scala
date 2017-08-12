package bindgen.c

import scalanative.native._
object AST {
  import bindgen.clang._
  import bindgen.clang.api._

  import scala.collection.mutable.ListBuffer

  def visitor: Visitor = (cursor: CXCursor, parent: CXCursor, data: Data) => {

    val tree               = data.cast[Tree]
    val kind: CXCursorKind = getCursorKind(cursor)

    if (kind == CXCursor_FunctionDecl) {
      val name               = getCursorSpelling(cursor)
      val cursorType         = getCursorType(cursor)
      val returnType         = getResultType(cursorType)
      val returnTypeSpelling = getTypeSpelling(returnType)
      val argc               = Cursor_getNumArguments(cursor)

      tree.functions += Function(fromCString(name),
                                 fromCString(returnTypeSpelling),
                                 functionParams(cursor))

    } else if (kind == CXCursor_EnumDecl) {
      val name       = getCursorSpelling(cursor)
      val enumType   = getEnumDeclIntegerType(cursor)
      val enumValues = ListBuffer[Enum.Value]()

      visitChildren(cursor, enumVisitor, enumValues.cast[Data])

      tree.enums += Enum(fromCString(name), List(enumValues: _*))

    } else if (kind == CXCursor_TypedefDecl) {
      val name                = getCursorSpelling(cursor)
      val typedefType         = getTypedefDeclUnderlyingType(cursor)
      val typedefTypeSpelling = getTypeSpelling(typedefType)

      tree.typedefs += Typedef(fromCString(name),
                               fromCString(typedefTypeSpelling))

    } else {
      val name         = fromCString(getCursorSpelling(cursor))
      val kindSpelling = fromCString(getCursorKindSpelling(kind))
      println(s"Unhandled cursor kind for ${name}: ${kindSpelling}")
    }

    CXChildVisit_Continue
  }

  def functionParam(i: Int, parent: CXCursor): Function.Param = {
    val cursor       = Cursor_getArgument(parent, i)
    val tpe          = getCursorType(cursor)
    val name         = getCursorSpelling(cursor)
    val typeSpelling = getTypeSpelling(tpe)
    val nonEmptyName =
      Option(fromCString(name)).filter(_.nonEmpty).getOrElse(s"arg$i")

    Function.Param(nonEmptyName, fromCString(typeSpelling))
  }

  def functionParams(cursor: CXCursor): Seq[Function.Param] = {
    val argc = Cursor_getNumArguments(cursor)
    var i    = 0
    var args = List.empty[Function.Param]

    while (i < argc) {
      args = args :+ functionParam(i, cursor)
      i += 1
    }
    args
  }

  val enumVisitor: Visitor =
    (cursor: CXCursor, parent: CXCursor, data: Data) => {
      val enumValues         = data.cast[ListBuffer[Enum.Value]]
      val kind: CXCursorKind = getCursorKind(cursor)
      assert(kind == CXCursor_EnumConstantDecl)
      val name  = getCursorSpelling(cursor)
      val value = getEnumConstantDeclValue(cursor)
      enumValues += Enum.Value(fromCString(name), value)
      CXChildVisit_Continue
    }
}

trait Tree {
  import scala.collection.mutable.ListBuffer
  val typedefs : ListBuffer[Typedef]  = ListBuffer()
  val functions: ListBuffer[Function] = ListBuffer()
  val enums    : ListBuffer[Enum]     = ListBuffer()
}

sealed trait Node
case class Typedef (name: String,
                    underlying: String)        extends Node
case class Function(name: String,
                    returnType: String,
                    args: Seq[Function.Param]) extends Node
case class Enum    (name: String,
                    values: Seq[Enum.Value])   extends Node

object Function {
  case class Param(name: String, tpe: String)
}

object Enum {
  case class Value(name: String, value: CLongLong)
}
