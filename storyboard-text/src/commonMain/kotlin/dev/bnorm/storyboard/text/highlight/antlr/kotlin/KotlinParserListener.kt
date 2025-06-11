// Generated from /Users/brian/code/bnorm/deck.bnorm.dev/storyboard/storyboard-text/antlr/kotlin/KotlinParser.g4 by ANTLR 4.13.1
package dev.bnorm.storyboard.text.highlight.antlr.kotlin

import org.antlr.v4.kotlinruntime.tree.ParseTreeListener

/**
 * This interface defines a complete listener for a parse tree produced by [KotlinParser].
 */
public interface KotlinParserListener : ParseTreeListener {
    /**
     * Enter a parse tree produced by [KotlinParser.kotlinFile].
     *
     * @param ctx The parse tree
     */
    public fun enterKotlinFile(ctx: KotlinParser.KotlinFileContext)

    /**
     * Exit a parse tree produced by [KotlinParser.kotlinFile].
     *
     * @param ctx The parse tree
     */
    public fun exitKotlinFile(ctx: KotlinParser.KotlinFileContext)

    /**
     * Enter a parse tree produced by [KotlinParser.script].
     *
     * @param ctx The parse tree
     */
    public fun enterScript(ctx: KotlinParser.ScriptContext)

    /**
     * Exit a parse tree produced by [KotlinParser.script].
     *
     * @param ctx The parse tree
     */
    public fun exitScript(ctx: KotlinParser.ScriptContext)

    /**
     * Enter a parse tree produced by [KotlinParser.shebangLine].
     *
     * @param ctx The parse tree
     */
    public fun enterShebangLine(ctx: KotlinParser.ShebangLineContext)

    /**
     * Exit a parse tree produced by [KotlinParser.shebangLine].
     *
     * @param ctx The parse tree
     */
    public fun exitShebangLine(ctx: KotlinParser.ShebangLineContext)

    /**
     * Enter a parse tree produced by [KotlinParser.fileAnnotation].
     *
     * @param ctx The parse tree
     */
    public fun enterFileAnnotation(ctx: KotlinParser.FileAnnotationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.fileAnnotation].
     *
     * @param ctx The parse tree
     */
    public fun exitFileAnnotation(ctx: KotlinParser.FileAnnotationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.packageHeader].
     *
     * @param ctx The parse tree
     */
    public fun enterPackageHeader(ctx: KotlinParser.PackageHeaderContext)

    /**
     * Exit a parse tree produced by [KotlinParser.packageHeader].
     *
     * @param ctx The parse tree
     */
    public fun exitPackageHeader(ctx: KotlinParser.PackageHeaderContext)

    /**
     * Enter a parse tree produced by [KotlinParser.importList].
     *
     * @param ctx The parse tree
     */
    public fun enterImportList(ctx: KotlinParser.ImportListContext)

    /**
     * Exit a parse tree produced by [KotlinParser.importList].
     *
     * @param ctx The parse tree
     */
    public fun exitImportList(ctx: KotlinParser.ImportListContext)

    /**
     * Enter a parse tree produced by [KotlinParser.importHeader].
     *
     * @param ctx The parse tree
     */
    public fun enterImportHeader(ctx: KotlinParser.ImportHeaderContext)

    /**
     * Exit a parse tree produced by [KotlinParser.importHeader].
     *
     * @param ctx The parse tree
     */
    public fun exitImportHeader(ctx: KotlinParser.ImportHeaderContext)

    /**
     * Enter a parse tree produced by [KotlinParser.importAlias].
     *
     * @param ctx The parse tree
     */
    public fun enterImportAlias(ctx: KotlinParser.ImportAliasContext)

    /**
     * Exit a parse tree produced by [KotlinParser.importAlias].
     *
     * @param ctx The parse tree
     */
    public fun exitImportAlias(ctx: KotlinParser.ImportAliasContext)

    /**
     * Enter a parse tree produced by [KotlinParser.topLevelObject].
     *
     * @param ctx The parse tree
     */
    public fun enterTopLevelObject(ctx: KotlinParser.TopLevelObjectContext)

    /**
     * Exit a parse tree produced by [KotlinParser.topLevelObject].
     *
     * @param ctx The parse tree
     */
    public fun exitTopLevelObject(ctx: KotlinParser.TopLevelObjectContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeAlias].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeAlias(ctx: KotlinParser.TypeAliasContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeAlias].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeAlias(ctx: KotlinParser.TypeAliasContext)

    /**
     * Enter a parse tree produced by [KotlinParser.declaration].
     *
     * @param ctx The parse tree
     */
    public fun enterDeclaration(ctx: KotlinParser.DeclarationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.declaration].
     *
     * @param ctx The parse tree
     */
    public fun exitDeclaration(ctx: KotlinParser.DeclarationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.classDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun enterClassDeclaration(ctx: KotlinParser.ClassDeclarationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.classDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun exitClassDeclaration(ctx: KotlinParser.ClassDeclarationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.primaryConstructor].
     *
     * @param ctx The parse tree
     */
    public fun enterPrimaryConstructor(ctx: KotlinParser.PrimaryConstructorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.primaryConstructor].
     *
     * @param ctx The parse tree
     */
    public fun exitPrimaryConstructor(ctx: KotlinParser.PrimaryConstructorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.classBody].
     *
     * @param ctx The parse tree
     */
    public fun enterClassBody(ctx: KotlinParser.ClassBodyContext)

    /**
     * Exit a parse tree produced by [KotlinParser.classBody].
     *
     * @param ctx The parse tree
     */
    public fun exitClassBody(ctx: KotlinParser.ClassBodyContext)

    /**
     * Enter a parse tree produced by [KotlinParser.classParameters].
     *
     * @param ctx The parse tree
     */
    public fun enterClassParameters(ctx: KotlinParser.ClassParametersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.classParameters].
     *
     * @param ctx The parse tree
     */
    public fun exitClassParameters(ctx: KotlinParser.ClassParametersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.classParameter].
     *
     * @param ctx The parse tree
     */
    public fun enterClassParameter(ctx: KotlinParser.ClassParameterContext)

    /**
     * Exit a parse tree produced by [KotlinParser.classParameter].
     *
     * @param ctx The parse tree
     */
    public fun exitClassParameter(ctx: KotlinParser.ClassParameterContext)

    /**
     * Enter a parse tree produced by [KotlinParser.delegationSpecifiers].
     *
     * @param ctx The parse tree
     */
    public fun enterDelegationSpecifiers(ctx: KotlinParser.DelegationSpecifiersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.delegationSpecifiers].
     *
     * @param ctx The parse tree
     */
    public fun exitDelegationSpecifiers(ctx: KotlinParser.DelegationSpecifiersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.delegationSpecifier].
     *
     * @param ctx The parse tree
     */
    public fun enterDelegationSpecifier(ctx: KotlinParser.DelegationSpecifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.delegationSpecifier].
     *
     * @param ctx The parse tree
     */
    public fun exitDelegationSpecifier(ctx: KotlinParser.DelegationSpecifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.constructorInvocation].
     *
     * @param ctx The parse tree
     */
    public fun enterConstructorInvocation(ctx: KotlinParser.ConstructorInvocationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.constructorInvocation].
     *
     * @param ctx The parse tree
     */
    public fun exitConstructorInvocation(ctx: KotlinParser.ConstructorInvocationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.annotatedDelegationSpecifier].
     *
     * @param ctx The parse tree
     */
    public fun enterAnnotatedDelegationSpecifier(ctx: KotlinParser.AnnotatedDelegationSpecifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.annotatedDelegationSpecifier].
     *
     * @param ctx The parse tree
     */
    public fun exitAnnotatedDelegationSpecifier(ctx: KotlinParser.AnnotatedDelegationSpecifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.explicitDelegation].
     *
     * @param ctx The parse tree
     */
    public fun enterExplicitDelegation(ctx: KotlinParser.ExplicitDelegationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.explicitDelegation].
     *
     * @param ctx The parse tree
     */
    public fun exitExplicitDelegation(ctx: KotlinParser.ExplicitDelegationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeParameters].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeParameters(ctx: KotlinParser.TypeParametersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeParameters].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeParameters(ctx: KotlinParser.TypeParametersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeParameter].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeParameter(ctx: KotlinParser.TypeParameterContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeParameter].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeParameter(ctx: KotlinParser.TypeParameterContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeConstraints].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeConstraints(ctx: KotlinParser.TypeConstraintsContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeConstraints].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeConstraints(ctx: KotlinParser.TypeConstraintsContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeConstraint].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeConstraint(ctx: KotlinParser.TypeConstraintContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeConstraint].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeConstraint(ctx: KotlinParser.TypeConstraintContext)

    /**
     * Enter a parse tree produced by [KotlinParser.classMemberDeclarations].
     *
     * @param ctx The parse tree
     */
    public fun enterClassMemberDeclarations(ctx: KotlinParser.ClassMemberDeclarationsContext)

    /**
     * Exit a parse tree produced by [KotlinParser.classMemberDeclarations].
     *
     * @param ctx The parse tree
     */
    public fun exitClassMemberDeclarations(ctx: KotlinParser.ClassMemberDeclarationsContext)

    /**
     * Enter a parse tree produced by [KotlinParser.classMemberDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun enterClassMemberDeclaration(ctx: KotlinParser.ClassMemberDeclarationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.classMemberDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun exitClassMemberDeclaration(ctx: KotlinParser.ClassMemberDeclarationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.anonymousInitializer].
     *
     * @param ctx The parse tree
     */
    public fun enterAnonymousInitializer(ctx: KotlinParser.AnonymousInitializerContext)

    /**
     * Exit a parse tree produced by [KotlinParser.anonymousInitializer].
     *
     * @param ctx The parse tree
     */
    public fun exitAnonymousInitializer(ctx: KotlinParser.AnonymousInitializerContext)

    /**
     * Enter a parse tree produced by [KotlinParser.companionObject].
     *
     * @param ctx The parse tree
     */
    public fun enterCompanionObject(ctx: KotlinParser.CompanionObjectContext)

    /**
     * Exit a parse tree produced by [KotlinParser.companionObject].
     *
     * @param ctx The parse tree
     */
    public fun exitCompanionObject(ctx: KotlinParser.CompanionObjectContext)

    /**
     * Enter a parse tree produced by [KotlinParser.functionValueParameters].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionValueParameters(ctx: KotlinParser.FunctionValueParametersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.functionValueParameters].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionValueParameters(ctx: KotlinParser.FunctionValueParametersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.functionValueParameter].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionValueParameter(ctx: KotlinParser.FunctionValueParameterContext)

    /**
     * Exit a parse tree produced by [KotlinParser.functionValueParameter].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionValueParameter(ctx: KotlinParser.FunctionValueParameterContext)

    /**
     * Enter a parse tree produced by [KotlinParser.functionDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.functionDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.functionBody].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionBody(ctx: KotlinParser.FunctionBodyContext)

    /**
     * Exit a parse tree produced by [KotlinParser.functionBody].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionBody(ctx: KotlinParser.FunctionBodyContext)

    /**
     * Enter a parse tree produced by [KotlinParser.variableDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun enterVariableDeclaration(ctx: KotlinParser.VariableDeclarationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.variableDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun exitVariableDeclaration(ctx: KotlinParser.VariableDeclarationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.multiVariableDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun enterMultiVariableDeclaration(ctx: KotlinParser.MultiVariableDeclarationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.multiVariableDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun exitMultiVariableDeclaration(ctx: KotlinParser.MultiVariableDeclarationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.propertyDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun enterPropertyDeclaration(ctx: KotlinParser.PropertyDeclarationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.propertyDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun exitPropertyDeclaration(ctx: KotlinParser.PropertyDeclarationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.propertyDelegate].
     *
     * @param ctx The parse tree
     */
    public fun enterPropertyDelegate(ctx: KotlinParser.PropertyDelegateContext)

    /**
     * Exit a parse tree produced by [KotlinParser.propertyDelegate].
     *
     * @param ctx The parse tree
     */
    public fun exitPropertyDelegate(ctx: KotlinParser.PropertyDelegateContext)

    /**
     * Enter a parse tree produced by [KotlinParser.getter].
     *
     * @param ctx The parse tree
     */
    public fun enterGetter(ctx: KotlinParser.GetterContext)

    /**
     * Exit a parse tree produced by [KotlinParser.getter].
     *
     * @param ctx The parse tree
     */
    public fun exitGetter(ctx: KotlinParser.GetterContext)

    /**
     * Enter a parse tree produced by [KotlinParser.setter].
     *
     * @param ctx The parse tree
     */
    public fun enterSetter(ctx: KotlinParser.SetterContext)

    /**
     * Exit a parse tree produced by [KotlinParser.setter].
     *
     * @param ctx The parse tree
     */
    public fun exitSetter(ctx: KotlinParser.SetterContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parametersWithOptionalType].
     *
     * @param ctx The parse tree
     */
    public fun enterParametersWithOptionalType(ctx: KotlinParser.ParametersWithOptionalTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parametersWithOptionalType].
     *
     * @param ctx The parse tree
     */
    public fun exitParametersWithOptionalType(ctx: KotlinParser.ParametersWithOptionalTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.functionValueParameterWithOptionalType].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionValueParameterWithOptionalType(ctx: KotlinParser.FunctionValueParameterWithOptionalTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.functionValueParameterWithOptionalType].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionValueParameterWithOptionalType(ctx: KotlinParser.FunctionValueParameterWithOptionalTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parameterWithOptionalType].
     *
     * @param ctx The parse tree
     */
    public fun enterParameterWithOptionalType(ctx: KotlinParser.ParameterWithOptionalTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parameterWithOptionalType].
     *
     * @param ctx The parse tree
     */
    public fun exitParameterWithOptionalType(ctx: KotlinParser.ParameterWithOptionalTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parameter].
     *
     * @param ctx The parse tree
     */
    public fun enterParameter(ctx: KotlinParser.ParameterContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parameter].
     *
     * @param ctx The parse tree
     */
    public fun exitParameter(ctx: KotlinParser.ParameterContext)

    /**
     * Enter a parse tree produced by [KotlinParser.objectDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun enterObjectDeclaration(ctx: KotlinParser.ObjectDeclarationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.objectDeclaration].
     *
     * @param ctx The parse tree
     */
    public fun exitObjectDeclaration(ctx: KotlinParser.ObjectDeclarationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.secondaryConstructor].
     *
     * @param ctx The parse tree
     */
    public fun enterSecondaryConstructor(ctx: KotlinParser.SecondaryConstructorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.secondaryConstructor].
     *
     * @param ctx The parse tree
     */
    public fun exitSecondaryConstructor(ctx: KotlinParser.SecondaryConstructorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.constructorDelegationCall].
     *
     * @param ctx The parse tree
     */
    public fun enterConstructorDelegationCall(ctx: KotlinParser.ConstructorDelegationCallContext)

    /**
     * Exit a parse tree produced by [KotlinParser.constructorDelegationCall].
     *
     * @param ctx The parse tree
     */
    public fun exitConstructorDelegationCall(ctx: KotlinParser.ConstructorDelegationCallContext)

    /**
     * Enter a parse tree produced by [KotlinParser.enumClassBody].
     *
     * @param ctx The parse tree
     */
    public fun enterEnumClassBody(ctx: KotlinParser.EnumClassBodyContext)

    /**
     * Exit a parse tree produced by [KotlinParser.enumClassBody].
     *
     * @param ctx The parse tree
     */
    public fun exitEnumClassBody(ctx: KotlinParser.EnumClassBodyContext)

    /**
     * Enter a parse tree produced by [KotlinParser.enumEntries].
     *
     * @param ctx The parse tree
     */
    public fun enterEnumEntries(ctx: KotlinParser.EnumEntriesContext)

    /**
     * Exit a parse tree produced by [KotlinParser.enumEntries].
     *
     * @param ctx The parse tree
     */
    public fun exitEnumEntries(ctx: KotlinParser.EnumEntriesContext)

    /**
     * Enter a parse tree produced by [KotlinParser.enumEntry].
     *
     * @param ctx The parse tree
     */
    public fun enterEnumEntry(ctx: KotlinParser.EnumEntryContext)

    /**
     * Exit a parse tree produced by [KotlinParser.enumEntry].
     *
     * @param ctx The parse tree
     */
    public fun exitEnumEntry(ctx: KotlinParser.EnumEntryContext)

    /**
     * Enter a parse tree produced by [KotlinParser.type].
     *
     * @param ctx The parse tree
     */
    public fun enterType(ctx: KotlinParser.TypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.type].
     *
     * @param ctx The parse tree
     */
    public fun exitType(ctx: KotlinParser.TypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeReference].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeReference(ctx: KotlinParser.TypeReferenceContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeReference].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeReference(ctx: KotlinParser.TypeReferenceContext)

    /**
     * Enter a parse tree produced by [KotlinParser.nullableType].
     *
     * @param ctx The parse tree
     */
    public fun enterNullableType(ctx: KotlinParser.NullableTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.nullableType].
     *
     * @param ctx The parse tree
     */
    public fun exitNullableType(ctx: KotlinParser.NullableTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.quest].
     *
     * @param ctx The parse tree
     */
    public fun enterQuest(ctx: KotlinParser.QuestContext)

    /**
     * Exit a parse tree produced by [KotlinParser.quest].
     *
     * @param ctx The parse tree
     */
    public fun exitQuest(ctx: KotlinParser.QuestContext)

    /**
     * Enter a parse tree produced by [KotlinParser.userType].
     *
     * @param ctx The parse tree
     */
    public fun enterUserType(ctx: KotlinParser.UserTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.userType].
     *
     * @param ctx The parse tree
     */
    public fun exitUserType(ctx: KotlinParser.UserTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.simpleUserType].
     *
     * @param ctx The parse tree
     */
    public fun enterSimpleUserType(ctx: KotlinParser.SimpleUserTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.simpleUserType].
     *
     * @param ctx The parse tree
     */
    public fun exitSimpleUserType(ctx: KotlinParser.SimpleUserTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeProjection].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeProjection(ctx: KotlinParser.TypeProjectionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeProjection].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeProjection(ctx: KotlinParser.TypeProjectionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeProjectionModifiers].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeProjectionModifiers(ctx: KotlinParser.TypeProjectionModifiersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeProjectionModifiers].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeProjectionModifiers(ctx: KotlinParser.TypeProjectionModifiersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeProjectionModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeProjectionModifier(ctx: KotlinParser.TypeProjectionModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeProjectionModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeProjectionModifier(ctx: KotlinParser.TypeProjectionModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.functionType].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionType(ctx: KotlinParser.FunctionTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.functionType].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionType(ctx: KotlinParser.FunctionTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.functionTypeParameters].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionTypeParameters(ctx: KotlinParser.FunctionTypeParametersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.functionTypeParameters].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionTypeParameters(ctx: KotlinParser.FunctionTypeParametersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parenthesizedType].
     *
     * @param ctx The parse tree
     */
    public fun enterParenthesizedType(ctx: KotlinParser.ParenthesizedTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parenthesizedType].
     *
     * @param ctx The parse tree
     */
    public fun exitParenthesizedType(ctx: KotlinParser.ParenthesizedTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.receiverType].
     *
     * @param ctx The parse tree
     */
    public fun enterReceiverType(ctx: KotlinParser.ReceiverTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.receiverType].
     *
     * @param ctx The parse tree
     */
    public fun exitReceiverType(ctx: KotlinParser.ReceiverTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parenthesizedUserType].
     *
     * @param ctx The parse tree
     */
    public fun enterParenthesizedUserType(ctx: KotlinParser.ParenthesizedUserTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parenthesizedUserType].
     *
     * @param ctx The parse tree
     */
    public fun exitParenthesizedUserType(ctx: KotlinParser.ParenthesizedUserTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.definitelyNonNullableType].
     *
     * @param ctx The parse tree
     */
    public fun enterDefinitelyNonNullableType(ctx: KotlinParser.DefinitelyNonNullableTypeContext)

    /**
     * Exit a parse tree produced by [KotlinParser.definitelyNonNullableType].
     *
     * @param ctx The parse tree
     */
    public fun exitDefinitelyNonNullableType(ctx: KotlinParser.DefinitelyNonNullableTypeContext)

    /**
     * Enter a parse tree produced by [KotlinParser.statements].
     *
     * @param ctx The parse tree
     */
    public fun enterStatements(ctx: KotlinParser.StatementsContext)

    /**
     * Exit a parse tree produced by [KotlinParser.statements].
     *
     * @param ctx The parse tree
     */
    public fun exitStatements(ctx: KotlinParser.StatementsContext)

    /**
     * Enter a parse tree produced by [KotlinParser.statement].
     *
     * @param ctx The parse tree
     */
    public fun enterStatement(ctx: KotlinParser.StatementContext)

    /**
     * Exit a parse tree produced by [KotlinParser.statement].
     *
     * @param ctx The parse tree
     */
    public fun exitStatement(ctx: KotlinParser.StatementContext)

    /**
     * Enter a parse tree produced by [KotlinParser.label].
     *
     * @param ctx The parse tree
     */
    public fun enterLabel(ctx: KotlinParser.LabelContext)

    /**
     * Exit a parse tree produced by [KotlinParser.label].
     *
     * @param ctx The parse tree
     */
    public fun exitLabel(ctx: KotlinParser.LabelContext)

    /**
     * Enter a parse tree produced by [KotlinParser.controlStructureBody].
     *
     * @param ctx The parse tree
     */
    public fun enterControlStructureBody(ctx: KotlinParser.ControlStructureBodyContext)

    /**
     * Exit a parse tree produced by [KotlinParser.controlStructureBody].
     *
     * @param ctx The parse tree
     */
    public fun exitControlStructureBody(ctx: KotlinParser.ControlStructureBodyContext)

    /**
     * Enter a parse tree produced by [KotlinParser.block].
     *
     * @param ctx The parse tree
     */
    public fun enterBlock(ctx: KotlinParser.BlockContext)

    /**
     * Exit a parse tree produced by [KotlinParser.block].
     *
     * @param ctx The parse tree
     */
    public fun exitBlock(ctx: KotlinParser.BlockContext)

    /**
     * Enter a parse tree produced by [KotlinParser.loopStatement].
     *
     * @param ctx The parse tree
     */
    public fun enterLoopStatement(ctx: KotlinParser.LoopStatementContext)

    /**
     * Exit a parse tree produced by [KotlinParser.loopStatement].
     *
     * @param ctx The parse tree
     */
    public fun exitLoopStatement(ctx: KotlinParser.LoopStatementContext)

    /**
     * Enter a parse tree produced by [KotlinParser.forStatement].
     *
     * @param ctx The parse tree
     */
    public fun enterForStatement(ctx: KotlinParser.ForStatementContext)

    /**
     * Exit a parse tree produced by [KotlinParser.forStatement].
     *
     * @param ctx The parse tree
     */
    public fun exitForStatement(ctx: KotlinParser.ForStatementContext)

    /**
     * Enter a parse tree produced by [KotlinParser.whileStatement].
     *
     * @param ctx The parse tree
     */
    public fun enterWhileStatement(ctx: KotlinParser.WhileStatementContext)

    /**
     * Exit a parse tree produced by [KotlinParser.whileStatement].
     *
     * @param ctx The parse tree
     */
    public fun exitWhileStatement(ctx: KotlinParser.WhileStatementContext)

    /**
     * Enter a parse tree produced by [KotlinParser.doWhileStatement].
     *
     * @param ctx The parse tree
     */
    public fun enterDoWhileStatement(ctx: KotlinParser.DoWhileStatementContext)

    /**
     * Exit a parse tree produced by [KotlinParser.doWhileStatement].
     *
     * @param ctx The parse tree
     */
    public fun exitDoWhileStatement(ctx: KotlinParser.DoWhileStatementContext)

    /**
     * Enter a parse tree produced by [KotlinParser.assignment].
     *
     * @param ctx The parse tree
     */
    public fun enterAssignment(ctx: KotlinParser.AssignmentContext)

    /**
     * Exit a parse tree produced by [KotlinParser.assignment].
     *
     * @param ctx The parse tree
     */
    public fun exitAssignment(ctx: KotlinParser.AssignmentContext)

    /**
     * Enter a parse tree produced by [KotlinParser.semi].
     *
     * @param ctx The parse tree
     */
    public fun enterSemi(ctx: KotlinParser.SemiContext)

    /**
     * Exit a parse tree produced by [KotlinParser.semi].
     *
     * @param ctx The parse tree
     */
    public fun exitSemi(ctx: KotlinParser.SemiContext)

    /**
     * Enter a parse tree produced by [KotlinParser.semis].
     *
     * @param ctx The parse tree
     */
    public fun enterSemis(ctx: KotlinParser.SemisContext)

    /**
     * Exit a parse tree produced by [KotlinParser.semis].
     *
     * @param ctx The parse tree
     */
    public fun exitSemis(ctx: KotlinParser.SemisContext)

    /**
     * Enter a parse tree produced by [KotlinParser.expression].
     *
     * @param ctx The parse tree
     */
    public fun enterExpression(ctx: KotlinParser.ExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.expression].
     *
     * @param ctx The parse tree
     */
    public fun exitExpression(ctx: KotlinParser.ExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.disjunction].
     *
     * @param ctx The parse tree
     */
    public fun enterDisjunction(ctx: KotlinParser.DisjunctionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.disjunction].
     *
     * @param ctx The parse tree
     */
    public fun exitDisjunction(ctx: KotlinParser.DisjunctionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.conjunction].
     *
     * @param ctx The parse tree
     */
    public fun enterConjunction(ctx: KotlinParser.ConjunctionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.conjunction].
     *
     * @param ctx The parse tree
     */
    public fun exitConjunction(ctx: KotlinParser.ConjunctionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.equality].
     *
     * @param ctx The parse tree
     */
    public fun enterEquality(ctx: KotlinParser.EqualityContext)

    /**
     * Exit a parse tree produced by [KotlinParser.equality].
     *
     * @param ctx The parse tree
     */
    public fun exitEquality(ctx: KotlinParser.EqualityContext)

    /**
     * Enter a parse tree produced by [KotlinParser.comparison].
     *
     * @param ctx The parse tree
     */
    public fun enterComparison(ctx: KotlinParser.ComparisonContext)

    /**
     * Exit a parse tree produced by [KotlinParser.comparison].
     *
     * @param ctx The parse tree
     */
    public fun exitComparison(ctx: KotlinParser.ComparisonContext)

    /**
     * Enter a parse tree produced by [KotlinParser.genericCallLikeComparison].
     *
     * @param ctx The parse tree
     */
    public fun enterGenericCallLikeComparison(ctx: KotlinParser.GenericCallLikeComparisonContext)

    /**
     * Exit a parse tree produced by [KotlinParser.genericCallLikeComparison].
     *
     * @param ctx The parse tree
     */
    public fun exitGenericCallLikeComparison(ctx: KotlinParser.GenericCallLikeComparisonContext)

    /**
     * Enter a parse tree produced by [KotlinParser.infixOperation].
     *
     * @param ctx The parse tree
     */
    public fun enterInfixOperation(ctx: KotlinParser.InfixOperationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.infixOperation].
     *
     * @param ctx The parse tree
     */
    public fun exitInfixOperation(ctx: KotlinParser.InfixOperationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.elvisExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterElvisExpression(ctx: KotlinParser.ElvisExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.elvisExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitElvisExpression(ctx: KotlinParser.ElvisExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.elvis].
     *
     * @param ctx The parse tree
     */
    public fun enterElvis(ctx: KotlinParser.ElvisContext)

    /**
     * Exit a parse tree produced by [KotlinParser.elvis].
     *
     * @param ctx The parse tree
     */
    public fun exitElvis(ctx: KotlinParser.ElvisContext)

    /**
     * Enter a parse tree produced by [KotlinParser.infixFunctionCall].
     *
     * @param ctx The parse tree
     */
    public fun enterInfixFunctionCall(ctx: KotlinParser.InfixFunctionCallContext)

    /**
     * Exit a parse tree produced by [KotlinParser.infixFunctionCall].
     *
     * @param ctx The parse tree
     */
    public fun exitInfixFunctionCall(ctx: KotlinParser.InfixFunctionCallContext)

    /**
     * Enter a parse tree produced by [KotlinParser.rangeExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterRangeExpression(ctx: KotlinParser.RangeExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.rangeExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitRangeExpression(ctx: KotlinParser.RangeExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.additiveExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterAdditiveExpression(ctx: KotlinParser.AdditiveExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.additiveExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitAdditiveExpression(ctx: KotlinParser.AdditiveExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.multiplicativeExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterMultiplicativeExpression(ctx: KotlinParser.MultiplicativeExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.multiplicativeExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitMultiplicativeExpression(ctx: KotlinParser.MultiplicativeExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.asExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterAsExpression(ctx: KotlinParser.AsExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.asExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitAsExpression(ctx: KotlinParser.AsExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.prefixUnaryExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterPrefixUnaryExpression(ctx: KotlinParser.PrefixUnaryExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.prefixUnaryExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitPrefixUnaryExpression(ctx: KotlinParser.PrefixUnaryExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.unaryPrefix].
     *
     * @param ctx The parse tree
     */
    public fun enterUnaryPrefix(ctx: KotlinParser.UnaryPrefixContext)

    /**
     * Exit a parse tree produced by [KotlinParser.unaryPrefix].
     *
     * @param ctx The parse tree
     */
    public fun exitUnaryPrefix(ctx: KotlinParser.UnaryPrefixContext)

    /**
     * Enter a parse tree produced by [KotlinParser.postfixUnaryExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterPostfixUnaryExpression(ctx: KotlinParser.PostfixUnaryExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.postfixUnaryExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitPostfixUnaryExpression(ctx: KotlinParser.PostfixUnaryExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.postfixUnarySuffix].
     *
     * @param ctx The parse tree
     */
    public fun enterPostfixUnarySuffix(ctx: KotlinParser.PostfixUnarySuffixContext)

    /**
     * Exit a parse tree produced by [KotlinParser.postfixUnarySuffix].
     *
     * @param ctx The parse tree
     */
    public fun exitPostfixUnarySuffix(ctx: KotlinParser.PostfixUnarySuffixContext)

    /**
     * Enter a parse tree produced by [KotlinParser.directlyAssignableExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterDirectlyAssignableExpression(ctx: KotlinParser.DirectlyAssignableExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.directlyAssignableExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitDirectlyAssignableExpression(ctx: KotlinParser.DirectlyAssignableExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parenthesizedDirectlyAssignableExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterParenthesizedDirectlyAssignableExpression(ctx: KotlinParser.ParenthesizedDirectlyAssignableExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parenthesizedDirectlyAssignableExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitParenthesizedDirectlyAssignableExpression(ctx: KotlinParser.ParenthesizedDirectlyAssignableExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.assignableExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterAssignableExpression(ctx: KotlinParser.AssignableExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.assignableExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitAssignableExpression(ctx: KotlinParser.AssignableExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parenthesizedAssignableExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterParenthesizedAssignableExpression(ctx: KotlinParser.ParenthesizedAssignableExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parenthesizedAssignableExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitParenthesizedAssignableExpression(ctx: KotlinParser.ParenthesizedAssignableExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.assignableSuffix].
     *
     * @param ctx The parse tree
     */
    public fun enterAssignableSuffix(ctx: KotlinParser.AssignableSuffixContext)

    /**
     * Exit a parse tree produced by [KotlinParser.assignableSuffix].
     *
     * @param ctx The parse tree
     */
    public fun exitAssignableSuffix(ctx: KotlinParser.AssignableSuffixContext)

    /**
     * Enter a parse tree produced by [KotlinParser.indexingSuffix].
     *
     * @param ctx The parse tree
     */
    public fun enterIndexingSuffix(ctx: KotlinParser.IndexingSuffixContext)

    /**
     * Exit a parse tree produced by [KotlinParser.indexingSuffix].
     *
     * @param ctx The parse tree
     */
    public fun exitIndexingSuffix(ctx: KotlinParser.IndexingSuffixContext)

    /**
     * Enter a parse tree produced by [KotlinParser.navigationSuffix].
     *
     * @param ctx The parse tree
     */
    public fun enterNavigationSuffix(ctx: KotlinParser.NavigationSuffixContext)

    /**
     * Exit a parse tree produced by [KotlinParser.navigationSuffix].
     *
     * @param ctx The parse tree
     */
    public fun exitNavigationSuffix(ctx: KotlinParser.NavigationSuffixContext)

    /**
     * Enter a parse tree produced by [KotlinParser.callSuffix].
     *
     * @param ctx The parse tree
     */
    public fun enterCallSuffix(ctx: KotlinParser.CallSuffixContext)

    /**
     * Exit a parse tree produced by [KotlinParser.callSuffix].
     *
     * @param ctx The parse tree
     */
    public fun exitCallSuffix(ctx: KotlinParser.CallSuffixContext)

    /**
     * Enter a parse tree produced by [KotlinParser.annotatedLambda].
     *
     * @param ctx The parse tree
     */
    public fun enterAnnotatedLambda(ctx: KotlinParser.AnnotatedLambdaContext)

    /**
     * Exit a parse tree produced by [KotlinParser.annotatedLambda].
     *
     * @param ctx The parse tree
     */
    public fun exitAnnotatedLambda(ctx: KotlinParser.AnnotatedLambdaContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeArguments].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeArguments(ctx: KotlinParser.TypeArgumentsContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeArguments].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeArguments(ctx: KotlinParser.TypeArgumentsContext)

    /**
     * Enter a parse tree produced by [KotlinParser.valueArguments].
     *
     * @param ctx The parse tree
     */
    public fun enterValueArguments(ctx: KotlinParser.ValueArgumentsContext)

    /**
     * Exit a parse tree produced by [KotlinParser.valueArguments].
     *
     * @param ctx The parse tree
     */
    public fun exitValueArguments(ctx: KotlinParser.ValueArgumentsContext)

    /**
     * Enter a parse tree produced by [KotlinParser.valueArgument].
     *
     * @param ctx The parse tree
     */
    public fun enterValueArgument(ctx: KotlinParser.ValueArgumentContext)

    /**
     * Exit a parse tree produced by [KotlinParser.valueArgument].
     *
     * @param ctx The parse tree
     */
    public fun exitValueArgument(ctx: KotlinParser.ValueArgumentContext)

    /**
     * Enter a parse tree produced by [KotlinParser.primaryExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterPrimaryExpression(ctx: KotlinParser.PrimaryExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.primaryExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitPrimaryExpression(ctx: KotlinParser.PrimaryExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parenthesizedExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterParenthesizedExpression(ctx: KotlinParser.ParenthesizedExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parenthesizedExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitParenthesizedExpression(ctx: KotlinParser.ParenthesizedExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.collectionLiteral].
     *
     * @param ctx The parse tree
     */
    public fun enterCollectionLiteral(ctx: KotlinParser.CollectionLiteralContext)

    /**
     * Exit a parse tree produced by [KotlinParser.collectionLiteral].
     *
     * @param ctx The parse tree
     */
    public fun exitCollectionLiteral(ctx: KotlinParser.CollectionLiteralContext)

    /**
     * Enter a parse tree produced by [KotlinParser.literalConstant].
     *
     * @param ctx The parse tree
     */
    public fun enterLiteralConstant(ctx: KotlinParser.LiteralConstantContext)

    /**
     * Exit a parse tree produced by [KotlinParser.literalConstant].
     *
     * @param ctx The parse tree
     */
    public fun exitLiteralConstant(ctx: KotlinParser.LiteralConstantContext)

    /**
     * Enter a parse tree produced by [KotlinParser.stringLiteral].
     *
     * @param ctx The parse tree
     */
    public fun enterStringLiteral(ctx: KotlinParser.StringLiteralContext)

    /**
     * Exit a parse tree produced by [KotlinParser.stringLiteral].
     *
     * @param ctx The parse tree
     */
    public fun exitStringLiteral(ctx: KotlinParser.StringLiteralContext)

    /**
     * Enter a parse tree produced by [KotlinParser.lineStringLiteral].
     *
     * @param ctx The parse tree
     */
    public fun enterLineStringLiteral(ctx: KotlinParser.LineStringLiteralContext)

    /**
     * Exit a parse tree produced by [KotlinParser.lineStringLiteral].
     *
     * @param ctx The parse tree
     */
    public fun exitLineStringLiteral(ctx: KotlinParser.LineStringLiteralContext)

    /**
     * Enter a parse tree produced by [KotlinParser.multiLineStringLiteral].
     *
     * @param ctx The parse tree
     */
    public fun enterMultiLineStringLiteral(ctx: KotlinParser.MultiLineStringLiteralContext)

    /**
     * Exit a parse tree produced by [KotlinParser.multiLineStringLiteral].
     *
     * @param ctx The parse tree
     */
    public fun exitMultiLineStringLiteral(ctx: KotlinParser.MultiLineStringLiteralContext)

    /**
     * Enter a parse tree produced by [KotlinParser.lineStringContent].
     *
     * @param ctx The parse tree
     */
    public fun enterLineStringContent(ctx: KotlinParser.LineStringContentContext)

    /**
     * Exit a parse tree produced by [KotlinParser.lineStringContent].
     *
     * @param ctx The parse tree
     */
    public fun exitLineStringContent(ctx: KotlinParser.LineStringContentContext)

    /**
     * Enter a parse tree produced by [KotlinParser.lineStringExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterLineStringExpression(ctx: KotlinParser.LineStringExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.lineStringExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitLineStringExpression(ctx: KotlinParser.LineStringExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.multiLineStringContent].
     *
     * @param ctx The parse tree
     */
    public fun enterMultiLineStringContent(ctx: KotlinParser.MultiLineStringContentContext)

    /**
     * Exit a parse tree produced by [KotlinParser.multiLineStringContent].
     *
     * @param ctx The parse tree
     */
    public fun exitMultiLineStringContent(ctx: KotlinParser.MultiLineStringContentContext)

    /**
     * Enter a parse tree produced by [KotlinParser.multiLineStringExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterMultiLineStringExpression(ctx: KotlinParser.MultiLineStringExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.multiLineStringExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitMultiLineStringExpression(ctx: KotlinParser.MultiLineStringExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.lambdaLiteral].
     *
     * @param ctx The parse tree
     */
    public fun enterLambdaLiteral(ctx: KotlinParser.LambdaLiteralContext)

    /**
     * Exit a parse tree produced by [KotlinParser.lambdaLiteral].
     *
     * @param ctx The parse tree
     */
    public fun exitLambdaLiteral(ctx: KotlinParser.LambdaLiteralContext)

    /**
     * Enter a parse tree produced by [KotlinParser.lambdaParameters].
     *
     * @param ctx The parse tree
     */
    public fun enterLambdaParameters(ctx: KotlinParser.LambdaParametersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.lambdaParameters].
     *
     * @param ctx The parse tree
     */
    public fun exitLambdaParameters(ctx: KotlinParser.LambdaParametersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.lambdaParameter].
     *
     * @param ctx The parse tree
     */
    public fun enterLambdaParameter(ctx: KotlinParser.LambdaParameterContext)

    /**
     * Exit a parse tree produced by [KotlinParser.lambdaParameter].
     *
     * @param ctx The parse tree
     */
    public fun exitLambdaParameter(ctx: KotlinParser.LambdaParameterContext)

    /**
     * Enter a parse tree produced by [KotlinParser.anonymousFunction].
     *
     * @param ctx The parse tree
     */
    public fun enterAnonymousFunction(ctx: KotlinParser.AnonymousFunctionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.anonymousFunction].
     *
     * @param ctx The parse tree
     */
    public fun exitAnonymousFunction(ctx: KotlinParser.AnonymousFunctionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.functionLiteral].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionLiteral(ctx: KotlinParser.FunctionLiteralContext)

    /**
     * Exit a parse tree produced by [KotlinParser.functionLiteral].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionLiteral(ctx: KotlinParser.FunctionLiteralContext)

    /**
     * Enter a parse tree produced by [KotlinParser.objectLiteral].
     *
     * @param ctx The parse tree
     */
    public fun enterObjectLiteral(ctx: KotlinParser.ObjectLiteralContext)

    /**
     * Exit a parse tree produced by [KotlinParser.objectLiteral].
     *
     * @param ctx The parse tree
     */
    public fun exitObjectLiteral(ctx: KotlinParser.ObjectLiteralContext)

    /**
     * Enter a parse tree produced by [KotlinParser.thisExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterThisExpression(ctx: KotlinParser.ThisExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.thisExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitThisExpression(ctx: KotlinParser.ThisExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.superExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterSuperExpression(ctx: KotlinParser.SuperExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.superExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitSuperExpression(ctx: KotlinParser.SuperExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.ifExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterIfExpression(ctx: KotlinParser.IfExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.ifExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitIfExpression(ctx: KotlinParser.IfExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.whenSubject].
     *
     * @param ctx The parse tree
     */
    public fun enterWhenSubject(ctx: KotlinParser.WhenSubjectContext)

    /**
     * Exit a parse tree produced by [KotlinParser.whenSubject].
     *
     * @param ctx The parse tree
     */
    public fun exitWhenSubject(ctx: KotlinParser.WhenSubjectContext)

    /**
     * Enter a parse tree produced by [KotlinParser.whenExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterWhenExpression(ctx: KotlinParser.WhenExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.whenExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitWhenExpression(ctx: KotlinParser.WhenExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.whenEntry].
     *
     * @param ctx The parse tree
     */
    public fun enterWhenEntry(ctx: KotlinParser.WhenEntryContext)

    /**
     * Exit a parse tree produced by [KotlinParser.whenEntry].
     *
     * @param ctx The parse tree
     */
    public fun exitWhenEntry(ctx: KotlinParser.WhenEntryContext)

    /**
     * Enter a parse tree produced by [KotlinParser.whenEntryAddition].
     *
     * @param ctx The parse tree
     */
    public fun enterWhenEntryAddition(ctx: KotlinParser.WhenEntryAdditionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.whenEntryAddition].
     *
     * @param ctx The parse tree
     */
    public fun exitWhenEntryAddition(ctx: KotlinParser.WhenEntryAdditionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.whenEntryGuard].
     *
     * @param ctx The parse tree
     */
    public fun enterWhenEntryGuard(ctx: KotlinParser.WhenEntryGuardContext)

    /**
     * Exit a parse tree produced by [KotlinParser.whenEntryGuard].
     *
     * @param ctx The parse tree
     */
    public fun exitWhenEntryGuard(ctx: KotlinParser.WhenEntryGuardContext)

    /**
     * Enter a parse tree produced by [KotlinParser.whenCondition].
     *
     * @param ctx The parse tree
     */
    public fun enterWhenCondition(ctx: KotlinParser.WhenConditionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.whenCondition].
     *
     * @param ctx The parse tree
     */
    public fun exitWhenCondition(ctx: KotlinParser.WhenConditionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.rangeTest].
     *
     * @param ctx The parse tree
     */
    public fun enterRangeTest(ctx: KotlinParser.RangeTestContext)

    /**
     * Exit a parse tree produced by [KotlinParser.rangeTest].
     *
     * @param ctx The parse tree
     */
    public fun exitRangeTest(ctx: KotlinParser.RangeTestContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeTest].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeTest(ctx: KotlinParser.TypeTestContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeTest].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeTest(ctx: KotlinParser.TypeTestContext)

    /**
     * Enter a parse tree produced by [KotlinParser.tryExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterTryExpression(ctx: KotlinParser.TryExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.tryExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitTryExpression(ctx: KotlinParser.TryExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.catchBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterCatchBlock(ctx: KotlinParser.CatchBlockContext)

    /**
     * Exit a parse tree produced by [KotlinParser.catchBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitCatchBlock(ctx: KotlinParser.CatchBlockContext)

    /**
     * Enter a parse tree produced by [KotlinParser.finallyBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterFinallyBlock(ctx: KotlinParser.FinallyBlockContext)

    /**
     * Exit a parse tree produced by [KotlinParser.finallyBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitFinallyBlock(ctx: KotlinParser.FinallyBlockContext)

    /**
     * Enter a parse tree produced by [KotlinParser.jumpExpression].
     *
     * @param ctx The parse tree
     */
    public fun enterJumpExpression(ctx: KotlinParser.JumpExpressionContext)

    /**
     * Exit a parse tree produced by [KotlinParser.jumpExpression].
     *
     * @param ctx The parse tree
     */
    public fun exitJumpExpression(ctx: KotlinParser.JumpExpressionContext)

    /**
     * Enter a parse tree produced by [KotlinParser.callableReference].
     *
     * @param ctx The parse tree
     */
    public fun enterCallableReference(ctx: KotlinParser.CallableReferenceContext)

    /**
     * Exit a parse tree produced by [KotlinParser.callableReference].
     *
     * @param ctx The parse tree
     */
    public fun exitCallableReference(ctx: KotlinParser.CallableReferenceContext)

    /**
     * Enter a parse tree produced by [KotlinParser.assignmentAndOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterAssignmentAndOperator(ctx: KotlinParser.AssignmentAndOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.assignmentAndOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitAssignmentAndOperator(ctx: KotlinParser.AssignmentAndOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.equalityOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterEqualityOperator(ctx: KotlinParser.EqualityOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.equalityOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitEqualityOperator(ctx: KotlinParser.EqualityOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.comparisonOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterComparisonOperator(ctx: KotlinParser.ComparisonOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.comparisonOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitComparisonOperator(ctx: KotlinParser.ComparisonOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.inOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterInOperator(ctx: KotlinParser.InOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.inOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitInOperator(ctx: KotlinParser.InOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.isOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterIsOperator(ctx: KotlinParser.IsOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.isOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitIsOperator(ctx: KotlinParser.IsOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.additiveOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterAdditiveOperator(ctx: KotlinParser.AdditiveOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.additiveOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitAdditiveOperator(ctx: KotlinParser.AdditiveOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.multiplicativeOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterMultiplicativeOperator(ctx: KotlinParser.MultiplicativeOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.multiplicativeOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitMultiplicativeOperator(ctx: KotlinParser.MultiplicativeOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.asOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterAsOperator(ctx: KotlinParser.AsOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.asOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitAsOperator(ctx: KotlinParser.AsOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.prefixUnaryOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterPrefixUnaryOperator(ctx: KotlinParser.PrefixUnaryOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.prefixUnaryOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitPrefixUnaryOperator(ctx: KotlinParser.PrefixUnaryOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.postfixUnaryOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterPostfixUnaryOperator(ctx: KotlinParser.PostfixUnaryOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.postfixUnaryOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitPostfixUnaryOperator(ctx: KotlinParser.PostfixUnaryOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.excl].
     *
     * @param ctx The parse tree
     */
    public fun enterExcl(ctx: KotlinParser.ExclContext)

    /**
     * Exit a parse tree produced by [KotlinParser.excl].
     *
     * @param ctx The parse tree
     */
    public fun exitExcl(ctx: KotlinParser.ExclContext)

    /**
     * Enter a parse tree produced by [KotlinParser.memberAccessOperator].
     *
     * @param ctx The parse tree
     */
    public fun enterMemberAccessOperator(ctx: KotlinParser.MemberAccessOperatorContext)

    /**
     * Exit a parse tree produced by [KotlinParser.memberAccessOperator].
     *
     * @param ctx The parse tree
     */
    public fun exitMemberAccessOperator(ctx: KotlinParser.MemberAccessOperatorContext)

    /**
     * Enter a parse tree produced by [KotlinParser.safeNav].
     *
     * @param ctx The parse tree
     */
    public fun enterSafeNav(ctx: KotlinParser.SafeNavContext)

    /**
     * Exit a parse tree produced by [KotlinParser.safeNav].
     *
     * @param ctx The parse tree
     */
    public fun exitSafeNav(ctx: KotlinParser.SafeNavContext)

    /**
     * Enter a parse tree produced by [KotlinParser.modifiers].
     *
     * @param ctx The parse tree
     */
    public fun enterModifiers(ctx: KotlinParser.ModifiersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.modifiers].
     *
     * @param ctx The parse tree
     */
    public fun exitModifiers(ctx: KotlinParser.ModifiersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parameterModifiers].
     *
     * @param ctx The parse tree
     */
    public fun enterParameterModifiers(ctx: KotlinParser.ParameterModifiersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parameterModifiers].
     *
     * @param ctx The parse tree
     */
    public fun exitParameterModifiers(ctx: KotlinParser.ParameterModifiersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.modifier].
     *
     * @param ctx The parse tree
     */
    public fun enterModifier(ctx: KotlinParser.ModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.modifier].
     *
     * @param ctx The parse tree
     */
    public fun exitModifier(ctx: KotlinParser.ModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeModifiers].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeModifiers(ctx: KotlinParser.TypeModifiersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeModifiers].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeModifiers(ctx: KotlinParser.TypeModifiersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeModifier(ctx: KotlinParser.TypeModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeModifier(ctx: KotlinParser.TypeModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.classModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterClassModifier(ctx: KotlinParser.ClassModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.classModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitClassModifier(ctx: KotlinParser.ClassModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.memberModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterMemberModifier(ctx: KotlinParser.MemberModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.memberModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitMemberModifier(ctx: KotlinParser.MemberModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.visibilityModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterVisibilityModifier(ctx: KotlinParser.VisibilityModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.visibilityModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitVisibilityModifier(ctx: KotlinParser.VisibilityModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.varianceModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterVarianceModifier(ctx: KotlinParser.VarianceModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.varianceModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitVarianceModifier(ctx: KotlinParser.VarianceModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeParameterModifiers].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeParameterModifiers(ctx: KotlinParser.TypeParameterModifiersContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeParameterModifiers].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeParameterModifiers(ctx: KotlinParser.TypeParameterModifiersContext)

    /**
     * Enter a parse tree produced by [KotlinParser.typeParameterModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterTypeParameterModifier(ctx: KotlinParser.TypeParameterModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.typeParameterModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitTypeParameterModifier(ctx: KotlinParser.TypeParameterModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.functionModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionModifier(ctx: KotlinParser.FunctionModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.functionModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionModifier(ctx: KotlinParser.FunctionModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.propertyModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterPropertyModifier(ctx: KotlinParser.PropertyModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.propertyModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitPropertyModifier(ctx: KotlinParser.PropertyModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.inheritanceModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterInheritanceModifier(ctx: KotlinParser.InheritanceModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.inheritanceModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitInheritanceModifier(ctx: KotlinParser.InheritanceModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.parameterModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterParameterModifier(ctx: KotlinParser.ParameterModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.parameterModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitParameterModifier(ctx: KotlinParser.ParameterModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.reificationModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterReificationModifier(ctx: KotlinParser.ReificationModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.reificationModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitReificationModifier(ctx: KotlinParser.ReificationModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.platformModifier].
     *
     * @param ctx The parse tree
     */
    public fun enterPlatformModifier(ctx: KotlinParser.PlatformModifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.platformModifier].
     *
     * @param ctx The parse tree
     */
    public fun exitPlatformModifier(ctx: KotlinParser.PlatformModifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.annotation].
     *
     * @param ctx The parse tree
     */
    public fun enterAnnotation(ctx: KotlinParser.AnnotationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.annotation].
     *
     * @param ctx The parse tree
     */
    public fun exitAnnotation(ctx: KotlinParser.AnnotationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.singleAnnotation].
     *
     * @param ctx The parse tree
     */
    public fun enterSingleAnnotation(ctx: KotlinParser.SingleAnnotationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.singleAnnotation].
     *
     * @param ctx The parse tree
     */
    public fun exitSingleAnnotation(ctx: KotlinParser.SingleAnnotationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.multiAnnotation].
     *
     * @param ctx The parse tree
     */
    public fun enterMultiAnnotation(ctx: KotlinParser.MultiAnnotationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.multiAnnotation].
     *
     * @param ctx The parse tree
     */
    public fun exitMultiAnnotation(ctx: KotlinParser.MultiAnnotationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.annotationUseSiteTarget].
     *
     * @param ctx The parse tree
     */
    public fun enterAnnotationUseSiteTarget(ctx: KotlinParser.AnnotationUseSiteTargetContext)

    /**
     * Exit a parse tree produced by [KotlinParser.annotationUseSiteTarget].
     *
     * @param ctx The parse tree
     */
    public fun exitAnnotationUseSiteTarget(ctx: KotlinParser.AnnotationUseSiteTargetContext)

    /**
     * Enter a parse tree produced by [KotlinParser.unescapedAnnotation].
     *
     * @param ctx The parse tree
     */
    public fun enterUnescapedAnnotation(ctx: KotlinParser.UnescapedAnnotationContext)

    /**
     * Exit a parse tree produced by [KotlinParser.unescapedAnnotation].
     *
     * @param ctx The parse tree
     */
    public fun exitUnescapedAnnotation(ctx: KotlinParser.UnescapedAnnotationContext)

    /**
     * Enter a parse tree produced by [KotlinParser.simpleIdentifier].
     *
     * @param ctx The parse tree
     */
    public fun enterSimpleIdentifier(ctx: KotlinParser.SimpleIdentifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.simpleIdentifier].
     *
     * @param ctx The parse tree
     */
    public fun exitSimpleIdentifier(ctx: KotlinParser.SimpleIdentifierContext)

    /**
     * Enter a parse tree produced by [KotlinParser.identifier].
     *
     * @param ctx The parse tree
     */
    public fun enterIdentifier(ctx: KotlinParser.IdentifierContext)

    /**
     * Exit a parse tree produced by [KotlinParser.identifier].
     *
     * @param ctx The parse tree
     */
    public fun exitIdentifier(ctx: KotlinParser.IdentifierContext)

}
