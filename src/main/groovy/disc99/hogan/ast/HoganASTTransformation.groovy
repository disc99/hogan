package disc99.hogan.ast

import disc99.hogan.setup.TableSetup
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.VariableScope
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class HoganASTTransformation implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        ClassNode classNode = nodes[1]

        classNode.methods.collect {
            if (it.code instanceof BlockStatement) {
                ((BlockStatement) it.code).statements
                        .findAll{ it instanceof ExpressionStatement }
                        .findAll{ it.expression instanceof MethodCallExpression }
                        .findAll{ it.expression.objectExpression instanceof VariableExpression }
                        .findAll{ ((VariableExpression) it.expression.objectExpression).accessedVariable?.type?.name == TableSetup.class.name }
                        .findAll{ it.expression.method instanceof ConstantExpression }
                        .findAll{ ((ConstantExpression) it.expression.method).value == "insert" }
                        .each { rewrite(it.expression) }
            }
        }
    }

    private void rewrite(MethodCallExpression methodCallExpression) {

        BlockStatement blockStatement = (BlockStatement) methodCallExpression.arguments.expressions[0].code

        String label

        // TODO make wrapper class for this logic
        Map<String, BlockStatement> map = [:]
        blockStatement.statements.each {
            ExpressionStatement statement = (ExpressionStatement) it
            if (statement.statementLabels != null) {
                label = statement.statementLabels[0]
                map.put(label, new BlockStatement())
            }
            BlockStatement bs = map.get(label)
            bs.addStatement(statement)
            map.put(label, bs)
        }


        ArgumentListExpression argumentListExpression = (ArgumentListExpression) methodCallExpression.arguments
        argumentListExpression.expressions.clear()

        List<MapEntryExpression> expressions = map.collect { l, block ->
            ClosureExpression ce = new ClosureExpression(Parameter.EMPTY_ARRAY, block)
            ce.setVariableScope(new VariableScope())
            new MapEntryExpression(new ConstantExpression(l), ce)
        }.toList()

        MapExpression exp = new MapExpression(expressions)

        argumentListExpression.expressions.add(exp)
    }
}
