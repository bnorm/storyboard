import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.assist.SceneCaption
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.example.shared.JetBrainsMono
import dev.bnorm.storyboard.toState
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalTransitionApi::class)
fun StoryboardBuilder.HttpClientScene() {
    // !!!
    // Captions are only captured and rendered within the Assistant window.
    // This means composition-local state is not shared with the Story window.
    // Instead, hoist the state outside the Storyboard itself,
    // so it is shared wherever the scene may be displayed!
    //
    // This is achieved here using captured local variables.
    // But a more elegant solution may involve a view-model-like class,
    // passed to the 'HttpClientScene' function as a parameter.
    // !!!
    val repository = TextFieldState("bnorm/storyboard")
    var error by mutableStateOf<String?>(null)
    var stargazers by mutableIntStateOf(-1)

    scene(stateCount = 3) {
        SceneCaption {
            // !!!
            // Make sure the LaunchedEffect is properly scoped.
            // Running this within the Scene may cause it to trigger multiple times.
            // The Scene could be displayed twice, in both the Story and Assistant windows.
            // By restricting to the caption, we make sure it is only launched a single time.
            // However, this means the Assistant window must be open for the effect to launch!
            // !!!
            LaunchedEffect(repository.text) {
                error = null
                delay(300)
                try {
                    val response = client.get("https://api.github.com/repos/${repository.text}")
                    if (response.status.isSuccess()) {
                        stargazers = response.body<Repository>().stargazers
                    } else {
                        stargazers = -1
                        error = "Error retrieving repository information: ${response.status}"
                    }
                } catch (e: Throwable) {
                    if (e is CancellationException) throw e
                    stargazers = -1
                    error = e.stackTraceToString()
                }
            }

            Column {
                TextField(repository, label = { Text("GitHub Repository") })
                if (error != null) {
                    Text(error.toString(), fontFamily = JetBrainsMono)
                }
            }
        }

        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Header { Text("HTTP Clients") }
            Divider(color = MaterialTheme.colors.primary, thickness = 4.dp)
            Body {
                Column(
                    Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    RevealEach(frame.createChildTransition { it.toState() }) {
                        item { Text("• Scenes aren't the only thing that can be interactive.") }
                        item { Text("• Press 'F2' to check the Story Assistant. (Desktop only!)") }
                        item {
                            if (stargazers < 0) {
                                Text("• Unable to retrieve information about '${repository.text}'!")
                            } else {
                                Text("• Did you know, '${repository.text}' has $stargazers stars on GitHub!")
                            }
                        }
                    }
                }
            }
        }
    }
}

private val client = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

@Serializable
private class Repository(
    @SerialName("stargazers_count")
    val stargazers: Int,
)
