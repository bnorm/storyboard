package ch.deletescape.highlight.core

/*
 * Created by Sergej Kravcenko on 5/16/2017.
 * Copyright (c) 2017 Sergej Kravcenko
 */

/**
 * Interface definition for callbacks to be invoked when the code
 * is being parsed for syntax highlight.
 */
interface StyleRenderer<out T> {
   /**
    * Callback method to be invoked when parser starts code processing.
    */
   fun onStart()

   /**
    * Callback method to be invoked when parser finishes code processing.
    */
   fun onFinish()

   /**
    * Callback method to be invoked when parser finds start of lexeme.
    *
    * @param style span style name
    */
   fun onPushStyle(style: String)

   /**
    * Callback method to be invoked when parser finds end of lexeme.
    */
   fun onPopStyle()

   /**
    * Callback method to be invoked when parser finds complete code
    * lexeme. Code lexeme style is specified by last {@link StyleRenderer#onPushStyle(String)}
    * call.
    *
    * @param codeLexeme code lexeme
    */
   fun onPushCodeBlock(codeLexeme: CharSequence)

   /**
    * Callback method to be invoked when parser finds sub-language block inside the
    * code.
    *
    * @param name language name
    * @param code code block processed and highlighted by another {@link StyleRenderer}
    */
   fun onPushSubLanguage(name: String?, code: CharSequence?)

   /**
    * Callback method to be invoked when parser cannot continue code processing due
    * an error.
    *
    * @param code code string passed to {@link Highlighter#highlight(String, String)} method
    */
   fun onAbort(code: CharSequence)

   /**
    * Returns result of code highlighting.
    *
    * @return highlighted code
    */
   fun getResult(): T
}
