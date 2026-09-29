package dev.bnorm.storyboard

import kotlin.test.Test

class ProtobufParserTest {
    @Test
    fun testParseTokens() {
        val tokens = parse(
            code = """
                syntax = "proto3";
        
                package com.example;
            """.trimIndent(),
            language = ProtobufV3
        )
        println(tokens)
    }

    @Test
    fun testParseTokensWithMessage() {
        val tokens = parse(
            code = """
                syntax = "proto3";
        
                package com.example;
        
                message SearchRequest {
                  string query = 1;
                  int32 page_number = 2;
                  int32 results_per_page = 3;
                }
            """.trimIndent(),
            language = ProtobufV3
        )
        tokens.forEach { println(it) }
    }
}
