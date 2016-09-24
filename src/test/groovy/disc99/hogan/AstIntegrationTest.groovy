package disc99.hogan

import disc99.hogan.ast.HoganTransformation
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.tools.ast.TransformTestHelper

class AstIntegrationTest extends GroovyTestCase {

    public void testInvokeUnitTest() {

         File file = new File("./src/test/groovy/disc99/hogan/TestClass.groovy")

        assert file.exists()

        def invoker = new TransformTestHelper(new HoganTransformation(), CompilePhase.CANONICALIZATION)

        def clazz = invoker.parse(file)
        def tester = clazz.newInstance()
        tester.setup()
        tester.go()
    }
}


