package io.disc99.hogan.ast

import io.disc99.hogan.Database
import io.disc99.hogan.EnableHogan
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.VariableScope
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import spock.lang.Specification


class TransformClassNode {

    ClassNode node

    TransformClassNode(ClassNode node) {
        this.node = node
    }

    /**
     * Judge enable transformation class.
     *
     * @return
     */
    boolean isEnable() {
        isAnnotatedHogan() || isExtendsSpecification()
    }

    /**
     * Rewrite node statement.
     */
    void transform() {
        node.methods
                .findAll {it.code instanceof BlockStatement}
                .each { it.code.statements.findAll { isRewrite(it) }.each { rewrite(it.expression) }}
    }

    private boolean isRewrite(Statement st) {
        (st instanceof ExpressionStatement
                && st.expression instanceof MethodCallExpression
                && st.expression.objectExpression instanceof VariableExpression
                && st.expression.objectExpression.accessedVariable?.type?.name == Database.class.name
                && st.expression.method instanceof ConstantExpression
                && isTargetMethod(st.expression.method.value)
                && st.expression.arguments.expressions[0] instanceof ClosureExpression)
    }

    private boolean isAnnotatedHogan() {
        node.annotations.any { it.classNode.isDerivedFrom(ClassHelper.makeWithoutCaching(EnableHogan)) }
    }

    private boolean isExtendsSpecification() {
        node.isDerivedFrom(ClassHelper.makeWithoutCaching(Specification))
    }

    private boolean isTargetMethod(value) {
        value == "insert" || value == "assert"
    }

    private void rewrite(MethodCallExpression method) {
        MapExpression newArgs = replaceArgs(method)
        method.arguments.expressions.clear()
        method.arguments.expressions.add(newArgs)
    }

    private MapExpression replaceArgs(MethodCallExpression method) {
        BlockStatement dslCode = (BlockStatement) method.arguments.expressions[0].code
        List<MapEntryExpression> arg = convertLabelMap(dslCode)
                .collect { label, block -> createTableClosureMap(label, block) }
                .toList()
        new MapExpression(arg)
    }

    private LinkedHashMap<String, BlockStatement> convertLabelMap(BlockStatement dslCode) {
        String label
        Map<String, BlockStatement> map = [:]
        dslCode.statements.each {
            ExpressionStatement statement = (ExpressionStatement) it
            if (statement.statementLabels != null) {
                String comment = ""
                if (statement.expression instanceof ConstantExpression) {
                    comment = ((ConstantExpression)statement.expression).value
                }
                label = statement.statementLabels[0] + ":" + comment
                map.put(label, new BlockStatement())
            }
            BlockStatement bs = map.get(label)
            bs.addStatement(statement)
            map.put(label, bs)
        }
        map
    }

    private MapEntryExpression createTableClosureMap(String label, BlockStatement dslBlock) {
        ClosureExpression ce = new ClosureExpression(Parameter.EMPTY_ARRAY, dslBlock)
        ce.setVariableScope(new VariableScope())
        new MapEntryExpression(new ConstantExpression(label), ce)
    }
}
