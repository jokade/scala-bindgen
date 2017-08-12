package bindgen.c

trait FileUtils {
  import java.io.File
  import java.nio.file.Paths

 // def mkdirs(name: Option[String])
  def mkdirs(name: String): File = mkdirs(Paths.get(name).toFile)
  def mkdirs(file: java.io.File): File = {
    val dir = file.getParentFile
    if(!dir.isDirectory)
      if (!dir.mkdirs)
        throw new java.io.IOException(s"Cannot create directory ${dir.toString}")
    file
  }

  private def resolve(chdir: Option[String], name: Option[String]): String = {
    val dir  =
      chdir match {
        case None     => "."
        case Some("") => "."
        case Some(d)  => d
      }
    val path =
      if(name.isEmpty)
        throw new IllegalArgumentException("Cannot access a null name.")
      else
        Paths.get(name.get)
    if(path.isAbsolute) path.toString else Paths.get(dir, path.toString).toString
  }

  def resolve(chdir: Option[String], name: Option[String], default: Option[String]): String =
    name match {
      case Some("-") => "-"
      case None      => resolve(chdir, default)
      case Some("")  => resolve(chdir, default)
      case _         => resolve(chdir, name)
    }

  def resolve(chdir: Option[String], name: Option[String], default: Option[String], from: String, to: String): String = {
    def replaceThenResolve: String = {
        val name = default.getOrElse(throw new IllegalArgumentException("Cannot enforce extension on a null default name."))
        resolve(chdir, Option(name.replace(from, to)))
    }
    name match {
      case Some("-") => "-"
      case None      => replaceThenResolve
      case Some("")  => replaceThenResolve
      case _         => resolve(chdir, name)
    }
  }

  // Java-like API

  def resolve(chdir: String, name: String, default: String): String =
    resolve(Option(chdir), Option(name), Option(default))

  def resolve(chdir: String, name: String, default: String, from: String, to: String): String =
    resolve(Option(chdir), Option(name), Option(default), from, to)
}

