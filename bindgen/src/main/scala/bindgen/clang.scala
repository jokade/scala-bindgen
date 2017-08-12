package bindgen

object clang {
  import scalanative.native._

  type Data              = Ptr[Byte]
  type CXString          = Ptr[Byte]
  type CXIndex           = Ptr[Byte]
  type CXCursor          = Ptr[Byte]
  type CXType            = Ptr[CStruct1[CXTypeKind]]
  type CXTranslationUnit = Ptr[Byte]
  type CXUnsavedFile     = Ptr[Byte]
  type CXDiagnostic      = Ptr[Byte]
  type Visitor           = CFunctionPtr3[CXCursor, CXCursor, Data, UInt]
  type CXSourceLocation  = Ptr[CStruct2[Ptr[Byte],UInt]]

  type CXCursorKind            = UInt
  type CXTranslationUnit_Flags = UInt
  type CXTypeKind              = UInt
  type CXChildVisitResult      = UInt
  object CXChildVisitResult {
    val Break: UInt    = 0.toUInt
    val Continue: UInt = 1.toUInt
    val Recurse: UInt  = 2.toUInt
  }

  type CXErrorCode             = UInt
  object CXErrorCode {
    val Success: UInt          = 0.toUInt
    val Failure: UInt          = 1.toUInt
    val Crashed: UInt          = 2.toUInt
    val InvalidArguments: UInt = 3.toUInt
    val ASTReadError: UInt     = 4.toUInt
  }

  type CXDiagnosticSeverity    = UInt
  object CXDiagnosticSeverity {
    val Ignored: UInt = 0.toUInt
    val Note: UInt    = 1.toUInt
    val Warning: UInt = 2.toUInt
    val Error: UInt   = 3.toUInt
    val Fatal: UInt   = 4.toUInt
  }

  object CXTypeKind {
    val Invalid = 0.toUInt
    val Unexposed = 1.toUInt
    val Void = 2.toUInt
    val Bool = 3.toUInt
    val Char_U = 4.toUInt
    val UChar = 5.toUInt
    val Char16 = 6.toUInt
    val Char32 = 7.toUInt
    val UShort = 8.toUInt
    val UInt = 9.toUInt
    val ULong = 10.toUInt
    val ULongLong = 11.toUInt
    val UInt128 = 12.toUInt
    val Char_S = 13.toUInt
    val SChar = 14.toUInt
    val WChar = 15.toUInt
    val Short = 16.toUInt
    val Int = 17.toUInt
    val Long = 18.toUInt
    val LongLong = 19.toUInt
    val Int128 = 20.toUInt
    val Float = 21.toUInt
    val Double = 22.toUInt
    val LongDouble = 23.toUInt
    val NullPtr = 24.toUInt
    val Overload = 25.toUInt
    val Dependent = 26.toUInt
    val ObjCId = 27.toUInt
    val ObjCClass = 28.toUInt
    val ObjCSel = 29.toUInt
    val Float128 = 30.toUInt
    val FirstBuiltin = Void.toUInt
    val LastBuiltin  = ObjCSel.toUInt
    val Complex = 100.toUInt
    val Pointer = 101.toUInt
    val BlockPointer = 102.toUInt
    val LValueReference = 103.toUInt
    val RValueReference = 104.toUInt
    val Record = 105.toUInt
    val Enum = 106.toUInt
    val Typedef = 107.toUInt
    val ObjCInterface = 108.toUInt
    val ObjCObjectPointer = 109.toUInt
    val FunctionNoProto = 110.toUInt
    val FunctionProto = 111.toUInt
    val ConstantArray = 112.toUInt
    val Vector = 113.toUInt
    val IncompleteArray = 114.toUInt
    val VariableArray = 115.toUInt
    val DependentSizedArray = 116.toUInt
    val MemberPointer = 117.toUInt
    val Auto = 118.toUInt
    val Elaborated = 119.toUInt
  }

  object CXCursorKind {
    val UnexposedDecl                 = 1.toUInt
    val StructDecl                    = 2.toUInt
    val UnionDecl                     = 3.toUInt
    val ClassDecl                     = 4.toUInt
    val EnumDecl                      = 5.toUInt
    val FieldDecl                     = 6.toUInt
    val EnumConstantDecl              = 7.toUInt
    val FunctionDecl                  = 8.toUInt
    val VarDecl                       = 9.toUInt
    val ParmDecl                      = 10.toUInt
    val ObjCInterfaceDecl             = 11.toUInt
    val ObjCCategoryDecl              = 12.toUInt
    val ObjCProtocolDecl              = 13.toUInt
    val ObjCPropertyDecl              = 14.toUInt
    val ObjCIvarDecl                  = 15.toUInt
    val ObjCInstanceMethodDecl        = 16.toUInt
    val ObjCClassMethodDecl           = 17.toUInt
    val ObjCImplementationDecl        = 18.toUInt
    val ObjCCategoryImplDecl          = 19.toUInt
    val TypedefDecl                   = 20.toUInt
    val CXXMethod                     = 21.toUInt
    val Namespace                     = 22.toUInt
    val LinkageSpec                   = 23.toUInt
    val Constructor                   = 24.toUInt
    val Destructor                    = 25.toUInt
    val ConversionFunction            = 26.toUInt
    val TemplateTypeParameter         = 27.toUInt
    val NonTypeTemplateParameter      = 28.toUInt
    val TemplateTemplateParameter     = 29.toUInt
    val FunctionTemplate              = 30.toUInt
    val ClassTemplate                 = 31.toUInt
    val ClassTemplatePartialSpecialization = 32.toUInt
    val NamespaceAlias                = 33.toUInt
    val UsingDirective                = 34.toUInt
    val UsingDeclaration              = 35.toUInt
    val TypeAliasDecl                 = 36.toUInt
    val ObjCSynthesizeDecl            = 37.toUInt
    val ObjCDynamicDecl               = 38.toUInt
    val CXXAccessSpecifier            = 39.toUInt
    val FirstDecl                     = UnexposedDecl.toUInt
    val LastDecl                      = CXXAccessSpecifier.toUInt
    val FirstRef                      = 40.toUInt
    val ObjCSuperClassRef             = 40.toUInt
    val ObjCProtocolRef               = 41.toUInt
    val ObjCClassRef                  = 42.toUInt
    val TypeRef                       = 43.toUInt
    val CXXBaseSpecifier              = 44.toUInt
    val TemplateRef                   = 45.toUInt
    val NamespaceRef                  = 46.toUInt
    val MemberRef                     = 47.toUInt
    val LabelRef                      = 48.toUInt
    val OverloadedDeclRef             = 49.toUInt
    val VariableRef                   = 50.toUInt
    val LastRef                       = VariableRef.toUInt
    val FirstInvalid                  = 70.toUInt
    val InvalidFile                   = 70.toUInt
    val NoDeclFound                   = 71.toUInt
    val NotImplemented                = 72.toUInt
    val InvalidCode                   = 73.toUInt
    val LastInvalid                   = InvalidCode.toUInt
    val FirstExpr                     = 100.toUInt
    val UnexposedExpr                 = 100.toUInt
    val DeclRefExpr                   = 101.toUInt
    val MemberRefExpr                 = 102.toUInt
    val CallExpr                      = 103.toUInt
    val ObjCMessageExpr               = 104.toUInt
    val BlockExpr                     = 105.toUInt
    val IntegerLiteral                = 106.toUInt
    val FloatingLiteral               = 107.toUInt
    val ImaginaryLiteral              = 108.toUInt
    val StringLiteral                 = 109.toUInt
    val CharacterLiteral              = 110.toUInt
    val ParenExpr                     = 111.toUInt
    val UnaryOperator                 = 112.toUInt
    val ArraySubscriptExpr            = 113.toUInt
    val BinaryOperator                = 114.toUInt
    val CompoundAssignOperator        = 115.toUInt
    val ConditionalOperator           = 116.toUInt
    val CStyleCastExpr                = 117.toUInt
    val CompoundLiteralExpr           = 118.toUInt
    val InitListExpr                  = 119.toUInt
    val AddrLabelExpr                 = 120.toUInt
    val StmtExpr                      = 121.toUInt
    val GenericSelectionExpr          = 122.toUInt
    val GNUNullExpr                   = 123.toUInt
    val CXXStaticCastExpr             = 124.toUInt
    val CXXDynamicCastExpr            = 125.toUInt
    val CXXReinterpretCastExpr        = 126.toUInt
    val CXXConstCastExpr              = 127.toUInt
    val CXXFunctionalCastExpr         = 128.toUInt
    val CXXTypeidExpr                 = 129.toUInt
    val CXXBoolLiteralExpr            = 130.toUInt
    val CXXNullPtrLiteralExpr         = 131.toUInt
    val CXXThisExpr                   = 132.toUInt
    val CXXThrowExpr                  = 133.toUInt
    val CXXNewExpr                    = 134.toUInt
    val CXXDeleteExpr                 = 135.toUInt
    val UnaryExpr                     = 136.toUInt
    val ObjCStringLiteral             = 137.toUInt
    val ObjCEncodeExpr                = 138.toUInt
    val ObjCSelectorExpr              = 139.toUInt
    val ObjCProtocolExpr              = 140.toUInt
    val ObjCBridgedCastExpr           = 141.toUInt
    val PackExpansionExpr             = 142.toUInt
    val SizeOfPackExpr                = 143.toUInt
    val LambdaExpr                    = 144.toUInt
    val ObjCBoolLiteralExpr           = 145.toUInt
    val ObjCSelfExpr                  = 146.toUInt
    val OMPArraySectionExpr           = 147.toUInt
    val ObjCAvailabilityCheckExpr     = 148.toUInt
    val LastExpr                      = ObjCAvailabilityCheckExpr.toUInt
    val FirstStmt                     = 200.toUInt
    val UnexposedStmt                 = 200.toUInt
    val LabelStmt                     = 201.toUInt
    val CompoundStmt                  = 202.toUInt
    val CaseStmt                      = 203.toUInt
    val DefaultStmt                   = 204.toUInt
    val IfStmt                        = 205.toUInt
    val SwitchStmt                    = 206.toUInt
    val WhileStmt                     = 207.toUInt
    val DoStmt                        = 208.toUInt
    val ForStmt                       = 209.toUInt
    val GotoStmt                      = 210.toUInt
    val IndirectGotoStmt              = 211.toUInt
    val ContinueStmt                  = 212.toUInt
    val BreakStmt                     = 213.toUInt
    val ReturnStmt                    = 214.toUInt
    val GCCAsmStmt                    = 215.toUInt
    val AsmStmt                       = GCCAsmStmt.toUInt
    val ObjCAtTryStmt                 = 216.toUInt
    val ObjCAtCatchStmt               = 217.toUInt
    val ObjCAtFinallyStmt             = 218.toUInt
    val ObjCAtThrowStmt               = 219.toUInt
    val ObjCAtSynchronizedStmt        = 220.toUInt
    val ObjCAutoreleasePoolStmt       = 221.toUInt
    val ObjCForCollectionStmt         = 222.toUInt
    val CXXCatchStmt                  = 223.toUInt
    val CXXTryStmt                    = 224.toUInt
    val CXXForRangeStmt               = 225.toUInt
    val SEHTryStmt                    = 226.toUInt
    val SEHExceptStmt                 = 227.toUInt
    val SEHFinallyStmt                = 228.toUInt
    val MSAsmStmt                     = 229.toUInt
    val NullStmt                      = 230.toUInt
    val DeclStmt                      = 231.toUInt
    val OMPParallelDirective          = 232.toUInt
    val OMPSimdDirective              = 233.toUInt
    val OMPForDirective               = 234.toUInt
    val OMPSectionsDirective          = 235.toUInt
    val OMPSectionDirective           = 236.toUInt
    val OMPSingleDirective            = 237.toUInt
    val OMPParallelForDirective       = 238.toUInt
    val OMPParallelSectionsDirective  = 239.toUInt
    val OMPTaskDirective              = 240.toUInt
    val OMPMasterDirective            = 241.toUInt
    val OMPCriticalDirective          = 242.toUInt
    val OMPTaskyieldDirective         = 243.toUInt
    val OMPBarrierDirective           = 244.toUInt
    val OMPTaskwaitDirective          = 245.toUInt
    val OMPFlushDirective             = 246.toUInt
    val SEHLeaveStmt                  = 247.toUInt
    val OMPOrderedDirective           = 248.toUInt
    val OMPAtomicDirective            = 249.toUInt
    val OMPForSimdDirective           = 250.toUInt
    val OMPParallelForSimdDirective   = 251.toUInt
    val OMPTargetDirective            = 252.toUInt
    val OMPTeamsDirective             = 253.toUInt
    val OMPTaskgroupDirective         = 254.toUInt
    val OMPCancellationPointDirective = 255.toUInt
    val OMPCancelDirective            = 256.toUInt
    val OMPTargetDataDirective        = 257.toUInt
    val OMPTaskLoopDirective          = 258.toUInt
    val OMPTaskLoopSimdDirective      = 259.toUInt
    val OMPDistributeDirective        = 260.toUInt
    val OMPTargetEnterDataDirective   = 261.toUInt
    val OMPTargetExitDataDirective    = 262.toUInt
    val OMPTargetParallelDirective    = 263.toUInt
    val OMPTargetParallelForDirective = 264.toUInt
    val OMPTargetUpdateDirective      = 265.toUInt
    val OMPDistributeParallelForDirective = 266.toUInt
    val OMPDistributeParallelForSimdDirective = 267.toUInt
    val OMPDistributeSimdDirective = 268.toUInt
    val OMPTargetParallelForSimdDirective = 269.toUInt
    val OMPTargetSimdDirective = 270.toUInt
    val OMPTeamsDistributeDirective = 271.toUInt
    val OMPTeamsDistributeSimdDirective = 272.toUInt
    val OMPTeamsDistributeParallelForSimdDirective = 273.toUInt
    val OMPTeamsDistributeParallelForDirective = 274.toUInt
    val OMPTargetTeamsDirective = 275.toUInt
    val OMPTargetTeamsDistributeDirective = 276.toUInt
    val OMPTargetTeamsDistributeParallelForDirective = 277.toUInt
    val OMPTargetTeamsDistributeParallelForSimdDirective = 278.toUInt
    val OMPTargetTeamsDistributeSimdDirective = 279.toUInt
    val LastStmt = OMPTargetTeamsDistributeSimdDirective.toUInt
    val TranslationUnit               = 300.toUInt
    val FirstAttr                     = 400.toUInt
    val UnexposedAttr                 = 400.toUInt
    val IBActionAttr                  = 401.toUInt
    val IBOutletAttr                  = 402.toUInt
    val IBOutletCollectionAttr        = 403.toUInt
    val CXXFinalAttr                  = 404.toUInt
    val CXXOverrideAttr               = 405.toUInt
    val AnnotateAttr                  = 406.toUInt
    val AsmLabelAttr                  = 407.toUInt
    val PackedAttr                    = 408.toUInt
    val PureAttr                      = 409.toUInt
    val ConstAttr                     = 410.toUInt
    val NoDuplicateAttr               = 411.toUInt
    val CUDAConstantAttr              = 412.toUInt
    val CUDADeviceAttr                = 413.toUInt
    val CUDAGlobalAttr                = 414.toUInt
    val CUDAHostAttr                  = 415.toUInt
    val CUDASharedAttr                = 416.toUInt
    val VisibilityAttr                = 417.toUInt
    val DLLExport                     = 418.toUInt
    val DLLImport                     = 419.toUInt
    val LastAttr                      = DLLImport.toUInt
    val PreprocessingDirective        = 500.toUInt
    val MacroDefinition               = 501.toUInt
    val MacroExpansion                = 502.toUInt
    val MacroInstantiation            = MacroExpansion.toUInt
    val InclusionDirective            = 503.toUInt
    val FirstPreprocessing            = PreprocessingDirective.toUInt
    val LastPreprocessing             = InclusionDirective.toUInt
    val ModuleImportDecl              = 600.toUInt
    val TypeAliasTemplateDecl         = 601.toUInt
    val StaticAssert                  = 602.toUInt
    val FriendDecl                    = 603.toUInt
    val FirstExtraDecl                = ModuleImportDecl.toUInt
    val LastExtraDecl                 = FriendDecl.toUInt
    val OverloadCandidate             = 700.toUInt
  }


  @extern
  @link("clang")
  object api {
    @name("bindgen_clang_CXCursor_StructDecl")
    def CXCursor_StructDecl(): CXCursorKind = extern

    @name("bindgen_clang_CXCursor_UnionDecl")
    def CXCursor_UnionDecl(): CXCursorKind = extern

    @name("bindgen_clang_CXCursor_EnumDecl")
    def CXCursor_EnumDecl(): CXCursorKind = extern

    @name("bindgen_clang_CXCursor_EnumConstantDecl")
    def CXCursor_EnumConstantDecl(): CXCursorKind = extern

    @name("bindgen_clang_CXCursor_FunctionDecl")
    def CXCursor_FunctionDecl(): CXCursorKind = extern;

    @name("bindgen_clang_CXCursor_VarDecl")
    def CXCursor_VarDecl(): CXCursorKind = extern

    @name("bindgen_clang_CXCursor_TypedefDecl")
    def CXCursor_TypedefDecl(): CXCursorKind = extern

    @name("bindgen_clang_CXTranslationUnit_None")
    def CXTranslationUnit_None(): CXTranslationUnit_Flags = extern

    @name("bindgen_clang_CXTranslationUnit_SkipFunctionBodies")
    def CXTranslationUnit_SkipFunctionBodies(): CXTranslationUnit_Flags =
      extern

    @name("bindgen_clang_CXChildVisit_Break")
    def CXChildVisit_Break(): CXChildVisitResult = extern

    @name("bindgen_clang_CXChildVisit_Continue")
    def CXChildVisit_Continue(): CXChildVisitResult = extern

    @name("bindgen_clang_CXChildVisit_Recurse")
    def CXChildVisit_Recurse(): CXChildVisitResult = extern

    @name("bindgen_clang_CXType_Invalid")
    def CXType_Invalid(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Unexposed")
    def CXType_Unexposed(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Void")
    def CXType_Void(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Bool")
    def CXType_Bool(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Char_U")
    def CXType_Char_U(): CXTypeKind = extern

    @name("bindgen_clang_CXType_UChar")
    def CXType_UChar(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Char16")
    def CXType_Char16(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Char32")
    def CXType_Char32(): CXTypeKind = extern

    @name("bindgen_clang_CXType_UShort")
    def CXType_UShort(): CXTypeKind = extern

    @name("bindgen_clang_CXType_UInt")
    def CXType_UInt(): CXTypeKind = extern

    @name("bindgen_clang_CXType_ULong")
    def CXType_ULong(): CXTypeKind = extern

    @name("bindgen_clang_CXType_ULongLong")
    def CXType_ULongLong(): CXTypeKind = extern

    @name("bindgen_clang_CXType_UInt128")
    def CXType_UInt128(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Char_S")
    def CXType_Char_S(): CXTypeKind = extern

    @name("bindgen_clang_CXType_SChar")
    def CXType_SChar(): CXTypeKind = extern

    @name("bindgen_clang_CXType_WChar")
    def CXType_WChar(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Short")
    def CXType_Short(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Int")
    def CXType_Int(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Long")
    def CXType_Long(): CXTypeKind = extern

    @name("bindgen_clang_CXType_LongLong")
    def CXType_LongLong(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Int128")
    def CXType_Int128(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Float")
    def CXType_Float(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Double")
    def CXType_Double(): CXTypeKind = extern

    @name("bindgen_clang_CXType_LongDouble")
    def CXType_LongDouble(): CXTypeKind = extern

    @name("bindgen_clang_CXType_NullPtr")
    def CXType_NullPtr(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Overload")
    def CXType_Overload(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Dependent")
    def CXType_Dependent(): CXTypeKind = extern

    @name("bindgen_clang_CXType_ObjCId")
    def CXType_ObjCId(): CXTypeKind = extern

    @name("bindgen_clang_CXType_ObjCClass")
    def CXType_ObjCClass(): CXTypeKind = extern

    @name("bindgen_clang_CXType_ObjCSel")
    def CXType_ObjCSel(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Complex")
    def CXType_Complex(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Pointer")
    def CXType_Pointer(): CXTypeKind = extern

    @name("bindgen_clang_CXType_BlockPointer")
    def CXType_BlockPointer(): CXTypeKind = extern

    @name("bindgen_clang_CXType_LValueReference")
    def CXType_LValueReference(): CXTypeKind = extern

    @name("bindgen_clang_CXType_RValueReference")
    def CXType_RValueReference(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Record")
    def CXType_Record(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Enum")
    def CXType_Enum(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Typedef")
    def CXType_Typedef(): CXTypeKind = extern

    @name("bindgen_clang_CXType_ObjCInterface")
    def CXType_ObjCInterface(): CXTypeKind = extern

    @name("bindgen_clang_CXType_ObjCObjectPointer")
    def CXType_ObjCObjectPointer(): CXTypeKind = extern

    @name("bindgen_clang_CXType_FunctionNoProto")
    def CXType_FunctionNoProto(): CXTypeKind = extern

    @name("bindgen_clang_CXType_FunctionProto")
    def CXType_FunctionProto(): CXTypeKind = extern

    @name("bindgen_clang_CXType_ConstantArray")
    def CXType_ConstantArray(): CXTypeKind = extern

    @name("bindgen_clang_CXType_Vector")
    def CXType_Vector(): CXTypeKind = extern

    @name("bindgen_clang_CXType_IncompleteArray")
    def CXType_IncompleteArray(): CXTypeKind = extern

    @name("bindgen_clang_CXType_VariableArray")
    def CXType_VariableArray(): CXTypeKind = extern

    @name("bindgen_clang_CXType_DependentSizedArray")
    def CXType_DependentSizedArray(): CXTypeKind = extern

    @name("bindgen_clang_CXType_MemberPointer")
    def CXType_MemberPointer(): CXTypeKind = extern;

    @name("bindgen_clang_getCursorKind")
    def getCursorKind(cursor: CXCursor): CXCursorKind = extern

    @name("bindgen_clang_Cursor_getNumArguments")
    def Cursor_getNumArguments(cursor: CXCursor): CInt = extern

    @name("bindgen_clang_getEnumConstantDeclValue")
    def getEnumConstantDeclValue(cursor: CXCursor): CLongLong = extern

    @name("bindgen_clang_getCursorType")
    def getCursorType(cursor: CXCursor): CXType = extern

    @name("clang_getResultType")
    def getResultType(tpe: CXType): CXType = extern

    @name("bindgen_clang_getEnumDeclIntegerType")
    def getEnumDeclIntegerType(cursor: CXCursor): CXType = extern

    @name("bindgen_clang_getTypedefDeclUnderlyingType")
    def getTypedefDeclUnderlyingType(cursor: CXCursor): CXType = extern

    @name("bindgen_clang_getTranslationUnitCursor")
    def getTranslationUnitCursor(unit: CXTranslationUnit): CXCursor = extern

    @name("bindgen_clang_getCursorKindSpelling")
    def getCursorKindSpelling(kind: CXCursorKind): CString = extern

    @name("bindgen_clang_getCursorSpelling")
    def getCursorSpelling(cursor: CXCursor): CString = extern

    @name("bindgen_clang_getTypeSpelling")
    def getTypeSpelling(tpe: CXType): CString = extern;

    @name("bindgen_clang_Cursor_getArgument")
    def Cursor_getArgument(cursor: CXCursor, i: CInt): CXCursor = extern

    @name("bindgen_clang_visitChildren")
    def visitChildren(parent: CXCursor, visitor: Visitor, data: Data): UInt = extern

    @name("clang_createIndex")
    def createIndex(excludeDeclarationsFromPCH: CInt,
                    displayDiagnostics: CInt): CXIndex = extern

    @name("clang_disposeIndex")
    def disposeIndex(index: CXIndex): Unit = extern

    @name("clang_parseTranslationUnit")
    def parseTranslationUnit(index: CXIndex,
                             fileName: CString,
                             argv: Ptr[CString],
                             argc: CInt,
                             unsavedFiles: CXUnsavedFile,
                             numUnsavedFiles: CInt,
                             options: UInt): CXTranslationUnit = extern

    @name("clang_parseTranslationUnit2")
    def parseTranslationUnit2(index: CXIndex,
                              fileName: CString,
                              argv: Ptr[CString],
                              argc: CInt,
                              unsavedFiles: CXUnsavedFile,
                              numUnsavedFiles: CInt,
                              options: UInt,
                              tunit: Ptr[CXTranslationUnit]): CXErrorCode = extern

    @name("clang_disposeTranslationUnit")
    def disposeTranslationUnit(unit: CXTranslationUnit): Unit = extern

    @name("clang_getTranslationUnitSpelling")
    def getTranslationUnitSpelling(tu: CXTranslationUnit): CXString = extern

    @name("clang_getNumDiagnostics")
    def getNumDiagnostics(unit: CXTranslationUnit): UInt = extern

    @name("clang_getDiagnostic")
    def getDiagnostic(unit: CXTranslationUnit, index: CInt): CXDiagnostic = extern

    @name("clang_disposeDiagnostic")
    def disposeDiagnostic(diag: CXDiagnostic): Unit = extern

    @name("clang_formatDiagnostic")
    def formatDiagnostic(diag: CXDiagnostic): CXString = extern

    @name("clang_getDiagnosticSeverity")
    def getDiagnosticSeverity(diag: CXDiagnostic): CXDiagnosticSeverity = extern

    @name("clang_getCString")
    def getCString(s: CXString): CString = extern

    @name("clang_disposeString")
    def disposeString(s: CXString): Unit = extern

    @name("clang_getDeclObjCTypeEncoding")
    def getDeclObjCTypeEncoding(c: CXCursor): CXString = extern

    @name("clang_Type_getObjCEncoding")
    def getObjCEncoding(c: CXType): CXString = extern

    @name("bindgen_clang_getCursorResultType")
    def getCursorResultType(c: CXCursor): CXType = extern

    @name("bindgen_getTypeKind")
    def getTypeKind(c: CXType): CXTypeKind = extern

    @name("bindgen_clang_getPointeeType")
    def getPointeeType(c: CXType): CXType = extern

//    @name("bindgen_clang_getTypeKindSpelling")
//    def getTypeKindSpelling(tk: CXTypeKind): CString = extern
    @name("bindgen_clang_getTypeDeclaration")
    def getTypeDeclaration(t: CXType): CXCursor = extern

//    /**
//     * Returns a CString representing the location of the current cursor.
//     * The returned CString should be freed when it is no longer required!
//     * @return
//     */
//    @name("bindgen_getLocationSpelling")
//    def getLocationSpelling(c: CXCursor): CString = extern
  }
}
