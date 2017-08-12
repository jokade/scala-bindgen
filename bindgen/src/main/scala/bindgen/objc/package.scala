package bindgen

import bindgen.clang._
import slogging.LazyLogging

import scala.scalanative.native._

package object objc extends LazyLogging {

  implicit final class RichCXString(val s: CXString) extends AnyVal {
    def string: String = {
      val cstr = api.getCString(s)
      fromCString(cstr)
    }
  }

  implicit final class RichCXTranslationUnit(val tu: CXTranslationUnit) extends AnyVal {
    @inline def translationUnitSpelling: CXString = api.getTranslationUnitSpelling(tu)
  }

  implicit final class RichCXCursor(val c: CXCursor) extends AnyVal {
    @inline def kind: CXCursorKind = api.getCursorKind(c)
    @inline def cursorSpelling: String = fromCString( api.getCursorSpelling(c) )
    @inline def cursorKindSpelling: String = fromCString( api.getCursorKindSpelling(c.kind) )
    @inline def visitChildren(data: Data)(f: Visitor): UInt = api.visitChildren(c,f, data)
    @inline def cxtype: CXType = api.getCursorType(c)
    @inline def underlyingType: CXType = api.getTypedefDeclUnderlyingType(c)
    @inline def cursorResultType: CXType = api.getCursorResultType(c)
    @inline def declObjCTypeEncoding: String = api.getDeclObjCTypeEncoding(c).string
  }

  implicit final class RichCXType(val t: CXType) extends AnyVal {
    @inline def typeSpelling: String = api.getTypeSpelling(t).string
    @inline def resultType: CXType = api.getResultType(t)
    @inline def objcEncoding: String = api.getObjCEncoding(t).string
    @inline def typeKind: CXTypeKind = api.getTypeKind(t)
    @inline def pointeeType: CXType = api.getPointeeType(t)
    @inline def typeDeclaration: CXCursor = api.getTypeDeclaration(t)
  }


  val typeMap = Map(
    //  CXTypeKind.Invalid = 0.toUInt
    //  CXTypeKind.Unexposed = 1.toUInt
    CXTypeKind.Void        -> "Unit",
    CXTypeKind.Bool        -> "CBool",
    CXTypeKind.Char_U      -> "CUnsignedChar",
    CXTypeKind.UChar       -> "CUnsignedChar",
    CXTypeKind.Char16      -> "CChar16",
    CXTypeKind.Char32      -> "CChar32",
    CXTypeKind.UShort      -> "CUnsignedShort",
    CXTypeKind.UInt        -> "CUnsignedInt",
    CXTypeKind.ULong       -> "CUnsignedLong",
    CXTypeKind.ULongLong   -> "CUnsignedLongLong",
    //  CXTypeKind.UInt128     ->
    CXTypeKind.Char_S      -> "CSignedChar",
    CXTypeKind.SChar       -> "CSignedChar",
    CXTypeKind.WChar       -> "CWideChar",
    CXTypeKind.Short       -> "CShort",
    CXTypeKind.Int         -> "CInt",
    CXTypeKind.Long        -> "CLong",
    CXTypeKind.LongLong    -> "CLongLong",
    //  CXTypeKind.Int128 = 20.toUInt
    CXTypeKind.Float       -> "Float",
    CXTypeKind.Double      -> "Double"
    //  CXTypeKind.LongDouble  -> "Fl= 23.toUInt
    //  CXTypeKind.NullPtr = 24.toUInt
    //  CXTypeKind.Overload = 25.toUInt
    //  CXTypeKind.Dependent = 26.toUInt
    //  CXTypeKind.ObjCId = 27.toUInt
    //  CXTypeKind.ObjCClass = 28.toUInt
    //  CXTypeKind.ObjCSel = 29.toUInt
    //  CXTypeKind.Float128 = 30.toUInt
    //  CXTypeKind.FirstBuiltin = Void.toUInt
    //  CXTypeKind.LastBuiltin  = ObjCSel.toUInt
    //  CXTypeKind.Complex = 100.toUInt
    //  CXTypeKind.Pointer = 101.toUInt
    //  CXTypeKind.BlockPointer = 102.toUInt
    //  CXTypeKind.LValueReference = 103.toUInt
    //  CXTypeKind.RValueReference = 104.toUInt
    //  CXTypeKind.Record = 105.toUInt
    //  CXTypeKind.Enum = 106.toUInt
    //  CXTypeKind.Typedef = 107.toUInt
    //  CXTypeKind.ObjCInterface = 108.toUInt
    //  CXTypeKind.ObjCObjectPointer = 109.toUInt
    //  CXTypeKind.FunctionNoProto = 110.toUInt
    //  CXTypeKind.FunctionProto = 111.toUInt
    //  CXTypeKind.ConstantArray = 112.toUInt
    //  CXTypeKind.Vector = 113.toUInt
    //  CXTypeKind.IncompleteArray = 114.toUInt
    //  CXTypeKind.VariableArray = 115.toUInt
    //  CXTypeKind.DependentSizedArray = 116.toUInt
    //  CXTypeKind.MemberPointer = 117.toUInt
    //  CXTypeKind.Auto = 118.toUInt
    //  CXTypeKind.Elaborated = 119
  )

}

