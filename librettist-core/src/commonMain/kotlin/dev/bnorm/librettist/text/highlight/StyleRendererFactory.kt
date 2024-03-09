package ch.deletescape.highlight.core

/*
 * Created by Sergej Kravcenko on 5/16/2017.
 * Copyright (c) 2017 Sergej Kravcenko
 */

/**
 * Factory that creates style renderers used for applying style to
 * code parts.
 */
fun interface StyleRendererFactory<out T> {
   /**
    * Creates style renderer for provided language name.
    *
    * @param languageName language name
    *
    * @return style renderer
    */
   fun create(languageName: String): StyleRenderer<T>
}
