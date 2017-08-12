package bindgen.c

import bindgen.Args

import scala.scalanative.native._


class Generator(args: Args, cargs: Array[String]) extends FileUtils {
  import java.io.{FileOutputStream, PrintStream}

  import bindgen.clang._
  import bindgen.clang.api._

  private val tree    = new Object with Tree
  private val visitor = AST.visitor
  private val cerr    = new PrintStream(System.err)

  def process: Int = Zone { implicit z =>
    val cargc: CInt = cargs.length
    val cargv: Ptr[CString] = stackalloc[CString](cargs.length)
    var i = 0; while(i<cargc) { cargv(i) = toCString(cargs(i)); i = i + 1 }

    val index: CXIndex = createIndex(0, 1)
    val xs =
      args.files.map { name =>
        if (args.verbose > 1) cerr.println(s"[${name}]")
        val tu: CXTranslationUnit =
          parseTranslationUnit(index,
                               toCString(name),
                               cargv,
                               cargc,
                               null,
                               0,
                               CXTranslationUnit_SkipFunctionBodies)
        if(tu == null) throw new RuntimeException("CXTranslationUnit is null")
        val root: CXCursor = getTranslationUnitCursor(tu)
        if(root == null) throw new RuntimeException("CXCursor is null")
        val result = visitChildren(root, visitor, tree.cast[Data]).toInt
        if(args.verbose > 0) println(s"${result} ${name}")
        if(result==0) makeOutput(tree, resolve(args.chdir, args.out, Option(name), ".h", ".scala"))
        disposeTranslationUnit(tu)
        if (args.debug || args.verbose > 0) cerr.println("----------------------------------------------")
        result
      }
    if (index != null) disposeIndex(index)
    xs.sum
  }

  private def makeOutput(tree: Tree, out: String): Unit = {
    val cout = if("-" == out) new PrintStream(System.out) else new PrintStream(new FileOutputStream(mkdirs(out)))

//    logger.debug("typedefs.size  = {}",tree.typedefs.size)
//    logger.debug("enums.size     = {}",tree.enums.size)
//    logger.debug("functions.size = {}",tree.functions.size)

    tree.typedefs.foreach { entry =>
      cout.println(s"type ${entry.name} = ${entry.underlying}")
    }

    tree.enums.foreach { entry =>
      cout.println(s"object ${entry.name}_Enum {")
      cout.println(
        entry.values
          .map(enum => s"  val ${enum.name} = ${enum.value}")
          .mkString("\n"))
      cout.println("}")
    }

    tree.functions.foreach { entry =>
      cout.println(s"def ${entry.name}(")
      cout.println(
        entry.args
          .map(param => s"  ${param.name}: ${param.tpe}")
          .mkString("\n"))
      cout.println(s"  ): ${entry.returnType} = extern")
    }

    cout.flush()
    if("-" != out) cout.close
  }
}


