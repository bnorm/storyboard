// Generated from /Users/brian/code/bnorm/deck.bnorm.dev/storyboard/storyboard-text/antlr/xml/XMLParser.g4 by ANTLR 4.13.1
package dev.bnorm.storyboard.text.highlight.antlr.xml

import org.antlr.v4.kotlinruntime.tree.ParseTreeListener

/**
 * This interface defines a complete listener for a parse tree produced by [XMLParser].
 */
internal interface XMLParserListener : ParseTreeListener {
    /**
     * Enter a parse tree produced by [XMLParser.document].
     *
     * @param ctx The parse tree
     */
    public fun enterDocument(ctx: XMLParser.DocumentContext)

    /**
     * Exit a parse tree produced by [XMLParser.document].
     *
     * @param ctx The parse tree
     */
    public fun exitDocument(ctx: XMLParser.DocumentContext)

    /**
     * Enter a parse tree produced by [XMLParser.prolog].
     *
     * @param ctx The parse tree
     */
    public fun enterProlog(ctx: XMLParser.PrologContext)

    /**
     * Exit a parse tree produced by [XMLParser.prolog].
     *
     * @param ctx The parse tree
     */
    public fun exitProlog(ctx: XMLParser.PrologContext)

    /**
     * Enter a parse tree produced by [XMLParser.content].
     *
     * @param ctx The parse tree
     */
    public fun enterContent(ctx: XMLParser.ContentContext)

    /**
     * Exit a parse tree produced by [XMLParser.content].
     *
     * @param ctx The parse tree
     */
    public fun exitContent(ctx: XMLParser.ContentContext)

    /**
     * Enter a parse tree produced by [XMLParser.element].
     *
     * @param ctx The parse tree
     */
    public fun enterElement(ctx: XMLParser.ElementContext)

    /**
     * Exit a parse tree produced by [XMLParser.element].
     *
     * @param ctx The parse tree
     */
    public fun exitElement(ctx: XMLParser.ElementContext)

    /**
     * Enter a parse tree produced by [XMLParser.reference].
     *
     * @param ctx The parse tree
     */
    public fun enterReference(ctx: XMLParser.ReferenceContext)

    /**
     * Exit a parse tree produced by [XMLParser.reference].
     *
     * @param ctx The parse tree
     */
    public fun exitReference(ctx: XMLParser.ReferenceContext)

    /**
     * Enter a parse tree produced by [XMLParser.attribute].
     *
     * @param ctx The parse tree
     */
    public fun enterAttribute(ctx: XMLParser.AttributeContext)

    /**
     * Exit a parse tree produced by [XMLParser.attribute].
     *
     * @param ctx The parse tree
     */
    public fun exitAttribute(ctx: XMLParser.AttributeContext)

    /**
     * Enter a parse tree produced by [XMLParser.chardata].
     *
     * @param ctx The parse tree
     */
    public fun enterChardata(ctx: XMLParser.ChardataContext)

    /**
     * Exit a parse tree produced by [XMLParser.chardata].
     *
     * @param ctx The parse tree
     */
    public fun exitChardata(ctx: XMLParser.ChardataContext)

    /**
     * Enter a parse tree produced by [XMLParser.misc].
     *
     * @param ctx The parse tree
     */
    public fun enterMisc(ctx: XMLParser.MiscContext)

    /**
     * Exit a parse tree produced by [XMLParser.misc].
     *
     * @param ctx The parse tree
     */
    public fun exitMisc(ctx: XMLParser.MiscContext)

}
