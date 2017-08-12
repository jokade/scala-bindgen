package bindgen

import bindgen.Args.Target
import slogging._


case class Args(files: Seq[String]     = Seq(),
                lang: Target       = Target.C,
                chdir: Option[String]  = None,
                pkg: Option[String]    = Some("scalanative.native.bindings"),
                out: Option[String]    = None,
                pkgOut: Option[String] = None,
                recursive: Boolean     = false,
                verbose: Int           = 0,
                debug: Boolean         = false)
object Args {
  sealed trait Target
  object Target {
    case object C extends Target
    case object ObjC extends Target
  }
}

object CLI {

  val parser = new scopt.OptionParser[Args]("bindgen") {
    head("bindgen", "0.1")

    arg[String]("files...")
      .unbounded()
      .optional()
      .action((x, c) => c.copy(files = c.files :+ x))
      .text("""Header file(s) to be converted to Scala.""")

    opt[String]('t',"target")
      .valueName("c|objc")
      .action( (x, c) => c.copy(lang = x.toLowerCase match {
        case "c" => Target.C
        case "objc" => Target.ObjC
        case default => ???
          Error(s"Invalid target language: $default")
      }))
      .text("Target semantics for the generated bindings (default: c)")

    opt[String]('C', "chdir")
      .optional
      .valueName("DIR")
      .action((x, c) => c.copy(chdir = Option(x)))
      .text("""Change to DIR before performing any operations.""")

    opt[String]('P', "package")
      .valueName("PACKAGE")
      .action((x, c) => c.copy(pkg = Option(x)))
      .text("""Package name.""")

    opt[String]('o', "out")
      .optional
      .valueName("FILE")
      .action((x, c) => c.copy(out = Option(x)))
      .text("""Unified output file.""")

    opt[String]('p', "package-out")
      .optional
      .valueName("FILE")
      .action((x,c) => c.copy(pkgOut = Option(x)))
      .text(s"Output file for the package object (required by target objc). Defaults to $${out%/*}/package.scala")

    opt[Unit]('r', "recursive")
      .action((_, c) => c.copy(recursive = true))
      .text("""Produces bindigs for #include(s) recursively.""")

    opt[Unit]('d', "debug")
      .action((_, c) => c.copy(debug = true))
      .text("""Emit Clang AST to stderr.""")

    opt[Unit]('v', "verbose")
      .minOccurs(0).maxOccurs(10)
      .action((_, c) => c.copy(verbose = c.verbose + 1))
      .text("""Increase verbosity.""")

    help("help")
      .text("""prints this usage text""")
  }
}

object Main extends LazyLogging {

  TerminalLoggerFactory.debugCode = TerminalLoggerFactory.TerminalControlCode.cyan
  TerminalLoggerFactory.infoCode = TerminalLoggerFactory.TerminalControlCode.green
  LoggerConfig.factory = TerminalLoggerFactory
  LoggerConfig.level = LogLevel.WARN
  TerminalLoggerFactory.formatter = new MessageFormatter.DefaultPrefixFormatter(true,false,false)

  def main(args: Array[String]): Unit = {
    val (left, right) = args.span(item => "--" != item)
    val cargs = if(right.isEmpty) Array[String]() else right.tail.toArray
    val errno: Int =
      CLI.parser.parse(left, Args()) match {
        case Some(args) =>
          if(args.verbose >= 3)
            LoggerConfig.level = LogLevel.TRACE
          else if(args.verbose == 2)
            LoggerConfig.level = LogLevel.DEBUG
          else if(args.verbose == 1)
            LoggerConfig.level = LogLevel.INFO

          args.lang match {
            case Target.C =>
              (new c.Generator(args, cargs)).process
            case Target.ObjC =>
              val modules = objc.Parser.parse(args,cargs)
              if(args.out.isDefined)
                objc.Generator.generateModules(args.pkg,modules,args.out.get)
              else
                println( objc.Generator.generateModulesAsString(args.pkg,modules) )
              0
          }
        case None    => -1 // arguments are bad, error message will have been displayed
      }

    System.exit(errno)
  }

}

object Error {
  def apply(msg: String): Nothing = {
    System.err.println(s"\33[31mERROR: $msg\33[39m")
    System.exit(1)
    ???
  }
}
