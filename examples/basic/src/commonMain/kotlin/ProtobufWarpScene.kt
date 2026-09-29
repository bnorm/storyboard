import androidx.compose.material.Text
import dev.bnorm.storyboard.BasicCode
import dev.bnorm.storyboard.ProtobufV3
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.ui.warp
import org.intellij.lang.annotations.Language

@Language("protobuf")
val PROTOBUF_SLIDES = listOf(
    """
      syntax = "proto3";

      package com.example;

    """.trimIndent(),

    """
        syntax = "proto3";
    
        package com.example;
        
        message SearchRequest {}
    
    """.trimIndent(),



        """
        syntax = "proto3";
    
        package com.example;
    
        message SearchRequest {
          string query = 1;
          int32 page_number = 2;
          int32 results_per_page = 3;
        }
    """.trimIndent(),

    """
        syntax = "proto3";
    
        package com.example;
    
        enum Corpus {}
        
        message SearchRequest {
          string query = 1;
          int32 page_number = 2;
          int32 results_per_page = 3;
          Corpus corpus = 4;
        }
    """.trimIndent(),

    """
        syntax = "proto3";
    
        package com.example;
    
        enum Corpus {
          CORPUS_UNSPECIFIED = 0;
          CORPUS_UNIVERSAL = 1;
          CORPUS_WEB = 2;
          CORPUS_IMAGES = 3;
          CORPUS_LOCAL = 4;
          CORPUS_NEWS = 5;
          CORPUS_PRODUCTS = 6;
          CORPUS_VIDEO = 7;
        }
        
        message SearchRequest {
          string query = 1;
          int32 page_number = 2;
          int32 results_per_page = 3;
          Corpus corpus = 4;
        }
    """.trimIndent(),
).map { BasicCode(code = it) }


@Suppress("FunctionName")
fun StoryboardBuilder.ProtobufWarpScene() = warp(
    codeBlocks = PROTOBUF_SLIDES,
    language = ProtobufV3,
    header = {
        Text("Code Scene")
    }
)
