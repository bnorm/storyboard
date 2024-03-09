// Generated from /Users/brian/code/bnorm/presenter/antlr/antlr/KotlinParser.g4 by ANTLR 4.13.1
package dev.bnorm.librettist.text.antlr.kotlin

import org.antlr.v4.kotlinruntime.tree.ParseTreeVisitor

/**
 * This interface defines a complete generic visitor for a parse tree produced by [KotlinParser].
 *
 * @param T The return type of the visit operation.
 *   Use [Unit] for operations with no return type
 */
public interface KotlinParserVisitor<T> : ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by [KotlinParser.kotlinFile].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitKotlinFile(ctx: KotlinParser.KotlinFileContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.script].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitScript(ctx: KotlinParser.ScriptContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.shebangLine].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitShebangLine(ctx: KotlinParser.ShebangLineContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.fileAnnotation].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFileAnnotation(ctx: KotlinParser.FileAnnotationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.packageHeader].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPackageHeader(ctx: KotlinParser.PackageHeaderContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.importList].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitImportList(ctx: KotlinParser.ImportListContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.importHeader].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitImportHeader(ctx: KotlinParser.ImportHeaderContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.importAlias].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitImportAlias(ctx: KotlinParser.ImportAliasContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.topLevelObject].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTopLevelObject(ctx: KotlinParser.TopLevelObjectContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeAlias].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeAlias(ctx: KotlinParser.TypeAliasContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.declaration].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitDeclaration(ctx: KotlinParser.DeclarationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.classDeclaration].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitClassDeclaration(ctx: KotlinParser.ClassDeclarationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.primaryConstructor].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPrimaryConstructor(ctx: KotlinParser.PrimaryConstructorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.classBody].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitClassBody(ctx: KotlinParser.ClassBodyContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.classParameters].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitClassParameters(ctx: KotlinParser.ClassParametersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.classParameter].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitClassParameter(ctx: KotlinParser.ClassParameterContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.delegationSpecifiers].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitDelegationSpecifiers(ctx: KotlinParser.DelegationSpecifiersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.delegationSpecifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitDelegationSpecifier(ctx: KotlinParser.DelegationSpecifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.constructorInvocation].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitConstructorInvocation(ctx: KotlinParser.ConstructorInvocationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.annotatedDelegationSpecifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAnnotatedDelegationSpecifier(ctx: KotlinParser.AnnotatedDelegationSpecifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.explicitDelegation].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitExplicitDelegation(ctx: KotlinParser.ExplicitDelegationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeParameters].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeParameters(ctx: KotlinParser.TypeParametersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeParameter].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeParameter(ctx: KotlinParser.TypeParameterContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeConstraints].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeConstraints(ctx: KotlinParser.TypeConstraintsContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeConstraint].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeConstraint(ctx: KotlinParser.TypeConstraintContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.classMemberDeclarations].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitClassMemberDeclarations(ctx: KotlinParser.ClassMemberDeclarationsContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.classMemberDeclaration].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitClassMemberDeclaration(ctx: KotlinParser.ClassMemberDeclarationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.anonymousInitializer].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAnonymousInitializer(ctx: KotlinParser.AnonymousInitializerContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.companionObject].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitCompanionObject(ctx: KotlinParser.CompanionObjectContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.functionValueParameters].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFunctionValueParameters(ctx: KotlinParser.FunctionValueParametersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.functionValueParameter].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFunctionValueParameter(ctx: KotlinParser.FunctionValueParameterContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.functionDeclaration].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.functionBody].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFunctionBody(ctx: KotlinParser.FunctionBodyContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.variableDeclaration].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitVariableDeclaration(ctx: KotlinParser.VariableDeclarationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.multiVariableDeclaration].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitMultiVariableDeclaration(ctx: KotlinParser.MultiVariableDeclarationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.propertyDeclaration].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPropertyDeclaration(ctx: KotlinParser.PropertyDeclarationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.propertyDelegate].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPropertyDelegate(ctx: KotlinParser.PropertyDelegateContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.getter].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitGetter(ctx: KotlinParser.GetterContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.setter].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitSetter(ctx: KotlinParser.SetterContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parametersWithOptionalType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParametersWithOptionalType(ctx: KotlinParser.ParametersWithOptionalTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.functionValueParameterWithOptionalType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFunctionValueParameterWithOptionalType(ctx: KotlinParser.FunctionValueParameterWithOptionalTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parameterWithOptionalType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParameterWithOptionalType(ctx: KotlinParser.ParameterWithOptionalTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parameter].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParameter(ctx: KotlinParser.ParameterContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.objectDeclaration].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitObjectDeclaration(ctx: KotlinParser.ObjectDeclarationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.secondaryConstructor].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitSecondaryConstructor(ctx: KotlinParser.SecondaryConstructorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.constructorDelegationCall].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitConstructorDelegationCall(ctx: KotlinParser.ConstructorDelegationCallContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.enumClassBody].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitEnumClassBody(ctx: KotlinParser.EnumClassBodyContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.enumEntries].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitEnumEntries(ctx: KotlinParser.EnumEntriesContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.enumEntry].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitEnumEntry(ctx: KotlinParser.EnumEntryContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.type].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitType(ctx: KotlinParser.TypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeReference].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeReference(ctx: KotlinParser.TypeReferenceContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.nullableType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitNullableType(ctx: KotlinParser.NullableTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.quest].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitQuest(ctx: KotlinParser.QuestContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.userType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitUserType(ctx: KotlinParser.UserTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.simpleUserType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitSimpleUserType(ctx: KotlinParser.SimpleUserTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeProjection].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeProjection(ctx: KotlinParser.TypeProjectionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeProjectionModifiers].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeProjectionModifiers(ctx: KotlinParser.TypeProjectionModifiersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeProjectionModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeProjectionModifier(ctx: KotlinParser.TypeProjectionModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.functionType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFunctionType(ctx: KotlinParser.FunctionTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.functionTypeParameters].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFunctionTypeParameters(ctx: KotlinParser.FunctionTypeParametersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parenthesizedType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParenthesizedType(ctx: KotlinParser.ParenthesizedTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.receiverType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitReceiverType(ctx: KotlinParser.ReceiverTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parenthesizedUserType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParenthesizedUserType(ctx: KotlinParser.ParenthesizedUserTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.definitelyNonNullableType].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitDefinitelyNonNullableType(ctx: KotlinParser.DefinitelyNonNullableTypeContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.statements].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitStatements(ctx: KotlinParser.StatementsContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.statement].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitStatement(ctx: KotlinParser.StatementContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.label].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitLabel(ctx: KotlinParser.LabelContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.controlStructureBody].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitControlStructureBody(ctx: KotlinParser.ControlStructureBodyContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.block].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitBlock(ctx: KotlinParser.BlockContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.loopStatement].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitLoopStatement(ctx: KotlinParser.LoopStatementContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.forStatement].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitForStatement(ctx: KotlinParser.ForStatementContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.whileStatement].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitWhileStatement(ctx: KotlinParser.WhileStatementContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.doWhileStatement].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitDoWhileStatement(ctx: KotlinParser.DoWhileStatementContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.assignment].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAssignment(ctx: KotlinParser.AssignmentContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.semi].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitSemi(ctx: KotlinParser.SemiContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.semis].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitSemis(ctx: KotlinParser.SemisContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.expression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitExpression(ctx: KotlinParser.ExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.disjunction].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitDisjunction(ctx: KotlinParser.DisjunctionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.conjunction].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitConjunction(ctx: KotlinParser.ConjunctionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.equality].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitEquality(ctx: KotlinParser.EqualityContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.comparison].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitComparison(ctx: KotlinParser.ComparisonContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.genericCallLikeComparison].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitGenericCallLikeComparison(ctx: KotlinParser.GenericCallLikeComparisonContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.infixOperation].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitInfixOperation(ctx: KotlinParser.InfixOperationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.elvisExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitElvisExpression(ctx: KotlinParser.ElvisExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.elvis].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitElvis(ctx: KotlinParser.ElvisContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.infixFunctionCall].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitInfixFunctionCall(ctx: KotlinParser.InfixFunctionCallContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.rangeExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitRangeExpression(ctx: KotlinParser.RangeExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.additiveExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAdditiveExpression(ctx: KotlinParser.AdditiveExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.multiplicativeExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitMultiplicativeExpression(ctx: KotlinParser.MultiplicativeExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.asExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAsExpression(ctx: KotlinParser.AsExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.prefixUnaryExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPrefixUnaryExpression(ctx: KotlinParser.PrefixUnaryExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.unaryPrefix].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitUnaryPrefix(ctx: KotlinParser.UnaryPrefixContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.postfixUnaryExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPostfixUnaryExpression(ctx: KotlinParser.PostfixUnaryExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.postfixUnarySuffix].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPostfixUnarySuffix(ctx: KotlinParser.PostfixUnarySuffixContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.directlyAssignableExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitDirectlyAssignableExpression(ctx: KotlinParser.DirectlyAssignableExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parenthesizedDirectlyAssignableExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParenthesizedDirectlyAssignableExpression(ctx: KotlinParser.ParenthesizedDirectlyAssignableExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.assignableExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAssignableExpression(ctx: KotlinParser.AssignableExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parenthesizedAssignableExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParenthesizedAssignableExpression(ctx: KotlinParser.ParenthesizedAssignableExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.assignableSuffix].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAssignableSuffix(ctx: KotlinParser.AssignableSuffixContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.indexingSuffix].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitIndexingSuffix(ctx: KotlinParser.IndexingSuffixContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.navigationSuffix].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitNavigationSuffix(ctx: KotlinParser.NavigationSuffixContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.callSuffix].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitCallSuffix(ctx: KotlinParser.CallSuffixContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.annotatedLambda].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAnnotatedLambda(ctx: KotlinParser.AnnotatedLambdaContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeArguments].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeArguments(ctx: KotlinParser.TypeArgumentsContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.valueArguments].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitValueArguments(ctx: KotlinParser.ValueArgumentsContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.valueArgument].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitValueArgument(ctx: KotlinParser.ValueArgumentContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.primaryExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPrimaryExpression(ctx: KotlinParser.PrimaryExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parenthesizedExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParenthesizedExpression(ctx: KotlinParser.ParenthesizedExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.collectionLiteral].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitCollectionLiteral(ctx: KotlinParser.CollectionLiteralContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.literalConstant].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitLiteralConstant(ctx: KotlinParser.LiteralConstantContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.stringLiteral].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitStringLiteral(ctx: KotlinParser.StringLiteralContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.lineStringLiteral].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitLineStringLiteral(ctx: KotlinParser.LineStringLiteralContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.multiLineStringLiteral].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitMultiLineStringLiteral(ctx: KotlinParser.MultiLineStringLiteralContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.lineStringContent].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitLineStringContent(ctx: KotlinParser.LineStringContentContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.lineStringExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitLineStringExpression(ctx: KotlinParser.LineStringExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.multiLineStringContent].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitMultiLineStringContent(ctx: KotlinParser.MultiLineStringContentContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.multiLineStringExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitMultiLineStringExpression(ctx: KotlinParser.MultiLineStringExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.lambdaLiteral].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitLambdaLiteral(ctx: KotlinParser.LambdaLiteralContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.lambdaParameters].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitLambdaParameters(ctx: KotlinParser.LambdaParametersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.lambdaParameter].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitLambdaParameter(ctx: KotlinParser.LambdaParameterContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.anonymousFunction].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAnonymousFunction(ctx: KotlinParser.AnonymousFunctionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.functionLiteral].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFunctionLiteral(ctx: KotlinParser.FunctionLiteralContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.objectLiteral].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitObjectLiteral(ctx: KotlinParser.ObjectLiteralContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.thisExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitThisExpression(ctx: KotlinParser.ThisExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.superExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitSuperExpression(ctx: KotlinParser.SuperExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.ifExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitIfExpression(ctx: KotlinParser.IfExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.whenSubject].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitWhenSubject(ctx: KotlinParser.WhenSubjectContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.whenExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitWhenExpression(ctx: KotlinParser.WhenExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.whenEntry].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitWhenEntry(ctx: KotlinParser.WhenEntryContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.whenCondition].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitWhenCondition(ctx: KotlinParser.WhenConditionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.rangeTest].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitRangeTest(ctx: KotlinParser.RangeTestContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeTest].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeTest(ctx: KotlinParser.TypeTestContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.tryExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTryExpression(ctx: KotlinParser.TryExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.catchBlock].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitCatchBlock(ctx: KotlinParser.CatchBlockContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.finallyBlock].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFinallyBlock(ctx: KotlinParser.FinallyBlockContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.jumpExpression].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitJumpExpression(ctx: KotlinParser.JumpExpressionContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.callableReference].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitCallableReference(ctx: KotlinParser.CallableReferenceContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.assignmentAndOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAssignmentAndOperator(ctx: KotlinParser.AssignmentAndOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.equalityOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitEqualityOperator(ctx: KotlinParser.EqualityOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.comparisonOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitComparisonOperator(ctx: KotlinParser.ComparisonOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.inOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitInOperator(ctx: KotlinParser.InOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.isOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitIsOperator(ctx: KotlinParser.IsOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.additiveOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAdditiveOperator(ctx: KotlinParser.AdditiveOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.multiplicativeOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitMultiplicativeOperator(ctx: KotlinParser.MultiplicativeOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.asOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAsOperator(ctx: KotlinParser.AsOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.prefixUnaryOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPrefixUnaryOperator(ctx: KotlinParser.PrefixUnaryOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.postfixUnaryOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPostfixUnaryOperator(ctx: KotlinParser.PostfixUnaryOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.excl].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitExcl(ctx: KotlinParser.ExclContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.memberAccessOperator].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitMemberAccessOperator(ctx: KotlinParser.MemberAccessOperatorContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.safeNav].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitSafeNav(ctx: KotlinParser.SafeNavContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.modifiers].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitModifiers(ctx: KotlinParser.ModifiersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parameterModifiers].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParameterModifiers(ctx: KotlinParser.ParameterModifiersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.modifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitModifier(ctx: KotlinParser.ModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeModifiers].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeModifiers(ctx: KotlinParser.TypeModifiersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeModifier(ctx: KotlinParser.TypeModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.classModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitClassModifier(ctx: KotlinParser.ClassModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.memberModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitMemberModifier(ctx: KotlinParser.MemberModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.visibilityModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitVisibilityModifier(ctx: KotlinParser.VisibilityModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.varianceModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitVarianceModifier(ctx: KotlinParser.VarianceModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeParameterModifiers].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeParameterModifiers(ctx: KotlinParser.TypeParameterModifiersContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.typeParameterModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitTypeParameterModifier(ctx: KotlinParser.TypeParameterModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.functionModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitFunctionModifier(ctx: KotlinParser.FunctionModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.propertyModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPropertyModifier(ctx: KotlinParser.PropertyModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.inheritanceModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitInheritanceModifier(ctx: KotlinParser.InheritanceModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.parameterModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitParameterModifier(ctx: KotlinParser.ParameterModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.reificationModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitReificationModifier(ctx: KotlinParser.ReificationModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.platformModifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitPlatformModifier(ctx: KotlinParser.PlatformModifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.annotation].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAnnotation(ctx: KotlinParser.AnnotationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.singleAnnotation].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitSingleAnnotation(ctx: KotlinParser.SingleAnnotationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.multiAnnotation].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitMultiAnnotation(ctx: KotlinParser.MultiAnnotationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.annotationUseSiteTarget].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitAnnotationUseSiteTarget(ctx: KotlinParser.AnnotationUseSiteTargetContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.unescapedAnnotation].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitUnescapedAnnotation(ctx: KotlinParser.UnescapedAnnotationContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.simpleIdentifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitSimpleIdentifier(ctx: KotlinParser.SimpleIdentifierContext): T

    /**
     * Visit a parse tree produced by [KotlinParser.identifier].
     *
     * @param ctx The parse tree
     * @return The visitor result
     */
    public fun visitIdentifier(ctx: KotlinParser.IdentifierContext): T

}
