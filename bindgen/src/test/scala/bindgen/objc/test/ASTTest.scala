//     Project: scala-bindgen
//      Module:
// Description:
package bindgen.objc.test

import utest._

object ASTTest extends TestSuite {
  import bindgen.objc.AST._

  val tests = TestSuite {
    'translateObjectType-{
      assert( translateObjectType("ObjectType") == ScalaType("ObjectType") )
      assert( translateObjectType("ObjectType< Arg >") ==
        ScalaType("ObjectType",ScalaType("Arg")))
    }
  }
}
