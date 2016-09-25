package disc99.hogan.ast

import disc99.hogan.EnableHogan
import disc99.hogan.Database
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
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
import spock.lang.Specification

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class HoganTransformation implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {

        for(ClassNode classNode in source.getAST().classes) {
            if (isEnableClass(classNode)) {
                classNode.methods.collect {
                    if (it.code instanceof BlockStatement) {
                        ((BlockStatement) it.code).statements
                                .findAll { ((it instanceof ExpressionStatement )
                                && (it.expression instanceof MethodCallExpression )
                                && (it.expression.objectExpression instanceof VariableExpression )
                                && (((VariableExpression) it.expression.objectExpression).accessedVariable?.type?.name == Database.class.name )
                                && (it.expression.method instanceof ConstantExpression )
                                && (isAstMethod(((ConstantExpression) it.expression.method).value) )
                                && (it.expression.arguments.expressions[0] instanceof ClosureExpression)) }
                                .each { rewrite(it.expression) }
                    }
                }
            }
        }
    }

    private boolean isAstMethod(value) {
        value == "insert" || value == "expect"
    }

    private boolean isEnableClass(ClassNode node) {
         isExtendsSpecification(node) || isAnnotatedHogan(node)
    }

    private boolean isAnnotatedHogan(ClassNode node) {
        node.annotations.any { it.classNode.isDerivedFrom(ClassHelper.makeWithoutCaching(EnableHogan)) }
    }

    private boolean isExtendsSpecification(ClassNode node) {
        node.isDerivedFrom(ClassHelper.makeWithoutCaching(Specification))
    }

    private void rewrite(MethodCallExpression methodCallExpression) {

        BlockStatement blockStatement = (BlockStatement) methodCallExpression.arguments.expressions[0].code

        String label

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
