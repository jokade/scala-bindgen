//     Project: scala-bindgen
//      Module:
// Description:
package bindgen.objc

import java.io.File

import bindgen.clang._
import bindgen.objc.AST.TranslationUnit
import bindgen.{Args, Error}
import slogging.LazyLogging

import scala.scalanative.native
import scala.scalanative.native._

object Parser extends LazyLogging {

  def parse(args: Args, clangArgs: Array[String]): Seq[AST.TranslationUnit] = {
    val cargs = (clangArgs :+ "-ObjC") ++
      args.clangArgs ++
      args.includeDirs.map(dir => s"-I$dir") ++
      args.frameworkIncludeDirs.map(dir => s"-F$dir")
    args.files.map(parseFile(_,args,cargs))
  }

  private def parseFile(file: String, args: Args, clangArgs: Array[String]): AST.TranslationUnit = Zone{ implicit z =>
    if(!new File(file).canRead)
      Error(s"cannot read file $file")

    val cargc: CInt = clangArgs.length
    val cargv: Ptr[CString] = stackalloc[CString](clangArgs.length)
    var i = 0; while(i<cargc) { cargv(i) = toCString(clangArgs(i)); i = i + 1 }

    logger.info(s"parsing '$file'")

    val index = api.createIndex(0, 1)
    val tu = native.stackalloc[CXTranslationUnit]
    val code = api.parseTranslationUnit2(
      index,
      toCString(file),
      cargv,
      cargc,
      null,
      0,
      api.CXTranslationUnit_SkipFunctionBodies(),
      tu)

    val ast = code match {
      case CXErrorCode.Success =>
        val diags = getDiagnostics(!tu)
        if(diags.exists(_.isError))
          Error(s"errors during parsing of '$file'")
        parseAST(!tu)
      case CXErrorCode.InvalidArguments =>
        Error(s"invalid arguments during clang call: ${clangArgs mkString " "}")
      case CXErrorCode.ASTReadError =>
        Error(s"error during clang call (arguments: ${clangArgs mkString " "}")
    }

    api.disposeTranslationUnit(!tu)
    ast
  }

  private def parseAST(tu: CXTranslationUnit): AST.TranslationUnit = {
    logger.debug("parsing AST")
    val cursor = api.getTranslationUnitCursor(tu)
    if(cursor.kind != CXCursorKind.TranslationUnit)
      Error(s"invalid cursor kind: ${cursor.kind} (expected: ${CXCursorKind.TranslationUnit})")

    TranslationUnit.parse(tu.translationUnitSpelling.string,cursor)
  }

  private def getDiagnostics(tu: CXTranslationUnit): Seq[Diagnostic] =
    for(i <- 0 to api.getNumDiagnostics(tu).toInt)
    yield getDiagnostic(tu,i)

  private def getDiagnostic(tu: CXTranslationUnit, index: Int): Diagnostic = {
    val diag = api.getDiagnostic(tu,index)
    val msg = api.formatDiagnostic(diag).string
    val severity = api.getDiagnosticSeverity(diag)
    api.disposeDiagnostic(diag)
    Diagnostic(severity,msg)
  }

  case class Diagnostic(severity: CXDiagnosticSeverity, message: String) {
    def isError: Boolean = severity > CXDiagnosticSeverity.Warning
  }

}
