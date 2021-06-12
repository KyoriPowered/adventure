/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import javax.inject.Inject

internal const val ADVENTURE_PREFIX = "adventure-"

/**
 * Copy project javadoc into the `adventure-javadoc` directory tree
 */
abstract class CopyJavadoc : DefaultTask() {
  @get:Input
  abstract val projectName: Property<String>

  @get:Input
  abstract val projectVersion: Property<String>

  @get:InputFiles
  abstract val javadocFiles: ConfigurableFileCollection

  @get:Internal
  abstract val rootDir: DirectoryProperty

  @get:Internal
  @get:Option(option="output", description="The root of the adventure-javadocs repository")
  abstract val output: Property<String>

  @get:OutputDirectory
  abstract val outputDir: DirectoryProperty

  @get:Inject
  protected abstract val fsOps: FileSystemOperations

  init {
    // relative to project root, <output>/<projectName>/<projectVersion>
    outputDir.set(rootDir.dir(output).flatMap { it.dir(this.projectName).flatMap { it.dir(this.projectVersion) } })
  }

  @TaskAction
  fun doTransfer() {
    val dest = outputDir.get().asFile

    dest.deleteRecursively()
    dest.mkdirs()

    fsOps.copy {
      from(javadocFiles)
      into(dest)
    }
  }
}
