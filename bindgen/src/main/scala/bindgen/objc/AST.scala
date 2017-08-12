//     Project: scala-bindgen
//      Module:
// Description:
package bindgen.objc

import bindgen.Error
import bindgen.clang._
import slogging.LazyLogging

import scala.scalanative.native._

object AST extends LazyLogging {

  sealed trait Node {
    def name: String
    def parent: Option[Node]
  }


  case class TranslationUnit(name: String,
                             var interfaces: Seq[Interface] = Nil,
                             var functions: Seq[GlobalFunction] = Nil,
                             var structs: Seq[StructDecl] = Nil) extends Node {
    final def parent = None
  }
  object TranslationUnit extends LazyLogging {
    def parse(name: String, c: CXCursor): TranslationUnit = {
      var decls = Seq.empty[Interface]

      val tu = TranslationUnit("name")

      c.visitChildren(tu.cast[Data]){(child: CXCursor, parent: CXCursor, data: Data) =>
        val tu = data.cast[TranslationUnit]
        child match {
          case Interface(p) =>
            p.parent = Some(tu)
            tu.interfaces :+= p
          case GlobalFunction(f) =>
            f.parent = Some(tu)
            tu.functions :+= f
          case StructDecl(s) =>
            s.parent = Some(tu)
            tu.structs :+= s
          case _ if
             child.kind == CXCursorKind.ObjCClassRef ||
             child.kind == CXCursorKind.ObjCProtocolRef => ()
          case x =>
            logger.warn("ignoring {} {}",x.cursorKindSpelling,x.cursorSpelling)
        }
        CXChildVisitResult.Continue
      }

      tu
    }

  }

  case class Interface(var name: String,
                       isProtocol: Boolean,
                       isCategory: Boolean,
                       isMerged: Boolean = false,
                       var typeParams: Seq[ScalaType] = Nil,
                       var superClass: Option[ScalaType] = None,
                       var protocols: Seq[ScalaType] = Nil,
                       var methods: Seq[Method] = Nil) extends Node {
    var parent: Option[TranslationUnit] = None
    def merge(other: Interface): Interface =
      if(other.name != name)
        Error(s"cannot merge $name with ${other.name}")
      else
        copy(
          isProtocol = this.isProtocol && other.isProtocol,
          isMerged = true,
          methods = methods ++ other.methods )

    def scalaType: ScalaType = ScalaType(name,typeParams)
  }
  object Interface extends LazyLogging {
    def unapply(c: CXCursor): Option[Interface] =
      if(c.kind == CXCursorKind.ObjCInterfaceDecl ||
         c.kind == CXCursorKind.ObjCProtocolDecl ||
         c.kind == CXCursorKind.ObjCCategoryDecl) Some(parse(c))
      else None

    def parse(c: CXCursor): Interface = {
      val isPrototcol = c.kind == CXCursorKind.ObjCProtocolDecl
      val isCategory = c.kind == CXCursorKind.ObjCCategoryDecl
      val name = c.cursorSpelling

      logger.debug("parsing {} {}",c.cursorKindSpelling,name)
      val p = Interface(name,isPrototcol,isCategory)

      c.visitChildren(p.cast[Data]){ (child:CXCursor,parent:CXCursor,data:Data) =>
        val p = data.cast[Interface]
        child match {
          case Method(m) =>
            m.parent = Some(p)
            p.methods :+= m
          case ObjCSuperClassRef(ref) => p.superClass = Some(ref)
            // if we get an ObjCClassRef here, we're actually extending the class in this reference
          case ObjCClassRef(ref) => p.name = ref.name
          case ObjCProtocolRef(ref) => p.protocols :+= ref
          case TemplateTypeParameter(tpe) => p.typeParams :+= tpe
          case _ if
            // property declarations are expanded to methods by clang,
            // so we can ignore them here
            child.kind == CXCursorKind.ObjCPropertyDecl => ()
          case x => logger.warn("ignoring {} {}",x.cursorKindSpelling,x.cursorSpelling)
        }
        CXChildVisitResult.Continue
      }
      logger.debug("end of {} {}",c.cursorKindSpelling,c.cursorSpelling)
      p
    }
  }

  sealed trait Callable extends Node
  case class Method(name: String,
                    isInstance: Boolean,
                    returnType: ScalaType,
                    var args: Seq[ParamDecl] = Nil) extends Callable {
    var parent: Option[Interface] = None
  }
  object Method extends LazyLogging {
    def unapply(c: CXCursor): Option[Method] =
      if(c.kind == CXCursorKind.ObjCInstanceMethodDecl ||
        c.kind == CXCursorKind.ObjCClassMethodDecl) Some(parse(c))
      else None

    def parse(c: CXCursor): Method = {
      val name = mapName(methodName(c.cursorSpelling))
      val isInstance = c.kind == CXCursorKind.ObjCInstanceMethodDecl
      logger.trace("parsing method declaration {}",(if(isInstance) "-" else "+") + name)

      val returnType = ScalaType( c.cursorResultType )
      val m = Method(name,isInstance,returnType)
      c.visitChildren(m.cast[Data]){ (child:CXCursor,parent:CXCursor,data:Data) =>
        val m = data.cast[Method]
        child match {
          case ParamDecl(decl) =>
            decl.parent = Some(m)
            m.args :+= decl
          case x if
            x.kind == CXCursorKind.TypeRef ||
            x.kind == CXCursorKind.ObjCClassRef => ()
          case x =>
            logger.warn("ignoring {} {}",child.cursorKindSpelling,child.cursorSpelling)
        }
        CXChildVisitResult.Continue
      }
      logger.trace("end of method {}",name)
      m
    }
  }

  case class GlobalFunction(name: String,
                            returnType: ScalaType,
                            var args: Seq[ParamDecl] = Nil) extends Callable {
    var parent: Option[TranslationUnit] = None
  }
  object GlobalFunction extends LazyLogging {
    def unapply(c: CXCursor): Option[GlobalFunction] =
      if(c.kind == CXCursorKind.FunctionDecl) Some(parse(c))
      else None

    def parse(c: CXCursor): GlobalFunction = {
      val name = mapName(c.cursorSpelling)
      logger.trace("parsing function declaration {}",name)
      val retType = ScalaType( c.cursorResultType )
      val f = GlobalFunction(name,retType)

      c.visitChildren(f.cast[Data]){ (child:CXCursor,parent:CXCursor,data:Data) =>
        val f = data.cast[GlobalFunction]
        child match {
          case ParamDecl(decl) =>
            decl.parent = Some(f)
            f.args :+= decl
          case x if
            x.kind == CXCursorKind.TypeRef ||
            x.kind == CXCursorKind.ObjCClassRef => ()
          case x =>
            logger.warn("ignoring {} {}",child.cursorKindSpelling,child.cursorSpelling)
        }
        CXChildVisitResult.Continue
      }
      logger.trace("end of function {}",name)
      f
    }
  }


  case class ParamDecl(name: String, var tpe: ScalaType = null) extends Node {
    var parent: Option[Callable] = None
    def toScala: String = s"${mapName(name)}: $tpe"
  }
  object ParamDecl extends LazyLogging {
    def unapply(c: CXCursor): Option[ParamDecl] =
      if(c.kind == CXCursorKind.ParmDecl) Some(parse(c))
      else None

    def parse(c: CXCursor): ParamDecl = {
      val name = c.cursorSpelling
      logger.trace("parsing parameter declaration {}",name)
      val p = ParamDecl(name)
      c.visitChildren(p.cast[Data]){ (child: CXCursor, parent: CXCursor, data: Data) =>
        val p = data.cast[ParamDecl]
        child match {
          case TypeRef(tpe) => p.tpe = tpe
          case ObjCClassRef(cref) => p.tpe = cref
          case x =>
            logger.warn("ignoring {} {}",child.cursorKindSpelling,child.cursorSpelling)
        }
        CXChildVisitResult.Continue
      }
      if(p.tpe == null)
        p.tpe = ScalaType( c.cxtype )
      logger.trace("end of parameter declaration {}",name)
      p
    }
  }

  object TypeRef extends LazyLogging {
    def unapply(c: CXCursor): Option[ScalaType] =
      if(c.kind == CXCursorKind.TypeRef) {
        val name = c.cursorSpelling
        logger.trace("parsing TypeRef {}",name)
        Some( ScalaType(name) )
      }
      else None
  }

  object ObjCClassRef extends LazyLogging {
    def unapply(c: CXCursor): Option[ScalaType] =
      if(c.kind == CXCursorKind.ObjCClassRef) {
        val name = c.cursorSpelling
        logger.trace("parsing ObjCClassRef {}",name)
        Some( ScalaType(name) )
      }
      else None
  }

  object ObjCSuperClassRef extends LazyLogging {
    def unapply(c: CXCursor): Option[ScalaType] =
      if(c.kind == CXCursorKind.ObjCSuperClassRef) {
        val ref = ScalaType( c.cxtype )
        logger.trace("parsing ObjCSuperClassRef {}",ref)
        Some(ref)
      }
      else None
  }

  object ObjCProtocolRef extends LazyLogging {
    def unapply(c: CXCursor): Option[ScalaType] =
      if(c.kind == CXCursorKind.ObjCProtocolRef) {
        val name = c.cursorSpelling
        logger.trace("parsing ObjCProtoclRef {}",name)
        Some( ScalaType(name) )
      }
      else None
  }

  object TemplateTypeParameter extends LazyLogging {
    def unapply(c: CXCursor): Option[ScalaType] =
      if(c.kind == CXCursorKind.TemplateTypeParameter) {
        val name = c.cursorSpelling
        logger.trace("parsing TemplateTypeParameter {}",name)
        Some( ScalaType(name) )
      }
      else None
  }

  case class StructDecl(name: String) extends Node {
    var parent: Option[TranslationUnit] = None
    def toScala: String = s"  type $name = Ptr[Byte]"
  }
  object StructDecl extends LazyLogging {
    def unapply(c: CXCursor): Option[StructDecl] =
      if(c.kind == CXCursorKind.StructDecl) Some(parse(c))
      else None

    def parse(c: CXCursor): StructDecl = {
      val name = c.cursorSpelling
      logger.trace("parsing struct declaration {}",name)
      val s = StructDecl(name)
      logger.trace("end of struct declaration {}",name)
      s
    }
  }

  case class ScalaType(name: String, args: Seq[ScalaType] = Nil) {
    override def toString(): String =
      if(args.isEmpty)
        mapName(name)
      else mapName(name) + args.mkString("[",", ","]")
  }
  object ScalaType extends LazyLogging {

    def ptr(tpe: ScalaType): ScalaType = ScalaType("Ptr", Seq(tpe))
    val Byte: ScalaType = ScalaType("Byte")
    val ptrByte: ScalaType = ptr(Byte)

    def apply(name: String, scalaType: ScalaType): ScalaType = ScalaType(name,Seq(scalaType))

    def apply(cxtype: bindgen.clang.CXType): ScalaType = {
      val kind = cxtype.typeKind
      typeMap
        .get(kind)
        .map(apply(_))
        .getOrElse(kind match {
        case CXTypeKind.Pointer =>
          ScalaType("Ptr", Seq(ScalaType(cxtype.pointeeType)))
        case CXTypeKind.ObjCObjectPointer =>
          ScalaType(cxtype.pointeeType)
        case CXTypeKind.ObjCInterface =>
          ScalaType(cxtype.typeSpelling)
        case CXTypeKind.BlockPointer =>
          ptrByte
        case CXTypeKind.Typedef | CXTypeKind.ObjCId =>
          ScalaType(cxtype.typeSpelling)
        case CXTypeKind.Unexposed | CXTypeKind.Elaborated =>
          translateObjectType(cxtype.typeSpelling)
        case CXTypeKind.ObjCClass =>
//          logger.error(cxtype.typeSpelling)
          ScalaType( cxtype.typeSpelling )
        case _ =>
          logger.warn("unsupported type: {} (kind: {})", cxtype.typeSpelling, kind)
          ScalaType(cxtype.typeSpelling)
      })
    }
  }

  private val ObjectTypeWithArg   = """(\w+)<(.+?)>""".r
  private val ObjectTypeWith2Args = """(\w+)<(.+?),(.+?)>""".r
  private val ObjectTypeWith3Args = """(\w+)<(.+?),(.+?),(.+?)>""".r
  private val ObjectPtrType       = """(.*?)\s*?\*""".r
  private val ObjectType          = """(\w+)""".r
  private val FctPtrArg           = """.+?\(.+?\)\((.+?)\)""".r
  private val FctPtrVoid          = """.+?\(.+?\)""".r
  private val Struct              = """struct (.+?)""".r
  protected[objc] def translateObjectType(tpe: String): ScalaType = tpe match {
    case Struct(name) =>
      ScalaType(name)
    case ObjectTypeWith3Args(name,arg1,arg2,arg3) =>
      ScalaType(name,Seq(arg1,arg2,arg3).map( p => translateObjectType(p.trim)) )
    case ObjectTypeWith2Args(name,arg1,arg2) =>
      ScalaType(name,Seq(arg1,arg2).map( p => translateObjectType(p.trim)) )
    case ObjectTypeWithArg(name,arg) =>
      ScalaType(name,translateObjectType(arg.trim()))
    case ObjectType(name) =>
      ScalaType(name)
    case ObjectPtrType(name) =>
      ScalaType(name)
    case FctPtrArg(_) | FctPtrVoid() =>
      logger.warn("function poointers not yet supported")
      ScalaType.ptrByte
    case x =>
      Error(s"encountered unsupported type spec: $x")
  }


  private def methodName(cursorSpelling: String): String = cursorSpelling.split(":").head

  def mergeInterfaces(interfaces: Iterable[AST.Interface]): Iterable[AST.Interface] = interfaces
    .groupBy(_.name)
    .map(p => p._2.reduce{ (l,r)=>
      //      logger.trace("merging {} with {}",l,r)
      l.merge(r)
    })

  private def mapName(name: String): String = name match {
    case "new"      => "`new`"
    case "object"   => "`object`"
    case "class"    => "`class`"
    case "finalize" => "`finalize`"
    case "Class"    => "ObjCClass"
    case x => x
  }


}
