//     Project: scala-bindgen
//      Module:
// Description:
package bindgen.objc

import java.io.{File, FileWriter, StringWriter, Writer}

import bindgen.objc.AST.{GlobalFunction, ScalaType, ParamDecl}
import slogging.LazyLogging

object Generator extends LazyLogging {


  def generateModulesAsString(pkgName: Option[String], modules: Iterable[AST.TranslationUnit]): String = {
    val w = new StringWriter()
    generateModules(pkgName,modules,w,w)
    w.toString
  }

  def generateModules(pkgName: Option[String], modules: Iterable[AST.TranslationUnit], file: String): Unit = {
    val w = new FileWriter(file)
    generateModules(pkgName,modules,w,w)
    w.close()
  }

  def generateModules(pkgName: Option[String], modules: Iterable[AST.TranslationUnit],
                      moduleWriter: Writer, pkgWriter: Writer): Unit = {
    genScalaModule(pkgName,modules)(moduleWriter)
    genScalaPkgObject(pkgName,modules)(pkgWriter)
  }

  private def genScalaModule(pkgName: Option[String], modules: Iterable[AST.TranslationUnit])(implicit w: Writer): Unit = {
    genModuleHeader(pkgName)
    AST.mergeInterfaces( modules.flatMap(_.interfaces) ).foreach( genInterface )
    w.writeln()
  }

  private def genScalaPkgObject(pkgName: Option[String], modules: Iterable[AST.TranslationUnit])(implicit w: Writer): Unit = {
    val name =
      if(pkgName.isEmpty) {
        logger.warn("no package defined; using 'scalanative.native.bindings' for package object")
        "scalanative.native.bindings"
      }
      else pkgName.get
    val functions = modules.flatMap(_.functions)
    val structs = modules.flatMap(_.structs)
    genPkgObject(name,functions,structs)
  }

  private def genInterface(iface: AST.Interface)(implicit w: Writer): Unit = {
    import iface._
    val (instMethods,clsMethods) = methods.partition(_.isInstance)
    genClass(name,isProtocol,isCategory,instMethods,superClass,protocols,typeParams)
    genObject(name,clsMethods)
  }

  private def genModuleHeader(pkgName: Option[String])(implicit w: Writer): Unit = {
    if(pkgName.isDefined)
      w.writeln(s"package ${pkgName.get}")
    w.writeln()
    w.writeln(
      s"""import scalanative.native._
         |import objc.ObjC
         |import objc.runtime._
       """.stripMargin)
    w.writeln()
  }

  private def genClass(name: String,
                       isProtocol: Boolean,
                       isCategory: Boolean,
                       methods: Seq[AST.Method],
                       baseClass: Option[ScalaType],
                       protocols: Seq[ScalaType],
                       typeParams: Seq[ScalaType])(implicit w: Writer): Unit = {
    val implements = baseClass.toSeq ++ protocols
    val tpe = (if(isProtocol||isCategory) s"trait $name" else s"class $name") +
      (if(typeParams.isEmpty) "" else typeParams.mkString("[",",","]")) +
      (if(implements.isEmpty) "" else implements.mkString(" extends "," with ",""))
    w.writeln(
      s"""@ObjC
         |$tpe {""".stripMargin)
    methods.foreach(genMethod)
    w.writeln("}")
    w.writeln()
  }

  private def genMethod(method: AST.Method)(implicit w: Writer): Unit = {
    val scalaName = genScalaName(method.name,method.args)
    
    val args = method.args.map(_.toScala).mkString(", ")
    val retType = genReturnType(method)
    w.writeln(s"  @inline def $scalaName($args): $retType = extern")
  }

  private def genObject(name: String, methods: Seq[AST.Method])(implicit w: Writer): Unit = {
    if(methods.isEmpty)
      return
    w.writeln(
      s"""object $name {""".stripMargin)
    methods.foreach(genMethod)
    w.writeln("}")
    w.writeln()
  }

  private def genPkgObject(pkgName: String,
                           functions: Iterable[GlobalFunction],
                           structs: Iterable[AST.StructDecl])(implicit w: Writer): Unit = {
    if(functions.isEmpty && structs.isEmpty)
      return
    val (pkg,objname) = pkgName.split("\\.") match {
      case p => (p.init.mkString("."),p.last)
    }
    w.writeln(
      s"""package $pkg
         |
         |import scalanative.native._
         |import objc.runtime._
         |
         |@extern
         |object $objname {""".stripMargin)

    structs.foreach(genStruct)
    functions.foreach(genFunction)
    w.writeln("}")
  }

  private def genFunction(function: GlobalFunction)(implicit w: Writer): Unit = {
    import function._

    val scalaName = genScalaName(name,args)
    w.writeln(s"  def $scalaName(${args.map(_.toScala).mkString(", ")}): $returnType = extern")
  }

  private def genStruct(struct: AST.StructDecl)(implicit w: Writer): Unit = {
    w.writeln(struct.toScala)
  }

  private def genReturnType(method: AST.Method): String = method.returnType.name match {
    case "instancetype" =>
      method.parent.get.scalaType.toString
    case x => method.returnType.toString
  }

  private def genSelectorString(name: String, args: Seq[ParamDecl]): String = args.size match {
    case 0 => name
    case 1 => name + ":"
    case _ => (name +: (args.tail.map(_.name))).mkString(":") + ":"
  }

  private def genScalaNameFromSelectorString(selectorString: String): String = selectorString.replaceAll(":","_")

  private def genScalaName(name: String, args: Seq[ParamDecl]): String = genScalaNameFromSelectorString( genSelectorString(name,args) )

  implicit final class RichWriter(val w: Writer) extends AnyVal {
    @inline def writeln(s: String): Unit = w.write(s+"\n")
    @inline def writeln(): Unit = w.write("\n")
  }


}
